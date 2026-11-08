package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

import java.util.List;
import java.util.stream.Collectors;

abstract class DynamoDbDao<T extends BeholderObject> implements BeholderDao {
    private static final Expression NEW_RECORD = Expression.builder()
            .expression("attribute_not_exists(objectId)")
            .build();

    protected final DynamoDbTable<T> table;
    private final Class<T> modelType;
    private final String nameIndex;

    DynamoDbDao(DynamoDbEnhancedClient client, String tableName, Class<T> modelType, String nameIndex) {
        this.table = client.table(tableName, TableSchema.fromBean(modelType));
        this.modelType = modelType;
        this.nameIndex = nameIndex;
    }

    @Override
    public T getSingle(String userEmail, String objectId) {
        return table.getItem(key(userEmail, objectId));
    }

    @Override
    public List<T> getMultiple(String userEmail) {
        Key owner = Key.builder().partitionValue(userEmail).build();
        return table.query(QueryConditional.keyEqualTo(owner)).items().stream().collect(Collectors.toList());
    }

    @Override
    public void writeObject(BeholderObject object) {
        T item = modelType.cast(object);
        table.updateItem(request -> request.item(item).returnValues(ReturnValue.NONE));
    }

    @Override
    public void createIfAbsent(BeholderObject object) {
        T item = modelType.cast(object);
        table.putItem(request -> request.item(item).conditionExpression(NEW_RECORD));
    }

    @Override
    public void deleteObject(String userEmail, String objectId) {
        table.deleteItem(key(userEmail, objectId));
    }

    @Override
    public Boolean objectNameExists(String userEmail, String objectName) {
        return findByName(userEmail, objectName) != null;
    }

    protected T findByName(String userEmail, String objectName) {
        return queryIndex(nameIndex, userEmail, objectName).items().stream().findFirst().orElse(null);
    }

    protected PageIterable<T> queryIndex(String index, String userEmail, String sortValue) {
        return PageIterable.create(table.index(index).query(QueryConditional.keyEqualTo(key(userEmail, sortValue))));
    }

    protected static Key key(String userEmail, String sortValue) {
        return Key.builder().partitionValue(userEmail).sortValue(sortValue).build();
    }
}

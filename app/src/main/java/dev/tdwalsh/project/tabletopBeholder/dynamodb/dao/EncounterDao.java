package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EncounterDao extends DynamoDbDao<Encounter> {
    @Inject
    public EncounterDao(DynamoDbEnhancedClient client) {
        super(client, Encounter.TABLE_NAME, Encounter.class, null);
    }

    @Override
    public Encounter getSingle(String userEmail, String objectId) {
        return table.getItem(request -> request.key(key(userEmail, objectId)).consistentRead(true));
    }

    @Override
    public void writeObject(BeholderObject object) {
        Encounter encounter = (Encounter) object;
        Long version = encounter.getVersion();
        Expression.Builder condition = Expression.builder();
        if (version == null) {
            condition.expression("attribute_exists(objectId) AND attribute_not_exists(#version)");
        } else {
            condition.expression("attribute_exists(objectId) AND #version = :version")
                    .putExpressionValue(":version", AttributeValue.fromN(version.toString()));
        }
        Expression expectedVersion = condition.putExpressionName("#version", "version").build();

        encounter.setVersion(version == null ? 1L : version + 1);
        try {
            table.updateItem(request -> request.item(encounter)
                    .conditionExpression(expectedVersion).returnValues(ReturnValue.NONE));
        } catch (RuntimeException exception) {
            encounter.setVersion(version);
            throw exception;
        }
    }

    @Override
    public void createIfAbsent(BeholderObject object) {
        ((Encounter) object).setVersion(1L);
        super.createIfAbsent(object);
    }

    @Override
    public Boolean objectNameExists(String userEmail, String objectName) {
        // The encounter table has a session index but no name index.
        Key owner = Key.builder().partitionValue(userEmail).build();
        Expression nameMatches = Expression.builder()
                .expression("objectName = :name")
                .expressionValues(Map.of(":name", AttributeValue.fromS(objectName)))
                .build();
        return table.query(request -> request.queryConditional(QueryConditional.keyEqualTo(owner))
                .filterExpression(nameMatches)).items().iterator().hasNext();
    }

    public List<Encounter> getEncounterBySession(String userEmail, String sessionId) {
        return queryIndex("EncountersSortBySessionIndex", userEmail, sessionId)
                .items().stream().collect(Collectors.toList());
    }
}

package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores, reads, updates, or deletes Action models in DynamoDB using {@link Action}.
 */

@Singleton
public class ActionDao implements BeholderDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a ActionDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public ActionDao(DynamoDBMapper mapper) { this.mapper = mapper; }

    /**
     * Retrieves a action by objectId and userEmail.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Action} if found, or null if none found
     */

    @Override
    public Action getSingle(String userEmail, String objectId) {
        return mapper.load(Action.class, userEmail, objectId);
    }

    /**
     * Retrieves all actions in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Action}, or an empty list if none found
     */

    @Override
    public List<? extends BeholderObject> getMultiple(String userEmail) {
        Action action = new Action();
        action.setUserEmail(userEmail);
        DynamoDBQueryExpression<Action> queryExpression = new DynamoDBQueryExpression<Action>()
                .withHashKeyValues(action);
        return mapper.query(Action.class, queryExpression);
    }

    /**
     * Updates a DynamoDB Action record with provided {@link Action} object, or creates a new record if one does not exist.
     *
     * @param beholderObject The {@link Action} to be saved
     */

    @Override
    public void writeObject(BeholderObject beholderObject) {
        mapper.save((Action) beholderObject);
    }

    /**
     * Removes a DynamoDB Action record matching provided userEmail and objectId.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     */

    @Override
    public void deleteObject(String userEmail, String objectId) {
        Action action = new Action();
        action.setUserEmail(userEmail);
        action.setObjectId(objectId);
        mapper.delete(action);
    }

    /**
     * Searches to determine whether a name exists already.
     *
     * @param userEmail The userEmail to search
     * @param objectName The objectName to search
     */
    @Override
    public Boolean objectNameExists(String userEmail, String objectName) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userEmail", new AttributeValue(userEmail));
        valueMap.put((":objectName"), new AttributeValue(objectName));
        DynamoDBQueryExpression<Action> queryExpression = new DynamoDBQueryExpression<Action>()
                .withIndexName("ActionsSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and objectName = :objectName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Action.class, queryExpression).isEmpty();
    }
}

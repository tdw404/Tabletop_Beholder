package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores, reads, updates, or deletes Action models in DynamoDB using {@link Action}.
 */

@Singleton
public class ActionDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a ActionDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public ActionDao(DynamoDBMapper mapper) { this.mapper = mapper; }

    /**
     * Retrieves an action by actionId and userEmail.
     *
     * @param actionId The actionId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Action} if found, or null if none found
     */

    public Action getSingleAction(String userEmail, String actionId) {
        return mapper.load(Action.class, userEmail, actionId);
    }

    /**
     * Retrieves all actions in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Action}, or an empty list if none found
     */

    public List<Action> getActionsByUser(String userEmail) {
        Action action = new Action();
        action.setUserEmail(userEmail);
        DynamoDBQueryExpression<Action> queryExpression = new DynamoDBQueryExpression<Action>()
                .withHashKeyValues(action);
        return mapper.query(Action.class, queryExpression);
    }

    /**
     * Updates a DynamoDB Action record with provided {@link Action} object, or creates a new record if one does not exist.
     *
     * @param action The {@link Action} to be saved
     */

    public void writeAction(Action action) {
        mapper.save(action);
    }

    /**
     * Removes a DynamoDB Action record matching provided userEmail and actionId.
     *
     * @param actionId The actionId to search
     * @param userEmail The userEmail to search
     */

    public void deleteAction(String userEmail, String actionId) {
        Action action = new Action();
        action.setUserEmail(userEmail);
        action.setObjectId(actionId);
        mapper.delete(action);
    }

    /**
     * Searches to determine whether a name exists already.
     *
     * @param userEmail The userEmail to search
     * @param actionName The name to search
     */
    public Boolean actionNameExists(String userEmail, String actionName) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userEmail", new AttributeValue(userEmail));
        valueMap.put(":actionName", new AttributeValue(actionName));
        DynamoDBQueryExpression<Action> queryExpression = new DynamoDBQueryExpression<Action>()
                .withIndexName("ActionsSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and actionName = :actionName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Action.class, queryExpression).isEmpty();
    }
}

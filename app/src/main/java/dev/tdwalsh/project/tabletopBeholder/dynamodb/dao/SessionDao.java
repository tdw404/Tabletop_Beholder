package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores, reads, updates, or deletes Session models in DynamoDB using {@link Session}.
 */

@Singleton
public class SessionDao implements BeholderDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a SessionDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public SessionDao(DynamoDBMapper mapper) { this.mapper = mapper; }

    /**
     * Retrieves a session by objectId and userEmail.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Session} if found, or null if none found
     */

    @Override
    public Session getSingle(String userEmail, String objectId) {
        return mapper.load(Session.class, userEmail, objectId);
    }

    /**
     * Retrieves all sessions in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Session}, or an empty list if none found
     */

    @Override
    public List<? extends BeholderObject> getMultiple(String userEmail) {
        Session session = new Session();
        session.setUserEmail(userEmail);
        DynamoDBQueryExpression<Session> queryExpression = new DynamoDBQueryExpression<Session>()
                .withHashKeyValues(session);
        return mapper.query(Session.class, queryExpression);
    }

    /**
     * Updates a DynamoDB Session record with provided {@link Session} object, or creates a new record if one does not exist.
     *
     * @param beholderObject The {@link Session} to be saved
     */

    @Override
    public void writeObject(BeholderObject beholderObject) {
        mapper.save((Session) beholderObject);
    }

    /**
     * Removes a DynamoDB Session record matching provided userEmail and objectId.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     */

    @Override
    public void deleteObject(String userEmail, String objectId) {
        Session session = new Session();
        session.setUserEmail(userEmail);
        session.setObjectId(objectId);
        mapper.delete(session);
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
        DynamoDBQueryExpression<Session> queryExpression = new DynamoDBQueryExpression<Session>()
                .withIndexName("SessionsSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and objectName = :objectName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Session.class, queryExpression).isEmpty();
    }
}

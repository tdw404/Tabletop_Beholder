package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Stores, reads, updates, or deletes Encounter models in DynamoDB using {@link Encounter}.
 */

@Singleton
public class EncounterDao implements BeholderDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a EncounterDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public EncounterDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Retrieves a encounter by objectId and userEmail.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Encounter} if found, or null if none found
     */

    @Override
    public Encounter getSingle(String userEmail, String objectId) {
        return mapper.load(Encounter.class, userEmail, objectId, ConsistentReads.CONSISTENT.config());
    }

    /**
     * Retrieves all encounters in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Encounter}, or an empty list if none found
     */

    @Override
    public List<? extends BeholderObject> getMultiple(String userEmail) {
        Encounter encounter = new Encounter();
        encounter.setUserEmail(userEmail);
        DynamoDBQueryExpression<Encounter> queryExpression = new DynamoDBQueryExpression<Encounter>()
                .withHashKeyValues(encounter);
        return mapper.query(Encounter.class, queryExpression);
    }

    /**
     * Updates an existing encounter only if its stored version matches the snapshot.
     *
     * @param beholderObject The {@link Encounter} to be saved
     */

    @Override
    public void writeObject(BeholderObject beholderObject) {
        Encounter encounter = (Encounter) beholderObject;
        Long expectedVersion = encounter.getVersion();
        ExpectedAttributeValue versionCondition = expectedVersion == null
                ? new ExpectedAttributeValue().withExists(false)
                : new ExpectedAttributeValue(new AttributeValue().withN(expectedVersion.toString()));
        DynamoDBSaveExpression condition = new DynamoDBSaveExpression()
                .withExpectedEntry("objectId", new ExpectedAttributeValue(new AttributeValue(encounter.getObjectId())))
                .withExpectedEntry("version", versionCondition);

        encounter.setVersion(expectedVersion == null ? 1L : expectedVersion + 1);
        try {
            mapper.save(encounter, condition);
        } catch (RuntimeException exception) {
            encounter.setVersion(expectedVersion);
            throw exception;
        }
    }

    @Override
    public void createIfAbsent(BeholderObject beholderObject) {
        Encounter encounter = (Encounter) beholderObject;
        encounter.setVersion(1L);
        mapper.save(encounter, BeholderDao.requireNewObjectId(), SaveBehavior.PUT.config());
    }

    /**
     * Removes a DynamoDB Encounter record matching provided userEmail and objectId.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     */

    @Override
    public void deleteObject(String userEmail, String objectId) {
        Encounter encounter = new Encounter();
        encounter.setUserEmail(userEmail);
        encounter.setObjectId(objectId);
        mapper.delete(encounter);
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
        valueMap.put(":objectName", new AttributeValue(objectName));
        DynamoDBQueryExpression<Encounter> queryExpression = new DynamoDBQueryExpression<Encounter>()
                .withIndexName("EncountersSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and objectName = :objectName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Encounter.class, queryExpression).isEmpty();
    }

    /**
     * Searches for all encounters belonging to a session.
     *
     * @param userEmail The userEmail to search
     * @param sessionId The session to search
     * @return List of encounters.
     */
    public List<Encounter> getEncounterBySession(String userEmail, String sessionId) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userEmail", new AttributeValue(userEmail));
        valueMap.put(":sessionId", new AttributeValue(sessionId));
        DynamoDBQueryExpression<Encounter> queryExpression = new DynamoDBQueryExpression<Encounter>()
                .withIndexName("EncountersSortBySessionIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and sessionId = :sessionId")
                .withExpressionAttributeValues(valueMap);
        return mapper.query(Encounter.class, queryExpression);
    }
}

package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Stores, reads, updates, or deletes Creature models in DynamoDB using {@link Creature}.
 */

@Singleton
public class CreatureDao implements BeholderDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a CreatureDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public CreatureDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Retrieves a creature by objectId and userEmail.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Creature} if found, or null if none found
     */

    @Override
    public Creature getSingle(String userEmail, String objectId) {
        return mapper.load(Creature.class, userEmail, objectId);
    }

    /**
     * Retrieves all creatures in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Creature}, or an empty list if none found
     */

    @Override
    public List<? extends BeholderObject> getMultiple(String userEmail) {
        Creature creature = new Creature();
        creature.setUserEmail(userEmail);
        DynamoDBQueryExpression<Creature> queryExpression = new DynamoDBQueryExpression<Creature>()
                .withHashKeyValues(creature);
        return mapper.query(Creature.class, queryExpression);
    }

    /**
     * Updates a DynamoDB Creature record with provided {@link Creature} object, or creates a new record if one does not exist.
     *
     * @param beholderObject The {@link Creature} to be saved
     */

    @Override
    public void writeObject(BeholderObject beholderObject) {
        mapper.save((Creature) beholderObject);
    }

    /**
     * Removes a DynamoDB Creature record matching provided userEmail and objectId.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     */

    @Override
    public void deleteObject(String userEmail, String objectId) {
        Creature creature = new Creature();
        creature.setUserEmail(userEmail);
        creature.setObjectId(objectId);
        mapper.delete(creature);
    }

    /**
     * Searches to determine whether a name exists already.
     *
     * @param userEmail The userEmail to search
     * @param objectName The objectName to search
     * @return Boolean, whether object exists.
     */
    @Override
    public Boolean objectNameExists(String userEmail, String objectName) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userEmail", new AttributeValue(userEmail));
        valueMap.put(":objectName", new AttributeValue(objectName));
        DynamoDBQueryExpression<Creature> queryExpression = new DynamoDBQueryExpression<Creature>()
                .withIndexName("CreaturesSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and objectName = :objectName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Creature.class, queryExpression).isEmpty();
    }
}

package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Stores, reads, updates, or deletes Spell models in DynamoDB using {@link Spell}.
 */

@Singleton
public class SpellDao implements BeholderDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a SpellDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public SpellDao(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Retrieves a spell by objectId and userEmail.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Spell} if found, or null if none found
     */

    @Override
    public Spell getSingle(String userEmail, String objectId) {
        return mapper.load(Spell.class, userEmail, objectId);
    }

    /**
     * Retrieves all spells in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Spell}, or an empty list if none found
     */

    @Override
    public List<? extends BeholderObject> getMultiple(String userEmail) {
        Spell spell = new Spell();
        spell.setUserEmail(userEmail);
        DynamoDBQueryExpression<Spell> queryExpression = new DynamoDBQueryExpression<Spell>()
                .withHashKeyValues(spell);
        return mapper.query(Spell.class, queryExpression);
    }

    /**
     * Updates a DynamoDB Spell record with provided {@link Spell} object, or creates a new record if one does not exist.
     *
     * @param beholderObject The {@link Spell} to be saved
     */

    @Override
    public void writeObject(BeholderObject beholderObject) {
        mapper.save((Spell) beholderObject);
    }

    /**
     * Removes a DynamoDB Spell record matching provided userEmail and objectId.
     *
     * @param objectId The objectId to search
     * @param userEmail The userEmail to search
     */

    @Override
    public void deleteObject(String userEmail, String objectId) {
        Spell spell = new Spell();
        spell.setUserEmail(userEmail);
        spell.setObjectId(objectId);
        mapper.delete(spell);
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
        DynamoDBQueryExpression<Spell> queryExpression = new DynamoDBQueryExpression<Spell>()
                .withIndexName("SpellsSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and objectName = :objectName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Spell.class, queryExpression).isEmpty();
    }

    /**
     * Retrieves a spell by name, if it exists.
     *
     * @param userEmail The userEmail to search
     * @param objectName The objectName to search
     * @return Spell.
     */
    public Spell getSpellByName(String userEmail, String objectName) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userEmail", new AttributeValue(userEmail));
        valueMap.put(":objectName", new AttributeValue(objectName));
        DynamoDBQueryExpression<Spell> queryExpression = new DynamoDBQueryExpression<Spell>()
                .withIndexName("SpellsSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and objectName = :objectName")
                .withExpressionAttributeValues(valueMap);
        PaginatedQueryList<Spell> spellPaginatedQueryList = mapper.query(Spell.class, queryExpression);
        if (spellPaginatedQueryList.isEmpty()) {
            return null;
        } else {
            return spellPaginatedQueryList.get(0);
        }
    }
}

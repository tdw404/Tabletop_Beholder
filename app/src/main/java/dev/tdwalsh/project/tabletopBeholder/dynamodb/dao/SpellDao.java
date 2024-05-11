package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores, reads, updates, or deletes Spell models in DynamoDB using {@link Spell}.
 */

@Singleton
public class SpellDao {
    private final DynamoDBMapper mapper;

    /**
     * Instantiates a SpellDao object.
     *
     * @param mapper the {@link DynamoDBMapper} used to interact with the Organizations table
     */

    @Inject
    public SpellDao(DynamoDBMapper mapper) { this.mapper = mapper; }

    /**
     * Retrieves a spell by spellId and userEmail.
     *
     * @param spellId The spellId to search
     * @param userEmail The userEmail to search
     * @return A single {@link Spell} if found, or null if none found
     */

    public Spell getSingleSpell(String userEmail, String spellId) {
        return mapper.load(Spell.class, userEmail, spellId);
    }

    /**
     * Retrieves all spells in database belonging to the provided userEmail.
     *
     * @param userEmail The userEmail to search by
     * @return A list of {@link Spell}, or an empty list if none found
     */

    public List<Spell> getSpellsByUser(String userEmail) {
        Spell spell = new Spell();
        spell.setUserEmail(userEmail);
        DynamoDBQueryExpression<Spell> queryExpression = new DynamoDBQueryExpression<Spell>()
                .withHashKeyValues(spell);
        return mapper.query(Spell.class, queryExpression);
    }

    /**
     * Updates a DynamoDB Spell record with provided {@link Spell} object, or creates a new record if one does not exist.
     *
     * @param spell The {@link Spell} to be saved
     */

    public void writeSpell(Spell spell) {
        mapper.save(spell);
    }

    /**
     * Removes a DynamoDB Spell record matching provided userEmail and spellId.
     *
     * @param spellId The spellId to search
     * @param userEmail The userEmail to search
     */

    public void deleteSpell(String userEmail, String spellId) {
        Spell spell = new Spell();
        spell.setUserEmail(userEmail);
        spell.setSpellId(spellId);
        mapper.delete(spell);
    }

    /**
     * Searches to determine whether a name exists already.
     *
     * @param userEmail The userEmail to search
     * @param spellName The userEmail to search
     */
    public Boolean spellNameExists(String userEmail, String spellName) {
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":userEmail", new AttributeValue(userEmail));
        valueMap.put(":spellName", new AttributeValue(spellName));
        DynamoDBQueryExpression<Spell> queryExpression = new DynamoDBQueryExpression<Spell>()
                .withIndexName("SpellsSortByNameIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("userEmail = :userEmail and spellName = :spellName")
                .withExpressionAttributeValues(valueMap);
        return !mapper.query(Spell.class, queryExpression).isEmpty();
    }
}

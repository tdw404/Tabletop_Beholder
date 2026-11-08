package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpellDao extends DynamoDbDao<Spell> {
    @Inject
    public SpellDao(DynamoDbEnhancedClient client) {
        super(client, Spell.TABLE_NAME, Spell.class, "SpellsSortByNameIndex");
    }

    public Spell getSpellByName(String userEmail, String objectName) {
        return findByName(userEmail, objectName);
    }
}

package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreatureDao extends DynamoDbDao<Creature> {
    @Inject
    public CreatureDao(DynamoDbEnhancedClient client) {
        super(client, Creature.TABLE_NAME, Creature.class, "CreaturesSortByNameIndex");
    }
}

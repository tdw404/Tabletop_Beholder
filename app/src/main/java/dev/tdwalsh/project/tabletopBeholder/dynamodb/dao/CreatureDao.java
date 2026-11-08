package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreatureDao extends DynamoDbDao<Creature> {
    @Inject
    public CreatureDao(DynamoDbEnhancedClient client) {
        super(client, Creature.TABLE_NAME, Creature.class, "CreaturesSortByNameIndex");
    }

    public Set<String> getSavedNames(String userEmail) {
        Key owner = Key.builder().partitionValue(userEmail).build();
        return PageIterable.create(table.index("CreaturesSortByNameIndex").query(request -> request
                .queryConditional(QueryConditional.keyEqualTo(owner))
                .attributesToProject("objectName")))
                .items().stream()
                .map(Creature::getObjectName)
                .collect(Collectors.toSet());
    }
}

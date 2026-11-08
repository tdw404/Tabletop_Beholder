package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionDao extends DynamoDbDao<Session> {
    @Inject
    public SessionDao(DynamoDbEnhancedClient client) {
        super(client, Session.TABLE_NAME, Session.class, "SessionsSortByNameIndex");
    }
}

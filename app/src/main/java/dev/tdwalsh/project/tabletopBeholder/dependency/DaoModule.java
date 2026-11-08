package dev.tdwalsh.project.tabletopBeholder.dependency;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.DynamoDbClientProvider;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Dagger Module providing dependencies for DAO classes.
 */
@Module
public class DaoModule {
    /**
     * Provides a DynamoDbEnhancedClient singleton instance.
     *
     * @return DynamoDbEnhancedClient object
     */
    @Singleton
    @Provides
    public DynamoDbEnhancedClient provideDynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(DynamoDbClientProvider.getDynamoDBClient())
                .build();
    }
}

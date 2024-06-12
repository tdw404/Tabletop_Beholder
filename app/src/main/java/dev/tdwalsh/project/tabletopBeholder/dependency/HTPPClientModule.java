package dev.tdwalsh.project.tabletopBeholder.dependency;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.DynamoDbClientProvider;
import dev.tdwalsh.project.tabletopBeholder.templateApi.Open5EClient;

import javax.inject.Singleton;

/**
 * Dagger Module providing dependencies for Template DAO classes.
 */
@Module
public class HTPPClientModule {
    /**
     * Provides a DynamoDBMapper singleton instance.
     *
     * @return DynamoDBMapper object
     */
    @Singleton
    @Provides
    public Open5EClient provideOpen5EClient() {
        return new Open5EClient();
    }
}

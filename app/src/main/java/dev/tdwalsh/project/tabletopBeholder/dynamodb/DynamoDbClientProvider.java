package dev.tdwalsh.project.tabletopBeholder.dynamodb;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.util.Set;

public class DynamoDbClientProvider {
    public static DynamoDbClient getDynamoDBClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder()
                .httpClientBuilder(UrlConnectionHttpClient.builder());

        String localEndpoint = System.getenv("DYNAMODB_ENDPOINT");
        if (localEndpoint != null && !localEndpoint.isBlank()) {
            URI endpoint = URI.create(localEndpoint);
            if (!"http".equals(endpoint.getScheme()) || endpoint.getHost() == null
                    || !Set.of("localhost", "127.0.0.1", "[::1]").contains(endpoint.getHost())) {
                throw new IllegalArgumentException("DYNAMODB_ENDPOINT must use HTTP on loopback.");
            }
            builder.endpointOverride(endpoint)
                    .region(Region.US_EAST_2)
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("local", "local")));
        }

        return builder.build();
    }
}

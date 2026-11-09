package dev.tdwalsh.project.tabletopBeholder.lambda;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.DynamoDbClientProvider;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class WarmupLambda implements RequestHandler<AuthenticatedLambdaRequest<Void>, LambdaResponse> {
    private static final long COOLDOWN_SECONDS = 300;
    private static final long EVENT_LIFETIME_SECONDS = 60;

    private DynamoDbClient database;
    private LambdaClient lambda;

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<Void> event, Context context) {
        try {
            event.fromOwnerId(owner -> null);
            String targets = System.getenv("WARMUP_TARGETS");
            if (targets == null || targets.isBlank()) {
                return LambdaResponse.successNoContent();
            }

            long now = Instant.now().getEpochSecond();
            if (!claimRound(now)) {
                context.getLogger().log("Warm-up round skipped: cooldown");
                return LambdaResponse.successNoContent();
            }

            if (lambda == null) {
                lambda = LambdaClient.builder()
                        .httpClientBuilder(UrlConnectionHttpClient.builder())
                        .overrideConfiguration(config -> config.apiCallTimeout(Duration.ofSeconds(2)))
                        .build();
            }
            SdkBytes payload = SdkBytes.fromUtf8String("{\"warmupUntil\":" + (now + EVENT_LIFETIME_SECONDS) + "}");
            int accepted = 0;
            int failed = 0;
            for (String target : targets.split(",")) {
                if (context.getRemainingTimeInMillis() < 2500) {
                    break;
                }
                try {
                    lambda.invoke(request -> request.functionName(target)
                            .invocationType(InvocationType.EVENT).payload(payload));
                    accepted++;
                } catch (SdkException exception) {
                    failed++;
                    context.getLogger().log("Warm-up enqueue failed for " + target + ": "
                            + exception.getClass().getSimpleName());
                }
            }
            context.getLogger().log("Warm-up round: accepted=" + accepted + ", failed=" + failed
                    + ", skipped=" + (targets.split(",").length - accepted - failed));
            return LambdaResponse.successNoContent();
        } catch (Exception exception) {
            return LambdaResponse.error(exception);
        }
    }

    private boolean claimRound(long now) {
        if (database == null) {
            database = DynamoDbClientProvider.getDynamoDBClient();
        }
        try {
            database.updateItem(request -> request.tableName(System.getenv("WARMUP_TABLE"))
                    .key(Map.of("id", AttributeValue.fromS("api")))
                    .conditionExpression("attribute_not_exists(nextRoundAt) OR nextRoundAt <= :now")
                    .updateExpression("SET nextRoundAt = :next")
                    .expressionAttributeValues(Map.of(
                            ":now", AttributeValue.fromN(Long.toString(now)),
                            ":next", AttributeValue.fromN(Long.toString(now + COOLDOWN_SECONDS)))));
            return true;
        } catch (ConditionalCheckFailedException exception) {
            return false;
        }
    }
}

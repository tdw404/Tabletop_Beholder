package dev.tdwalsh.project.tabletopBeholder.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LambdaActivityRunnerTest {
    private final LambdaActivityRunner<String, String> runner = new LambdaActivityRunner<>();
    private final AtomicInteger initializations = new AtomicInteger();

    @Test
    void internalWarmupInitializesDependenciesWithoutRunningTheRequest() throws Exception {
        long until = Instant.now().plusSeconds(60).getEpochSecond();
        LambdaRequest<?> event = new ObjectMapper().readValue(
                "{\"warmupUntil\":" + until + "}", AuthenticatedLambdaRequest.class);

        assertEquals(204, warm(event).getStatusCode());
        assertEquals(1, initializations.get());
    }

    @Test
    void expiredWarmupDoesNotInitializeDependencies() {
        LambdaRequest<?> event = new LambdaRequest<>();
        event.setWarmupUntil(1L);

        assertEquals(204, warm(event).getStatusCode());
        assertEquals(0, initializations.get());
    }

    @Test
    void apiRequestWithWarmupMarkerStillRunsNormalHandler() {
        LambdaRequest<?> event = new LambdaRequest<>();
        event.setHttpMethod("POST");
        event.setWarmupUntil(Instant.now().plusSeconds(60).getEpochSecond());

        LambdaResponse result = runner.runActivity(event, () -> "normal", service -> "activity",
                (activity, request) -> request);

        assertEquals(200, result.getStatusCode());
        assertEquals("\"normal\"", result.getBody());
    }

    private LambdaResponse warm(LambdaRequest<?> event) {
        return runner.runActivity(event, () -> {
            throw new AssertionError("Warm-up must not parse the request");
        }, service -> initializations.incrementAndGet(), (activity, request) -> {
            throw new AssertionError("Warm-up must not run the activity");
        });
    }
}

package dev.tdwalsh.project.tabletopBeholder.lambda;

import dev.tdwalsh.project.tabletopBeholder.dependency.DaggerServiceComponent;
import dev.tdwalsh.project.tabletopBeholder.dependency.ServiceComponent;

import java.time.Instant;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.function.Supplier;

public class LambdaActivityRunner<TRequest, TResult> {
    private ServiceComponent service;

    /**
     * Handles running the activity and returning a LambdaResponse (either success or failure).
     *
     * @param event the incoming Lambda event
     * @param activityProvider creates the activity and its dependencies
     * @param <A> the activity type
     * @param requestSupplier Provides the activity request.
     * @param handleRequest   Runs the activity and provides a response.
     * @return A LambdaResponse
     */
    protected <A> LambdaResponse runActivity(
            LambdaRequest<?> event,
            Supplier<TRequest> requestSupplier,
            Function<ServiceComponent, A> activityProvider,
            BiFunction<A, TRequest, TResult> handleRequest) {

        if (event != null && event.isInternalWarmup()) {
            if (Instant.now().getEpochSecond() <= event.getWarmupUntil()) {
                activityProvider.apply(getService());
                Logger.getLogger(getClass().getName()).info("Warm-up initialized");
            }
            return LambdaResponse.successNoContent();
        }

        TRequest request;
        try {

            request = requestSupplier.get();

        } catch (Exception e) {
            return LambdaResponse.error(e);
        }

        try {

            A activity = activityProvider.apply(getService());
            TResult result = handleRequest.apply(activity, request);
            if (result == null) {
                return LambdaResponse.successNoContent();
            }
            return LambdaResponse.success(result);
        } catch (Exception e) {
            return LambdaResponse.error(e);
        }
    }

    protected final ServiceComponent getService() {
        if (service == null) {
            service = DaggerServiceComponent.create();
        }
        return service;
    }
}

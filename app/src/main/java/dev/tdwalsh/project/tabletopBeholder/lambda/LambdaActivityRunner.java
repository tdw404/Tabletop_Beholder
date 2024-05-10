package dev.tdwalsh.project.tabletopBeholder.lambda;

import dev.tdwalsh.project.tabletopBeholder.dependency.DaggerServiceComponent;
import dev.tdwalsh.project.tabletopBeholder.dependency.ServiceComponent;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class LambdaActivityRunner <TRequest, TResult> {
    private ServiceComponent service;

    /**
     * Handles running the activity and returning a LambdaResponse (either success or failure).
     *
     * @param requestSupplier Provides the activity request.
     * @param handleRequest   Runs the activity and provides a response.
     * @return A LambdaResponse
     */
    protected LambdaResponse runActivity(
            Supplier<TRequest> requestSupplier,
            BiFunction<TRequest, ServiceComponent, TResult> handleRequest) {

        TRequest request;
        try {

            request = requestSupplier.get();

        } catch (Exception e) {
            return LambdaResponse.error(e);
        }

        try {

            ServiceComponent serviceComponent = getService();
            TResult result = handleRequest.apply(request, serviceComponent);

            return LambdaResponse.success(result);
        } catch (Exception e) {
            return LambdaResponse.error(e);
        }
    }

    private ServiceComponent getService() {
        if (service == null) {
            service = DaggerServiceComponent.create();
        }
        return service;
    }
}

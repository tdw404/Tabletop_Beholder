package dev.tdwalsh.project.tabletopBeholder.lambda.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.DeleteSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.DeleteSessionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DeleteSessionLambda
        extends LambdaActivityRunner<DeleteSessionRequest, DeleteSessionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteSessionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteSessionRequest> input, Context context) {
        return super.runActivity(
            input,
            () -> {
                DeleteSessionRequest stageRequest = input.fromOwnerId(ownerId ->
                        DeleteSessionRequest.builder()
                                .withUserEmail(ownerId)
                                .build());

                return input.fromPath(path ->
                        DeleteSessionRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withObjectId(path.get("objectId"))
                                .build());
            },
            serviceComponent -> serviceComponent.provideDeleteSessionActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

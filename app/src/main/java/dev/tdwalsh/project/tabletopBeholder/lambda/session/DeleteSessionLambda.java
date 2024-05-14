package dev.tdwalsh.project.tabletopBeholder.lambda.session;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.DeleteSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.DeleteSessionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class DeleteSessionLambda
        extends LambdaActivityRunner<DeleteSessionRequest, DeleteSessionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteSessionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteSessionRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    DeleteSessionRequest stageRequest = input.fromUserClaims(claims ->
                            DeleteSessionRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .build());

                    return input.fromPath(path ->
                            DeleteSessionRequest.builder()
                                    .withUserEmail(stageRequest.getUserEmail())
                                    .withObjectId(path.get("objectId"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteSessionActivity().handleRequest(request)
        );
    }
}
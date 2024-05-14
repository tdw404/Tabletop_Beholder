package dev.tdwalsh.project.tabletopBeholder.lambda.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.DeleteActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.DeleteActionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class DeleteActionLambda
        extends LambdaActivityRunner<DeleteActionRequest, DeleteActionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteActionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteActionRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    DeleteActionRequest stageRequest = input.fromUserClaims(claims ->
                            DeleteActionRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .build());

                    return input.fromPath(path ->
                            DeleteActionRequest.builder()
                                    .withUserEmail(stageRequest.getUserEmail())
                                    .withObjectId(path.get("objectId"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteActionActivity().handleRequest(request)
        );
    }
}
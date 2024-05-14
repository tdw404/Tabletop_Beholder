package dev.tdwalsh.project.tabletopBeholder.lambda.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.UpdateActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.UpdateActionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class UpdateActionLambda
        extends LambdaActivityRunner<UpdateActionRequest, UpdateActionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateActionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateActionRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    UpdateActionRequest stageRequest = input.fromBody(UpdateActionRequest.class);

                    return input.fromUserClaims(claims ->
                            UpdateActionRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withAction(stageRequest.getAction())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateActionActivity().handleRequest(request)
        );
    }
}
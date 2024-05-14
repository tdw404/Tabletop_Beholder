package dev.tdwalsh.project.tabletopBeholder.lambda.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.GetActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.GetActionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetActionLambda
        extends LambdaActivityRunner<GetActionRequest, GetActionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetActionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetActionRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    GetActionRequest stageRequest = input.fromUserClaims(claims ->
                    GetActionRequest.builder()
                        .withUserEmail(claims.get("email"))
                        .build());

                    return input.fromPath(path ->
                        GetActionRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withObjectId(path.get("objectId"))
                                .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideGetActionActivity().handleRequest(request)
        );
    }
}
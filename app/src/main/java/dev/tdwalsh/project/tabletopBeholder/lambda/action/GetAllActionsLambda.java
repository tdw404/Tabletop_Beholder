package dev.tdwalsh.project.tabletopBeholder.lambda.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.GetAllActionsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.GetAllActionsResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetAllActionsLambda
        extends LambdaActivityRunner<GetAllActionsRequest, GetAllActionsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllActionsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllActionsRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims ->
                        GetAllActionsRequest.builder()
                                .withUserEmail(claims.get("userEmail"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllActionsActivity().handleRequest(request)
        );
    }
}
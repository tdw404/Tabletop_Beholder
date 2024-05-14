package dev.tdwalsh.project.tabletopBeholder.lambda.action;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.action.request.CreateActionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.action.result.CreateActionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class CreateActionLambda
        extends LambdaActivityRunner<CreateActionRequest, CreateActionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateActionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateActionRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    CreateActionRequest stageRequest = input.fromBody(CreateActionRequest.class);

                    return input.fromUserClaims(claims ->
                            CreateActionRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withAction(stageRequest.getAction())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateActionActivity().handleRequest(request)
        );
    }
}
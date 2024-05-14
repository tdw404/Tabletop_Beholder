package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.CreateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.CreateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class CreateCreatureLambda
        extends LambdaActivityRunner<CreateCreatureRequest, CreateCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateCreatureRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    CreateCreatureRequest stageRequest = input.fromBody(CreateCreatureRequest.class);

                    return input.fromUserClaims(claims ->
                            CreateCreatureRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withCreature(stageRequest.getCreature())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateCreatureActivity().handleRequest(request)
        );
    }
}
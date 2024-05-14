package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.UpdateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.UpdateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class UpdateCreatureLambda
        extends LambdaActivityRunner<UpdateCreatureRequest, UpdateCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateCreatureRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    UpdateCreatureRequest stageRequest = input.fromBody(UpdateCreatureRequest.class);

                    return input.fromUserClaims(claims ->
                            UpdateCreatureRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withCreature(stageRequest.getCreature())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateCreatureActivity().handleRequest(request)
        );
    }
}
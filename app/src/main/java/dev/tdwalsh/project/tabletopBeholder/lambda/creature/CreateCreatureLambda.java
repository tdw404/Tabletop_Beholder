package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.CreateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.CreateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateCreatureLambda
        extends LambdaActivityRunner<CreateCreatureRequest, CreateCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateCreatureRequest> input, Context context) {
        return super.runActivity(
            input,
            () -> {
                CreateCreatureRequest stageRequest = input.fromBody(CreateCreatureRequest.class);

                return input.fromOwnerId(ownerId ->
                        CreateCreatureRequest.builder()
                                .withUserEmail(ownerId)
                                .withCreature(stageRequest.getCreature())
                                .build());
            },
            serviceComponent -> serviceComponent.provideCreateCreatureActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

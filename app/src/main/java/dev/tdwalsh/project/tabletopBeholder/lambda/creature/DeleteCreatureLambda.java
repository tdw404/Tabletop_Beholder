package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.DeleteCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.DeleteCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DeleteCreatureLambda
        extends LambdaActivityRunner<DeleteCreatureRequest, DeleteCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteCreatureRequest> input, Context context) {
        return super.runActivity(
            () -> {
                DeleteCreatureRequest stageRequest = input.fromUserClaims(claims ->
                        DeleteCreatureRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        DeleteCreatureRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withObjectId(path.get("objectId"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideDeleteCreatureActivity().handleRequest(request)
        );
    }
}

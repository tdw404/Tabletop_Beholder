package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetCreatureLambda
        extends LambdaActivityRunner<GetCreatureRequest, GetCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetCreatureRequest> input, Context context) {
        return super.runActivity(
            () -> {
                GetCreatureRequest stageRequest = input.fromUserClaims(claims ->
                        GetCreatureRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                    GetCreatureRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withObjectId(path.get("objectId"))
                            .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetCreatureActivity().handleRequest(request)
        );
    }
}

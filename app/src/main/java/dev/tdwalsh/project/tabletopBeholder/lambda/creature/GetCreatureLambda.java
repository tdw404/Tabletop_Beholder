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
            input,
            () -> {
                GetCreatureRequest stageRequest = input.fromOwnerId(ownerId ->
                        GetCreatureRequest.builder()
                                .withUserEmail(ownerId)
                                .build());

                return input.fromPath(path ->
                    GetCreatureRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withObjectId(path.get("objectId"))
                            .build());
            },
            serviceComponent -> serviceComponent.provideGetCreatureActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

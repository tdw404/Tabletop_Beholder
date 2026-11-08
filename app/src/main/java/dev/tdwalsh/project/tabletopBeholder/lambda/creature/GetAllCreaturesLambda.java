package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.GetAllCreaturesActivity;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetAllCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetAllCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAllCreaturesLambda
        extends LambdaActivityRunner<GetAllCreaturesRequest, GetAllCreaturesResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllCreaturesRequest>, LambdaResponse> {

    // Initialize the DynamoDB client and table mapping before SnapStart takes the snapshot.
    private final GetAllCreaturesActivity activity = getService().provideGetAllCreaturesActivity();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllCreaturesRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromOwnerId(ownerId ->
                    GetAllCreaturesRequest.builder()
                            .withUserEmail(ownerId)
                            .build()),
            (request, serviceComponent) -> activity.handleRequest(request)
        );
    }
}

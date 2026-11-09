package dev.tdwalsh.project.tabletopBeholder.lambda.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.DeleteEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.DeleteEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DeleteEncounterLambda
        extends LambdaActivityRunner<DeleteEncounterRequest, DeleteEncounterResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteEncounterRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteEncounterRequest> input, Context context) {
        return super.runActivity(
            input,
            () -> {
                DeleteEncounterRequest stageRequest = input.fromOwnerId(ownerId ->
                        DeleteEncounterRequest.builder()
                                .withUserEmail(ownerId)
                                .build());

                return input.fromPath(path ->
                        DeleteEncounterRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withObjectId(path.get("objectId"))
                                .build());
            },
            serviceComponent -> serviceComponent.provideDeleteEncounterActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

package dev.tdwalsh.project.tabletopBeholder.lambda.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.UpdateEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.UpdateEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateEncounterLambda
        extends LambdaActivityRunner<UpdateEncounterRequest, UpdateEncounterResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateEncounterRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateEncounterRequest> input, Context context) {
        return super.runActivity(
            input,
            () -> {
                UpdateEncounterRequest stageRequest = input.fromBody(UpdateEncounterRequest.class);

                return input.fromOwnerId(ownerId ->
                        UpdateEncounterRequest.builder()
                                .withUserEmail(ownerId)
                                .withEncounter(stageRequest.getEncounter())
                                .build());
            },
            serviceComponent -> serviceComponent.provideUpdateEncounterActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

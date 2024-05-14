package dev.tdwalsh.project.tabletopBeholder.lambda.encounter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.DeleteEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.DeleteEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class DeleteEncounterLambda
        extends LambdaActivityRunner<DeleteEncounterRequest, DeleteEncounterResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteEncounterRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteEncounterRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    DeleteEncounterRequest stageRequest = input.fromUserClaims(claims ->
                            DeleteEncounterRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .build());

                    return input.fromPath(path ->
                            DeleteEncounterRequest.builder()
                                    .withUserEmail(stageRequest.getUserEmail())
                                    .withObjectId(path.get("objectId"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteEncounterActivity().handleRequest(request)
        );
    }
}
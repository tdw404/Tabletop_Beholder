package dev.tdwalsh.project.tabletopBeholder.lambda.encounter;

import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.GetEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.GetEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetEncounterLambda
        extends LambdaActivityRunner<GetEncounterRequest, GetEncounterResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetEncounterRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetEncounterRequest> input, Context context) {
        return super.runActivity(
            () -> {
                GetEncounterRequest stageRequest = input.fromUserClaims(claims ->
                        GetEncounterRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                    GetEncounterRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withObjectId(path.get("objectId"))
                            .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetEncounterActivity().handleRequest(request)
        );
    }
}

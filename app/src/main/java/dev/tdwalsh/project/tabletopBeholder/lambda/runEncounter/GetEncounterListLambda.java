package dev.tdwalsh.project.tabletopBeholder.lambda.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.GetEncounterListRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.GetEncounterListResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetEncounterListLambda
        extends LambdaActivityRunner<GetEncounterListRequest, GetEncounterListResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetEncounterListRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetEncounterListRequest> input, Context context) {
        return super.runActivity(
            () -> {
                GetEncounterListRequest stageRequest = input.fromUserClaims(claims ->
                        GetEncounterListRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        GetEncounterListRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withSessionId(path.get("sessionId"))
                            .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetEncounterListActivity().handleRequest(request)
        );
    }
}

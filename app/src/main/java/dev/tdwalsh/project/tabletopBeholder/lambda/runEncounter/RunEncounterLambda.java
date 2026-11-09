package dev.tdwalsh.project.tabletopBeholder.lambda.runEncounter;

import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request.RunEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result.RunEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.json.JSONObject;

import java.util.Map;

public class RunEncounterLambda
        extends LambdaActivityRunner<RunEncounterRequest, RunEncounterResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RunEncounterRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RunEncounterRequest> input, Context context) {
        return super.runActivity(
            input,
            () -> {
                RunEncounterRequest claimsRequest = input.fromOwnerId(ownerId ->
                        RunEncounterRequest.builder()
                                .withUserEmail(ownerId)
                                .build());
                RunEncounterRequest queryRequest = input.fromQuery(query ->
                        RunEncounterRequest.builder()
                                .withActivity(query.get("activity"))
                                .build());
                return input.fromPath(path ->
                        RunEncounterRequest.builder()
                            .withUserEmail(claimsRequest.getUserEmail())
                            .withEncounterId(path.get("encounterId"))
                            .withActivity(queryRequest.getActivity())
                            .withBody(new JSONObject(input.readJsonBody(Map.class)))
                            .build());
            },
            serviceComponent -> serviceComponent.provideRunEncounterActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

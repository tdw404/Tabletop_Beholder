package dev.tdwalsh.project.tabletopBeholder.lambda.encounter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.CreateEncounterRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.CreateEncounterResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class CreateEncounterLambda
        extends LambdaActivityRunner<CreateEncounterRequest, CreateEncounterResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateEncounterRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateEncounterRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    CreateEncounterRequest stageRequest = input.fromBody(CreateEncounterRequest.class);

                    return input.fromUserClaims(claims ->
                            CreateEncounterRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withEncounter(stageRequest.getEncounter())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateEncounterActivity().handleRequest(request)
        );
    }
}
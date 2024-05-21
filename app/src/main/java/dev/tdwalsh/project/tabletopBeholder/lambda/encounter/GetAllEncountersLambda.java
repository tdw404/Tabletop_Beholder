package dev.tdwalsh.project.tabletopBeholder.lambda.encounter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.request.GetAllEncountersRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.encounter.result.GetAllEncountersResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetAllEncountersLambda
        extends LambdaActivityRunner<GetAllEncountersRequest, GetAllEncountersResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllEncountersRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllEncountersRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims ->
                        GetAllEncountersRequest.builder()
                                .withUserEmail(claims.get("userEmail"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllEncountersActivity().handleRequest(request)
        );
    }
}
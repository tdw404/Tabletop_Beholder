package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetAllSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetAllSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAllSpellsLambda
        extends LambdaActivityRunner<GetAllSpellsRequest, GetAllSpellsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllSpellsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllSpellsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromUserClaims(claims ->
                    GetAllSpellsRequest.builder()
                            .withUserEmail(claims.get("email"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetAllSpellsActivity().handleRequest(request)
        );
    }
}

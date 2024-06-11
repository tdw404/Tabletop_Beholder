package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.UpdateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateSpellLambda
        extends LambdaActivityRunner<UpdateSpellRequest, UpdateSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateSpellRequest> input, Context context) {
        return super.runActivity(
            () -> {
                UpdateSpellRequest stageRequest = input.fromBody(UpdateSpellRequest.class);

                return input.fromUserClaims(claims ->
                        UpdateSpellRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .withSpell(stageRequest.getSpell())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateSpellActivity().handleRequest(request)
        );
    }
}

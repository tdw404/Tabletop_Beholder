package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.CreateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateSpellLambda
        extends LambdaActivityRunner<CreateSpellRequest, CreateSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateSpellRequest> input, Context context) {
        return super.runActivity(
            () -> {
                CreateSpellRequest stageRequest = input.fromBody(CreateSpellRequest.class);

                return input.fromUserClaims(claims ->
                        CreateSpellRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .withSpell(stageRequest.getSpell())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateSpellActivity().handleRequest(request)
        );
    }
}

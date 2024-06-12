package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.DeleteSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.DeleteSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DeleteSpellLambda
        extends LambdaActivityRunner<DeleteSpellRequest, DeleteSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteSpellRequest> input, Context context) {
        return super.runActivity(
            () -> {
                DeleteSpellRequest stageRequest = input.fromUserClaims(claims ->
                        DeleteSpellRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        DeleteSpellRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withObjectId(path.get("objectId"))
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideDeleteSpellActivity().handleRequest(request)
        );
    }
}

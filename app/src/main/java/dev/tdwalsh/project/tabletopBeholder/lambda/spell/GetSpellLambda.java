package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetSpellLambda
        extends LambdaActivityRunner<GetSpellRequest, GetSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetSpellRequest> input, Context context) {
        return super.runActivity(
            () -> {
                GetSpellRequest stageRequest = input.fromUserClaims(claims ->
                        GetSpellRequest.builder()
                        .withUserEmail(claims.get("email"))
                        .build());

                return input.fromPath(path ->
                    GetSpellRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withObjectId(path.get("objectId"))
                            .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideGetSpellActivity().handleRequest(request)
        );
    }
}

package dev.tdwalsh.project.tabletopBeholder.lambda.templateSpell;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.GetTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.GetTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetTemplateSpellLambda
        extends LambdaActivityRunner<GetTemplateSpellRequest, GetTemplateSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetTemplateSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetTemplateSpellRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    GetTemplateSpellRequest stageRequest = input.fromUserClaims(claims ->
                    GetTemplateSpellRequest.builder()
                        .withUserEmail(claims.get("email"))
                        .build());

                    return input.fromPath(path ->
                        GetTemplateSpellRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withSlug(path.get("slug"))
                                .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideGetTemplateSpellActivity().handleRequest(request)
        );
    }
}
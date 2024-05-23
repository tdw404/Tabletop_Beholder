package dev.tdwalsh.project.tabletopBeholder.lambda.templateCreature;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.GetTemplateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.GetTemplateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.GetTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.GetTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetTemplateCreatureLambda
        extends LambdaActivityRunner<GetTemplateCreatureRequest, GetTemplateCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetTemplateCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetTemplateCreatureRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    GetTemplateCreatureRequest stageRequest = input.fromUserClaims(claims ->
                            GetTemplateCreatureRequest.builder()
                        .withUserEmail(claims.get("email"))
                        .build());

                    return input.fromPath(path ->
                            GetTemplateCreatureRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withSlug(path.get("slug"))
                                .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideGetTemplateCreatureActivity().handleRequest(request)
        );
    }
}
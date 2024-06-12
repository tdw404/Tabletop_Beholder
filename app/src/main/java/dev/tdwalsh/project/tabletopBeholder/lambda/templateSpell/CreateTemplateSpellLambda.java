package dev.tdwalsh.project.tabletopBeholder.lambda.templateSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.CreateTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.CreateTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateTemplateSpellLambda
        extends LambdaActivityRunner<CreateTemplateSpellRequest, CreateTemplateSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateTemplateSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateTemplateSpellRequest> input, Context context) {
        return super.runActivity(
            () -> {
                CreateTemplateSpellRequest stageRequest = input.fromPath(path ->
                        CreateTemplateSpellRequest.builder()
                                .withSlug(path.get("slug"))
                                .build());

                return input.fromUserClaims(claims ->
                        CreateTemplateSpellRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .withSlug(stageRequest.getSlug())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideCreateTemplateSpellActivity().handleRequest(request)
        );
    }
}

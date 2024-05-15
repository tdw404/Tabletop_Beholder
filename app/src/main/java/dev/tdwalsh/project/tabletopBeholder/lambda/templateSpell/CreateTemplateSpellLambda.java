package dev.tdwalsh.project.tabletopBeholder.lambda.templateSpell;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.CreateTemplateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.CreateTemplateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class CreateTemplateSpellLambda
        extends LambdaActivityRunner<CreateTemplateSpellRequest, CreateTemplateSpellResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateTemplateSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateTemplateSpellRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    System.out.println("One");
                    CreateTemplateSpellRequest stageRequest = input.fromPath(path ->
                            CreateTemplateSpellRequest.builder()
                                    .withSlug(path.get("slug"))
                                    .build());
                    System.out.println("two");
                    //CreateTemplateSpellRequest stageRequest = input.fromPath(CreateTemplateSpellRequest.class);

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
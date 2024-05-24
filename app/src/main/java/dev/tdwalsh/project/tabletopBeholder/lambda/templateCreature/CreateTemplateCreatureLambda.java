package dev.tdwalsh.project.tabletopBeholder.lambda.templateCreature;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.CreateTemplateCreatureRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.CreateTemplateCreatureResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class CreateTemplateCreatureLambda
        extends LambdaActivityRunner<CreateTemplateCreatureRequest, CreateTemplateCreatureResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateTemplateCreatureRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateTemplateCreatureRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    CreateTemplateCreatureRequest stageRequest = input.fromPath(path ->
                            CreateTemplateCreatureRequest.builder()
                                    .withSlug(path.get("slug"))
                                    .build());

                    return input.fromUserClaims(claims ->
                            CreateTemplateCreatureRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withSlug(stageRequest.getSlug())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateTemplateCreatureActivity().handleRequest(request)
        );
    }
}
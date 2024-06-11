package dev.tdwalsh.project.tabletopBeholder.lambda.templateCreature;

import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request.SearchTemplateCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result.SearchTemplateCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SearchTemplateCreaturesLambda
        extends LambdaActivityRunner<SearchTemplateCreaturesRequest, SearchTemplateCreaturesResult>
        implements RequestHandler<AuthenticatedLambdaRequest<SearchTemplateCreaturesRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<SearchTemplateCreaturesRequest> input, Context context) {
        return super.runActivity(
            () -> {
                SearchTemplateCreaturesRequest stageRequest = input.fromUserClaims(claims ->
                        SearchTemplateCreaturesRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        SearchTemplateCreaturesRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withSearchTerms(path.get("searchTerms"))
                            .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideSearchTemplateCreaturesActivity().handleRequest(request)
        );
    }
}

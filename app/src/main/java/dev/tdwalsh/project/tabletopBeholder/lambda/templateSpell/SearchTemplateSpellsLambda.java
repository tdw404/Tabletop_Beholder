package dev.tdwalsh.project.tabletopBeholder.lambda.templateSpell;

import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request.SearchTemplateSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result.SearchTemplateSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SearchTemplateSpellsLambda
        extends LambdaActivityRunner<SearchTemplateSpellsRequest, SearchTemplateSpellsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<SearchTemplateSpellsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<SearchTemplateSpellsRequest> input, Context context) {
        return super.runActivity(
            () -> {
                SearchTemplateSpellsRequest stageRequest = input.fromUserClaims(claims ->
                        SearchTemplateSpellsRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .build());

                return input.fromPath(path ->
                        SearchTemplateSpellsRequest.builder()
                            .withUserEmail(stageRequest.getUserEmail())
                            .withSearchTerms(path.get("searchTerms"))
                            .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideSearchTemplateSpellsActivity().handleRequest(request)
        );
    }
}

package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetAllCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetAllCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAllCreaturesLambda
        extends LambdaActivityRunner<GetAllCreaturesRequest, GetAllCreaturesResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllCreaturesRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllCreaturesRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromUserClaims(claims ->
                    GetAllCreaturesRequest.builder()
                            .withUserEmail(claims.get("email"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetAllCreaturesActivity().handleRequest(request)
        );
    }
}

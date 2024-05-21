package dev.tdwalsh.project.tabletopBeholder.lambda.creature;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.request.GetAllCreaturesRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.creature.result.GetAllCreaturesResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetAllCreaturesLambda
        extends LambdaActivityRunner<GetAllCreaturesRequest, GetAllCreaturesResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllCreaturesRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllCreaturesRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromUserClaims(claims ->
                        GetAllCreaturesRequest.builder()
                                .withUserEmail(claims.get("userEmail"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllCreaturesActivity().handleRequest(request)
        );
    }
}
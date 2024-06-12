package dev.tdwalsh.project.tabletopBeholder.lambda.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.GetAllSessionsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.GetAllSessionsResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAllSessionsLambda
        extends LambdaActivityRunner<GetAllSessionsRequest, GetAllSessionsResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetAllSessionsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetAllSessionsRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromUserClaims(claims ->
                    GetAllSessionsRequest.builder()
                            .withUserEmail(claims.get("email"))
                            .build()),
            (request, serviceComponent) ->
                    serviceComponent.provideGetAllSessionsActivity().handleRequest(request)
        );
    }
}

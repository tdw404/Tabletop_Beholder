package dev.tdwalsh.project.tabletopBeholder.lambda.session;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.GetSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.GetSessionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetSessionLambda
        extends LambdaActivityRunner<GetSessionRequest, GetSessionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetSessionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetSessionRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    GetSessionRequest stageRequest = input.fromUserClaims(claims ->
                    GetSessionRequest.builder()
                        .withUserEmail(claims.get("email"))
                        .build());

                    return input.fromPath(path ->
                        GetSessionRequest.builder()
                                .withUserEmail(stageRequest.getUserEmail())
                                .withObjectId(path.get("objectId"))
                                .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideGetSessionActivity().handleRequest(request)
        );
    }
}
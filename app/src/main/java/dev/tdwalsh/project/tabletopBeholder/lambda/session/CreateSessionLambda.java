package dev.tdwalsh.project.tabletopBeholder.lambda.session;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.session.request.CreateSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.CreateSessionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class CreateSessionLambda
        extends LambdaActivityRunner<CreateSessionRequest, CreateSessionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateSessionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateSessionRequest> input, Context context) {
        return super.runActivity(
                () ->
                {
                    CreateSessionRequest stageRequest = input.fromBody(CreateSessionRequest.class);

                    return input.fromUserClaims(claims ->
                            CreateSessionRequest.builder()
                                    .withUserEmail(claims.get("email"))
                                    .withSession(stageRequest.getSession())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateSessionActivity().handleRequest(request)
        );
    }
}
package dev.tdwalsh.project.tabletopBeholder.lambda.session;

import dev.tdwalsh.project.tabletopBeholder.activity.session.request.UpdateSessionRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.session.result.UpdateSessionResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.AuthenticatedLambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateSessionLambda
        extends LambdaActivityRunner<UpdateSessionRequest, UpdateSessionResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateSessionRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateSessionRequest> input, Context context) {
        return super.runActivity(
            () -> {
                UpdateSessionRequest stageRequest = input.fromBody(UpdateSessionRequest.class);

                return input.fromUserClaims(claims ->
                        UpdateSessionRequest.builder()
                                .withUserEmail(claims.get("email"))
                                .withSession(stageRequest.getSession())
                                .build());
            },
            (request, serviceComponent) ->
                    serviceComponent.provideUpdateSessionActivity().handleRequest(request)
        );
    }
}

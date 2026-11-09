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
            input,
            () -> {
                UpdateSessionRequest stageRequest = input.fromBody(UpdateSessionRequest.class);

                return input.fromOwnerId(ownerId ->
                        UpdateSessionRequest.builder()
                                .withUserEmail(ownerId)
                                .withSession(stageRequest.getSession())
                                .build());
            },
            serviceComponent -> serviceComponent.provideUpdateSessionActivity(),
            (activity, request) -> activity.handleRequest(request)
        );
    }
}

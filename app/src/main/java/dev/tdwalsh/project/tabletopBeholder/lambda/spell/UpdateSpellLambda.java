package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.UpdateSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class UpdateSpellLambda
        extends LambdaActivityRunner<UpdateSpellRequest, UpdateSpellResult>
        implements RequestHandler<LambdaRequest<UpdateSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<UpdateSpellRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromBody(UpdateSpellRequest.class),
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateSpellActivity().handleRequest(request)
        );
    }
}
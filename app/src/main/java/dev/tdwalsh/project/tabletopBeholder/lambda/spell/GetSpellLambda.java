package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetSpellLambda
        extends LambdaActivityRunner<GetSpellRequest, GetSpellResult>
        implements RequestHandler<LambdaRequest<GetSpellRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetSpellRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path ->
                        GetSpellRequest.builder()
                                .withUserEmail(path.get("userEmail"))
                                .withObjectId(path.get("objectId"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetSpellActivity().handleRequest(request)
        );
    }
}
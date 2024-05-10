package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetAllSpellsRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.GetSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetAllSpellsResult;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.GetSpellResult;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaActivityRunner;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaRequest;
import dev.tdwalsh.project.tabletopBeholder.lambda.LambdaResponse;

public class GetAllSpellsLambda
        extends LambdaActivityRunner<GetAllSpellsRequest, GetAllSpellsResult>
        implements RequestHandler<LambdaRequest<GetAllSpellsRequest>, LambdaResponse> {

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetAllSpellsRequest> input, Context context) {
        return super.runActivity(
                () -> input.fromPath(path ->
                        GetAllSpellsRequest.builder()
                                .withUserEmail(path.get("userEmail"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetAllSpellsActivity().handleRequest(request)
        );
    }
}
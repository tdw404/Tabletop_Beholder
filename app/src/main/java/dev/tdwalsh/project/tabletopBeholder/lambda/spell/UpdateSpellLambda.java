package dev.tdwalsh.project.tabletopBeholder.lambda.spell;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.CreateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.request.UpdateSpellRequest;
import dev.tdwalsh.project.tabletopBeholder.activity.spell.result.CreateSpellResult;
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
                () -> {
                    UpdateSpellRequest partialSpellRequest = input.fromBody(UpdateSpellRequest.class);
                    return input.fromPath(path ->
                            UpdateSpellRequest.builder()
                                    .withUserEmail(path.get("userEmail"))
                                    .withSpellId(path.get("spellId"))
                                    .withObjectName(partialSpellRequest.getObjectName())
                                    .withSpellDescription(partialSpellRequest.getSpellDescription())
                                    .withSpellHigherLevel(partialSpellRequest.getSpellHigherLevel())
                                    .withSpellRange(partialSpellRequest.getSpellRange())
                                    .withSpellComponents(partialSpellRequest.getSpellComponents())
                                    .withSpellMaterial(partialSpellRequest.getSpellMaterial())
                                    .withRitualCast(partialSpellRequest.getRitualCast())
                                    .withCastingTime(partialSpellRequest.getCastingTime())
                                    .withSpellLevel(partialSpellRequest.getSpellLevel())
                                    .withSpellSchool(partialSpellRequest.getSpellSchool())
                                    .withAppliesEffects(partialSpellRequest.getAppliesEffects())
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateSpellActivity().handleRequest(request)
        );
    }
}
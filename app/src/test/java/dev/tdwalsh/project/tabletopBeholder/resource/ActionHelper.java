package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActionHelper {

    public static Action provideAction(Integer mod) {
        Action action = new Action();
        action.setUserEmail("userEmail" + mod);
        action.setObjectId("objectId" + mod);
        action.setObjectName("objectName" + mod);
        action.setActionType("actionType" + mod);
        action.setActionDescription("actionDesction" + mod);
        action.setUses(mod);
        action.setRechargeOn(mod);
        List<Effect> effectList = new ArrayList<>();
        effectList.add(EffectHelper.provideEffect(1));
        effectList.add(EffectHelper.provideEffect(2));
        action.setAppliesEffects(effectList);
        action.setCreateDateTime(ZonedDateTime.now());
        action.setEditDateTime(ZonedDateTime.now());
        return action;
    }
}

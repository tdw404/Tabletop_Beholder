package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Effect effect1 = EffectHelper.provideEffect(1);
        Effect effect2 = EffectHelper.provideEffect(2);
        Map<String, Effect> effectMap = new HashMap<>();
        effectMap.put(effect1.getObjectId(), effect1);
        effectMap.put(effect2.getObjectId(), effect2);
        action.setAppliesEffects(effectMap);
        action.setCreateDateTime(ZonedDateTime.now());
        action.setEditDateTime(ZonedDateTime.now());
        return action;
    }
}

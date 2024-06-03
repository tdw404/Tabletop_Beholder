package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EffectHelper {
    public static Effect provideEffect(Integer mod) {
        Effect effect = new Effect();
        effect.setObjectId("id" + mod);
        effect.setEffectName("effectName" + mod);
        effect.setTurnDuration(mod);
        effect.setBlameSource("blameSource" + mod);
        effect.setBlameCreatureId("blameCreatureId" + mod);
        effect.setBlameConcentration(true);
        effect.setSaveType("saveType" + mod);
        effect.setSaveDC(mod);
        Set<String> newSet = new HashSet<>();
        newSet.add("setItem1");
        newSet.add("setItem2");
        effect.setSaveOn(newSet);
        effect.setEndOn(newSet);
        return effect;
    }
}

package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;

import java.util.ArrayList;
import java.util.List;

public class EffectHelper {
    public static Effect provideEffect(Integer mod) {
        Effect effect = new Effect();
        effect.setEffectName("effectName" + mod);
        effect.setTurnDuration(mod);
        effect.setBlameSource("blameSource" + mod);
        effect.setBlameCreatureId("blameCreatureId" + mod);
        effect.setBlameConcentration(true);
        effect.setSaveType("saveType" + mod);
        effect.setSaveDC(mod);
        List<String> newList = new ArrayList<>();
        newList.add("listItem1");
        newList.add("listItem2");
        effect.setSaveOn(newList);
        effect.setEndOn(newList);
        return effect;
    }
}

package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellHelper {

    public static Spell provideSpell(Integer mod) {
        Spell spell = new Spell();
        spell.setUserEmail("userEmail" + mod);
        spell.setObjectId("objectId" + mod);
        spell.setObjectName("objectName" + mod);
        spell.setSpellDescription("spellDescription" + mod);
        spell.setSpellHigherLevel("spellHigherLevel" + mod);
        spell.setSpellRange("spellRange" + mod);
        spell.setSpellComponents("spellComponents" + mod);
        spell.setSpellMaterial("spellMaterial" + mod);
        spell.setReaction("yes");
        spell.setRitualCast(true);
        spell.setCastingTime("castingTime" + mod);
        spell.setCastingTurns(mod);
        spell.setSpellLevel(mod);
        spell.setSpellSchool("spellSchool" + mod);
        spell.setInnateCasts(mod);
        Map<String, Effect> effectMap = new HashMap<>();
        Effect effect1 = EffectHelper.provideEffect(1);
        Effect effect2 = EffectHelper.provideEffect(2);
        effectMap.put(effect1.getObjectId(), effect1);
        effectMap.put(effect2.getObjectId(), effect2);
        spell.setAppliesEffects(effectMap);
        spell.setCreateDateTime(ZonedDateTime.now());
        spell.setEditDateTime(ZonedDateTime.now());
        return spell;
    }
}

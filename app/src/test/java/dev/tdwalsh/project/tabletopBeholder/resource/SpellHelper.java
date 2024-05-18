package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
        spell.setRitualCast(true);
        spell.setCastingTime(mod);
        spell.setSpellLevel(mod);
        spell.setSpellSchool("spellSchool" + mod);
        spell.setInnateCasts(mod);
        List<Effect> effectList = new ArrayList<>();
        effectList.add(EffectHelper.provideEffect(1));
        effectList.add(EffectHelper.provideEffect(2));
        spell.setAppliesEffects(effectList);
        spell.setCreateDateTime(ZonedDateTime.now());
        spell.setEditDateTime(ZonedDateTime.now());
        return spell;
    }
}

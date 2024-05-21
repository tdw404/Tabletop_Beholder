package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatureHelper {
    public static Creature provideCreature(Integer mod) {
        Creature creature = new Creature();
        creature.setUserEmail("userEmail" + mod);
        creature.setObjectId("objectId" + mod);
        creature.setObjectName("objectName" + mod);
        creature.setEncounterCreatureId("encounterCreatureId" + mod);
        creature.setEncounterCreatureId("encounterCreatureId" + mod);
        creature.setDraftStatus(true);
        creature.setObjectName("objectName" + mod);
        creature.setIsPC(true);
        creature.setPcLevel(mod);
        creature.setSourceBook("sourceBook" + mod);
        creature.setCreatureDescription("creatureDescription" + mod);
        creature.setSize("size" + mod);
        creature.setType("type" + mod);
        creature.setSubType("subType" + mod);
        creature.setGroup("group" + mod);
        creature.setCreatureAlignment("creatureAlignment" + mod);
        creature.setCreatureAC(mod);
        creature.setCreatureArmor("creatureArmor" + mod);
        List<Effect> effectList = new ArrayList<>();
        effectList.add(EffectHelper.provideEffect(1));
        effectList.add(EffectHelper.provideEffect(2));
        creature.setActiveEffects(effectList);
        creature.setKnockedOut(false);
        creature.setDead(false);
        creature.setDeathSaves(mod);
        creature.setHitPoints(mod);
        creature.setCurrentHitPoints(mod);
        creature.setHitDice("hitDice" + mod);
        creature.setCurrentHitDice(mod);
        creature.setWalkSpeed(mod);
        creature.setFlySpeed(mod);
        creature.setSwimSpeed(mod);
        creature.setBurrowSpeed(mod);
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("test1", 1);
        testMap.put("test2", 2);
        creature.setStatMap(testMap);
        creature.setSaveMap(testMap);
        creature.setPassivePerception(mod);
        creature.setSkillsMap(testMap);
        List<String> testList = new ArrayList<>();
        testList.add("test1");
        testList.add("test2");
        creature.setVulnerabilities(testList);
        creature.setResistances(testList);
        creature.setImmunities(testList);
        creature.setConditionImmunities(testList);
        creature.setSenses(testList);
        creature.setLanguages(testList);
        creature.setChallengeRating(new Double(mod));
        List<Action> actionList = new ArrayList<>();
        actionList.add(ActionHelper.provideAction(1));
        actionList.add(ActionHelper.provideAction(2));
        creature.setLegendaryDesc("legendaryDesc" + mod);
        List<Spell> spellList = new ArrayList<>();
        spellList.add(SpellHelper.provideSpell(1));
        spellList.add(SpellHelper.provideSpell(2));
        creature.setSpellList(spellList);
        Map<Integer, String> testMap2 = new HashMap<>();
        testMap2.put(1, "test1");
        testMap2.put(2, "test2");
        creature.setSpellSlots(testMap2);
        creature.setSpellcastingAbility("spellcastingAbility" + mod);
        creature.setSpellSaveDC(mod);
        creature.setSpellAttackModifier(mod);
        creature.setCreateDateTime(ZonedDateTime.now());
        creature.setEditDateTime(ZonedDateTime.now());
        return creature;
    }
}
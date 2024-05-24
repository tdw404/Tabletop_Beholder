package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateCreatureTranslator {

    public static Creature translate(TemplateCreature templateCreature, SpellDao spellDao) {
        Creature creature = new Creature();

        //Easily extracted fields
        creature.setObjectName(templateCreature.getName());
        creature.setIsPC(false);
        creature.setSourceBook(templateCreature.getDocument__title());
        creature.setCreatureDescription(templateCreature.getDesc());
        creature.setSize(templateCreature.getSize());
        creature.setType(templateCreature.getType());
        creature.setSubType(templateCreature.getSubtype());
        creature.setGroup(templateCreature.getGroup());
        creature.setAlignment(templateCreature.getAlignment());
        creature.setArmorClass(templateCreature.getArmor_class());
        creature.setArmorType(templateCreature.getArmor_desc());
        creature.setHitPoints(templateCreature.getHit_points());
        creature.setCurrentHitPoints(templateCreature.getHit_points());
        creature.setHitDice(templateCreature.getHit_dice());
        creature.setSpeedMap(templateCreature.getSpeed());
        HashMap<String, Integer> statMap = new HashMap<>();
        statMap.put("strength", templateCreature.getStrength());
        statMap.put("dexterity", templateCreature.getDexterity());
        statMap.put("constitution", templateCreature.getConstitution());
        statMap.put("intelligence", templateCreature.getIntelligence());
        statMap.put("wisdom", templateCreature.getWisdom());
        statMap.put("charisma", templateCreature.getCharisma());
        creature.setStatMap(statMap);
        HashMap<String, Integer> saveMap = new HashMap<>();
        saveMap.put("strength_save", templateCreature.getStrength_save());
        saveMap.put("dexterity_save", templateCreature.getDexterity_save());
        saveMap.put("constitution_save", templateCreature.getConstitution_save());
        saveMap.put("intelligence_save", templateCreature.getIntelligence_save());
        saveMap.put("wisdom_save", templateCreature.getWisdom_save());
        saveMap.put("charisma_save", templateCreature.getCharisma_save());
        creature.setSaveMap(saveMap);
        creature.setPassivePerception(templateCreature.getPerception());
        creature.setSkillsMap(templateCreature.getSkills());
        creature.setVulnerabilities(templateCreature.getDamage_vulnerabilities());
        creature.setResistances(templateCreature.getDamage_resistances());
        creature.setImmunities(templateCreature.getDamage_immunities());
        creature.setSenses(templateCreature.getSenses());
        creature.setLanguages(templateCreature.getLanguages());
        creature.setChallengeRating(new Double(templateCreature.getChallenge_rating()));
        creature.setLegendaryDesc(templateCreature.getLegendary_desc());
        creature.setCreateDateTime(ZonedDateTime.now());
        creature.setEditDateTime(creature.getEditDateTime());

        //Actions
        Map<String, List<Action>> actionMap = new HashMap<>();
        actionMap.put("action", Optional.ofNullable(templateCreature.getActions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "action"))
                .collect(Collectors.toList()));
        actionMap.put("bonus", Optional.ofNullable(templateCreature.getBonus_actions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "bonus"))
                .collect(Collectors.toList()));
        actionMap.put("reaction", Optional.ofNullable(templateCreature.getReactions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "reaction"))
                .collect(Collectors.toList()));
        actionMap.put("legendary", Optional.ofNullable(templateCreature.getLegendary_actions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "legendary"))
                .collect(Collectors.toList()));
        actionMap.put("special", Optional.ofNullable(templateCreature.getSpecial_abilities()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "special"))
                .collect(Collectors.toList()));
        creature.setActionMap(actionMap);

        //TODO spell slots
        //TODO spellcasting ability
        //TODO spell save dc
        //TODO spell converter
        return creature;
    }
}


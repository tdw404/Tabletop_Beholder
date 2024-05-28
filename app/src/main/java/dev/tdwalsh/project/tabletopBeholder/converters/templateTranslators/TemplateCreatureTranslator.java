package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
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

        //Import Actions
        //Retrieve actions from JSON lists and add to a single action map
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

        List<Spell> spellList = new ArrayList<>();

        //Import Innate Spellcasting
        //Finds the 'Innate Spellcasting' special action, splits into lines.
        //Gets spellcasting ability and DC from first lines
        //Gets spells and number of innate casts from each subsequent lines
        List<String> sentenceTokens = Arrays.asList(
                Optional.ofNullable(actionMap.get("special")).orElse(Collections.emptyList())
                .stream()
                .filter(action -> action.getObjectName().equals("Innate Spellcasting"))
                .collect(Collectors.toList())
                .get(0)
                .getActionDescription()
                .split("\\n\\n"));

        List<String> spellNames = new ArrayList<>();
        sentenceTokens.forEach(sentenceToken -> {
            if (sentenceToken.contains("spell save")) {
                creature.setSpellcastingAbility(Arrays.asList(sentenceToken.split("ability is | \\(")).get(1));
                creature.setSpellSaveDC(Arrays.asList(sentenceToken.split("spell save DC |\\)")).get(1));
            }

            if (sentenceToken.contains("at will")) {
                int innateCasts = -1;
                Arrays.stream(sentenceToken.split(", "))
                        .forEach(spellName -> {
                            //First, check to see if spell already exists in library
                            //Else, check remote and retrieve the spell if possible
                            //Else, create a blank spell with this name
//                            try {
//                                String spellId = spe
//                            }
                        });
            }
        });


        //TODO spell slots
        //TODO spellcasting ability
        //TODO spell save dc
        //TODO spell converter
        return creature;
    }
}


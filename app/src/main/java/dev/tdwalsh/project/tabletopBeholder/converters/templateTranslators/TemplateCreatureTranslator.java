package dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators;

import dev.tdwalsh.project.tabletopBeholder.activity.helpers.CreateObjectHelper;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.SpellDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateSpellDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.apache.commons.text.WordUtils;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TemplateCreatureTranslator {

    private SpellDao spellDao;
    private TemplateSpellDao templateSpellDao;

    @Inject
    public TemplateCreatureTranslator (SpellDao spellDao, TemplateSpellDao templateSpellDao) {
        this.spellDao = spellDao;
        this.templateSpellDao = templateSpellDao;
    }


    public Creature translate(TemplateCreature templateCreature, String userEmail) {
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
        List<String> sentenceTokens = new ArrayList<>();
        Action castAction = Optional.ofNullable(actionMap.get("special"))
                .orElse(Collections.emptyList())
                .stream()
                .filter(action -> action.getObjectName().equals("Innate Spellcasting"))
                .findFirst().orElse(null);
        if (castAction != null) {
            sentenceTokens = Arrays.asList(castAction
                    .getActionDescription()
                    .split("\\n\\n"));
        }

        sentenceTokens.forEach(sentenceToken -> {
            if (sentenceToken.contains("spellcasting ability")) {
                //Retrieves spellcasting stats
                creature.setSpellcastingAbility(Arrays.asList(sentenceToken.split("ability is | \\(")).get(1));
                String protoSpellSave = Arrays.asList(sentenceToken.split("save DC |\\)")).get(1);
                creature.setSpellSaveDC(Arrays.asList(protoSpellSave.split(",")).get(0));
                if(sentenceToken.contains("to hit with spell attacks")) {
                    List<String> splitTokens = Arrays.asList(
                            Arrays.asList(sentenceToken.split(" to hit with spell attacks"))
                                    .get(0).split(", | "));
                    creature.setSpellAttackModifier(splitTokens.get(splitTokens.size() - 1));
                }
            }

            if (sentenceToken.contains("at will") || sentenceToken.contains("/day")) {
                //Extract number of innate casts for this set of spells
                int casts = 0;
                if (sentenceToken.contains("at will")) {
                    casts = -1;
                }
                else {
                    List<String> innateSplit = Arrays.asList(sentenceToken.split("/day"));
                    try {
                        casts = Integer.parseInt(innateSplit.get(0));
                    } catch (Exception e) {
                        casts = 1;
                    }
                }
                final int innateCasts = casts;
                Arrays.stream(Arrays.asList(sentenceToken.split(": ")).get(1).split(", "))
                        .map(WordUtils::capitalizeFully)
                        .forEach(spellName -> {
                            //First, check to see if spell already exists in library
                            //Else, check remote and retrieve the spell if possible
                            //Else, create a blank spell with this name
                            Spell spell = spellDao.getSpellByName(userEmail, spellName);
                            if (spell != null) {
                                spell.setInnateCasts(innateCasts);
                                spellList.add(spell);
                            } else {
                                //Since the external api does not appear to allow searches by name
                                //It looks like we're forced to get a list of partial matches
                                //Then filter through and find the matching name manually
                                List<TemplateSpell> templateSpellList= templateSpellDao.getMultiple("search=" + spellName.replace(" ", "%20"));
                                TemplateSpell templateSpell = templateSpellList.stream()
                                        .filter(template -> template.getName().equals(spellName))
                                        .findFirst().orElse(null);
                                if (templateSpell != null) {
                                    spell = TemplateSpellTranslator.translate(templateSpell);
                                    spell.setUserEmail(userEmail);
                                    spell = (Spell) CreateObjectHelper.createObject(spellDao, spell);
                                    spell.setInnateCasts(innateCasts);
                                    spellList.add(spell);
                                } else {
                                    spell = new Spell();
                                    spell.setUserEmail(userEmail);
                                    spell.setObjectName(spellName);
                                    spell = (Spell) CreateObjectHelper.createObject(spellDao, spell);
                                    spell.setInnateCasts(innateCasts);
                                    spellList.add(spell);
                                }
                            }
                        });
            }
        });

        //Import spellcasting
        //Finds the 'Spellcasting' special action, splits into lines.
        //Gets spellcasting ability and DC from first lines
        //Gets spells and number of spell slots from each subsequent lines


        sentenceTokens = new ArrayList<>();
        Map<Integer,Integer> spellSlots = new HashMap<>();
        castAction = Optional.ofNullable(actionMap.get("special"))
                .orElse(Collections.emptyList())
                .stream()
                .filter(action -> action.getObjectName().equals("Spellcasting"))
                .findFirst().orElse(null);
        if (castAction != null) {
            sentenceTokens = Arrays.asList(castAction
                    .getActionDescription()
                    .split("\\n"));
        }

        sentenceTokens.forEach(sentenceToken -> {
            if (sentenceToken.contains("spellcasting ability")) {
                //Retrieves spellcasting stats
                creature.setSpellcastingAbility(Arrays.asList(sentenceToken.split("ability is | \\(")).get(1));
                String protoSpellSave = Arrays.asList(sentenceToken.split("save DC |\\)")).get(1);
                creature.setSpellSaveDC(Arrays.asList(protoSpellSave.split(",")).get(0));
                if(sentenceToken.contains("to hit with spell attacks")) {
                    List<String> splitTokens = Arrays.asList(
                            Arrays.asList(sentenceToken.split(" to hit with spell attacks"))
                                    .get(0).split(", | "));
                    creature.setSpellAttackModifier(splitTokens.get(splitTokens.size() - 1));
                }
            }

            if (sentenceToken.contains("at will") || sentenceToken.contains("slots")) {
                //Extract number of innate casts for this set of spells
                int casts = 0;
                int level = 0;
                if (sentenceToken.contains("at will")) {
                    level = -1;
                    casts = -1;
                }
                else {
                    List<String> slotsSplit = Arrays.asList(sentenceToken.split("\\(| slots"));
                    try {
                        casts = Integer.parseInt(slotsSplit.get(1));
                    } catch (Exception e) {
                        casts = 1;
                    }
                    try {
                        level = Integer.parseInt(sentenceToken.substring(0,1));
                    } catch (Exception e) {
                        level = 1;
                    }
                }
                spellSlots.put(level, casts);

                Arrays.stream(Arrays.asList(sentenceToken.split(": ")).get(1).split(", "))
                        .map(WordUtils::capitalizeFully)
                        .forEach(spellName -> {
                            //First, check to see if spell already exists in library
                            //Else, check remote and retrieve the spell if possible
                            //Else, create a blank spell with this name
                            Spell spell = spellDao.getSpellByName(userEmail, spellName);
                            if (spell != null) {
                                spellList.add(spell);
                            } else {
                                //Since the external api does not appear to allow searches by name
                                //It looks like we're forced to get a list of partial matches
                                //Then filter through and find the matching name manually
                                List<TemplateSpell> templateSpellList= templateSpellDao.getMultiple("search=" + spellName.replace(" ", "%20"));
                                TemplateSpell templateSpell = templateSpellList.stream()
                                        .filter(template -> template.getName().equals(spellName))
                                        .findFirst().orElse(null);
                                if (templateSpell != null) {
                                    spell = TemplateSpellTranslator.translate(templateSpell);
                                    spell.setUserEmail(userEmail);
                                    spell = (Spell) CreateObjectHelper.createObject(spellDao, spell);
                                    spellList.add(spell);
                                } else {
                                    spell = new Spell();
                                    spell.setUserEmail(userEmail);
                                    spell.setObjectName(spellName);
                                    spell = (Spell) CreateObjectHelper.createObject(spellDao, spell);
                                    spellList.add(spell);
                                }
                            }
                        });
            }
        });
        creature.setSpellList(spellList);
        creature.setSpellSlots(spellSlots);
        return creature;
    }
}


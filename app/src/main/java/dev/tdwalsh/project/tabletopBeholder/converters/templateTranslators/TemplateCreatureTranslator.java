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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

public class TemplateCreatureTranslator {

    private SpellDao spellDao;
    private TemplateSpellDao templateSpellDao;

    /**
     * Constructor.
     * @param spellDao - Dao dependency.
     * @param templateSpellDao - Dao dependency.
     */
    @Inject
    public TemplateCreatureTranslator(SpellDao spellDao, TemplateSpellDao templateSpellDao) {
        this.spellDao = spellDao;
        this.templateSpellDao = templateSpellDao;
    }

    /**
     * Translates between the Creature model used by Open5E and the Creature model used by Beholder.
     * @param templateCreature - Template to be translated.
     * @param userEmail - User whose account this creature should be associated with.
     * @return Beholder Creature
     */
    public Creature translate(TemplateCreature templateCreature, String userEmail) {
        return translate(templateCreature, userEmail, new ArrayList<>());
    }

    public Creature translate(TemplateCreature templateCreature, String userEmail, List<String> warnings) {
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
        Map<String, Integer> statMap = new HashMap<>();
        statMap.put("strength", templateCreature.getStrength());
        statMap.put("dexterity", templateCreature.getDexterity());
        statMap.put("constitution", templateCreature.getConstitution());
        statMap.put("intelligence", templateCreature.getIntelligence());
        statMap.put("wisdom", templateCreature.getWisdom());
        statMap.put("charisma", templateCreature.getCharisma());
        creature.setStatMap(statMap);
        Map<String, Integer> saveMap = new HashMap<>();
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
        creature.setConditionImmunities(templateCreature.getCondition_immunities());
        creature.setSenses(templateCreature.getSenses());
        creature.setLanguages(templateCreature.getLanguages());
        Double challengeRating = parseChallengeRating(templateCreature.getChallenge_rating());
        if (challengeRating == null) {
            challengeRating = templateCreature.getCr();
        }
        if (isValidChallengeRating(challengeRating)) {
            creature.setChallengeRating(challengeRating);
        } else {
            warnings.add("Creature imported. Review its missing or invalid challenge rating.");
        }
        creature.setLegendaryDesc(templateCreature.getLegendary_desc());
        creature.setCreateDateTime(ZonedDateTime.now());
        creature.setEditDateTime(creature.getEditDateTime());

        //Import Actions
        //Retrieve actions from JSON lists and add to a single action map
        Map<String, Action> actionMap = new LinkedHashMap<>();
        Optional.ofNullable(templateCreature.getActions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "standard"))
                .forEach(action -> actionMap.put(action.getObjectId(), action));
        Optional.ofNullable(templateCreature.getBonus_actions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "bonus"))
                .forEach(action -> actionMap.put(action.getObjectId(), action));
        Optional.ofNullable(templateCreature.getReactions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "reaction"))
                .forEach(action -> actionMap.put(action.getObjectId(), action));
        Optional.ofNullable(templateCreature.getLegendary_actions()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "legendary"))
                .forEach(action -> actionMap.put(action.getObjectId(), action));
        Optional.ofNullable(templateCreature.getSpecial_abilities()).orElse(Collections.emptyList())
                .stream()
                .map(templateAction -> TemplateActionTranslator.translate(templateAction, "special"))
                .forEach(action -> actionMap.put(action.getObjectId(), action));
        creature.setActionMap(actionMap);

        importSpellcasting(creature, actionMap, userEmail, warnings);
        return creature;
    }

    private static Double parseChallengeRating(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String[] parts = text.split("/", -1);
        try {
            double rating = Double.parseDouble(parts[0].strip());
            if (parts.length == 2) {
                double denominator = Double.parseDouble(parts[1].strip());
                if (!Double.isFinite(denominator) || denominator <= 0) {
                    return null;
                }
                rating /= denominator;
            } else if (parts.length != 1) {
                return null;
            }
            return isValidChallengeRating(rating) ? rating : null;
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static boolean isValidChallengeRating(Double rating) {
        return rating != null && Double.isFinite(rating) && rating >= 0;
    }

    private void importSpellcasting(Creature creature, Map<String, Action> actions, String userEmail,
                                    List<String> warnings) {
        Map<String, Integer> spellUses = new LinkedHashMap<>();
        Map<Integer, Integer> slots = new HashMap<>();
        boolean incomplete = false;
        for (Action action : actions.values()) {
            if (!SpellcastingParser.recognizes(action.getObjectName())) {
                continue;
            }
            SpellcastingParser.Parsed parsed = SpellcastingParser.parse(action.getObjectName(),
                    action.getActionDescription());
            if (parsed.ability != null) {
                creature.setSpellcastingAbility(WordUtils.capitalizeFully(parsed.ability));
            }
            if (parsed.saveDc != null) {
                creature.setSpellSaveDC(parsed.saveDc);
            }
            if (parsed.attack != null) {
                creature.setSpellAttackModifier(parsed.attack);
            }
            slots.putAll(parsed.slots);
            for (Map.Entry<String, Integer> entry : parsed.spells.entrySet()) {
                Integer previous = spellUses.putIfAbsent(entry.getKey(), entry.getValue());
                incomplete |= previous != null && entry.getValue() != null && !previous.equals(entry.getValue());
            }
            incomplete |= parsed.incomplete;
        }
        Map<String, Spell> spellMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : spellUses.entrySet()) {
            Spell spell = resolveSpell(WordUtils.capitalizeFully(entry.getKey()), userEmail);
            if (entry.getValue() != null) {
                spell.setInnateCasts(entry.getValue());
            }
            spellMap.put(spell.getObjectId(), spell);
        }
        creature.setSpellMap(spellMap);
        creature.setSpellSlots(slots);
        if (incomplete) {
            warnings.add("Creature imported. Review linked spells in its Spellcasting trait.");
        }
    }

    private Spell resolveSpell(String name, String userEmail) {
        Spell saved = spellDao.getSpellByName(userEmail, name);
        if (saved != null) {
            return saved;
        }
        String searchName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        Optional<TemplateSpell> template = templateSpellDao.getMultiple("search=" + searchName).stream()
                .filter(candidate -> name.equalsIgnoreCase(candidate.getName()))
                .findFirst();
        Spell spell = template.map(TemplateSpellTranslator::translate).orElseGet(Spell::new);
        if (template.isEmpty()) {
            spell.setObjectName(name);
        }
        spell.setUserEmail(userEmail);
        return (Spell) CreateObjectHelper.createObject(spellDao, spell);
    }
}

package dev.tdwalsh.project.tabletopBeholder.resource.template;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateAction;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateCreatureHelper {

    public static TemplateCreature provideTemplateCreature(Integer mod) {
        TemplateCreature templateCreature = new TemplateCreature();
        templateCreature.setSlug("slug" + mod);
        templateCreature.setDesc("desc" + mod);
        templateCreature.setName("name" + mod);
        templateCreature.setSize("size" + mod);
        templateCreature.setType("type" + mod);
        templateCreature.setSubtype("subtype" + mod);
        templateCreature.setGroup("group" + mod);
        templateCreature.setAlignment("alignment" + mod);
        templateCreature.setArmor_class(mod);
        templateCreature.setArmor_desc("armor_desc" + mod);
        templateCreature.setHit_points(mod);
        templateCreature.setHit_dice("hit_dice" + mod);
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("One", 1);
        stringIntegerMap.put("Two", 2);
        templateCreature.setSpeed(stringIntegerMap);
        templateCreature.setStrength(mod);
        templateCreature.setDexterity(mod);
        templateCreature.setConstitution(mod);
        templateCreature.setIntelligence(mod);
        templateCreature.setWisdom(mod);
        templateCreature.setCharisma(mod);
        templateCreature.setStrength_save(mod);
        templateCreature.setDexterity_save(mod);
        templateCreature.setConstitution_save(mod);
        templateCreature.setIntelligence_save(mod);
        templateCreature.setWisdom_save(mod);
        templateCreature.setCharisma_save(mod);
        templateCreature.setPerception(mod);
        templateCreature.setSkills(stringIntegerMap);
        templateCreature.setDamage_vulnerabilities("damage_vulnerabilities" + mod);
        templateCreature.setDamage_resistances("damage_resistances" + mod);
        templateCreature.setDamage_immunities("damage_immunities" + mod);
        templateCreature.setCondition_immunities("condition_immunities" + mod);
        templateCreature.setSenses("senses" + mod);
        templateCreature.setLanguages("languages" + mod);
        templateCreature.setChallenge_rating("challenge_rating" + mod);
        templateCreature.setCr(mod);
        List<TemplateAction> actionList = new ArrayList<>();
        actionList.add(TemplateActionHelper.provideTemplateAction(1));
        actionList.add(TemplateActionHelper.provideTemplateAction(2));
        templateCreature.setActions(actionList);
        templateCreature.setBonus_actions(actionList);
        templateCreature.setReactions(actionList);
        templateCreature.setLegendary_desc("legendary_desc" + mod);
        templateCreature.setLegendary_actions(actionList);
        templateCreature.setSpecial_abilities(actionList);
        List<String> stringList = new ArrayList<>();
        stringList.add("item1");
        stringList.add("item2");
        templateCreature.setSpell_list(stringList);
        templateCreature.setPage_no(mod);
        templateCreature.setEnvironments(stringList);
        templateCreature.setImg_main("img_main" + mod);
        templateCreature.setDocument__slug("document__slug" + mod);
        templateCreature.setDocument__title("document__title" + mod);
        templateCreature.setDocument__license_url("docuement__license_url" + mod);
        templateCreature.setDocument__url("document__url" + mod);
        templateCreature.setResourceExists(false);
        return templateCreature;
    }
}

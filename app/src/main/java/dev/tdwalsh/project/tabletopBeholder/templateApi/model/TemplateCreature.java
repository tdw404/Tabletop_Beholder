package dev.tdwalsh.project.tabletopBeholder.templateApi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TemplateCreature {
    private String slug;
    private String desc;
    private String name;
    private String size;
    private String type;
    private String subtype;
    private String group;
    private String alignment;
    private int armor_class;
    private String armor_desc;
    private int hit_points;
    private String hit_dice;
    private Map<String, Integer> speed;
    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int strength_save;
    private int dexterity_save;
    private int constitution_save;
    private int intelligence_save;
    private int wisdom_save;
    private int charisma_save;
    private int perception;
    private Map<String, Integer> skills;
    private String damage_vulnerabilities;
    private String damage_resistances;
    private String damage_immunities;
    private String condition_immunities;
    private String senses;
    private String languages;
    private String challenge_rating;
    private int cr;
    private String actions;
    private String bonus_actions;
    private String reactions;
    private String legendary_desc;
    private String legendary_actions;
    private String special_abilities;
    private String spell_list;
    private int page_no;
    private String environments;
    private String img_main;
    private String document__slug;
    private String document__title;
    private String document__license_url;
    private String document__url;
    private transient boolean resourceExists  = false;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public int getArmor_class() {
        return armor_class;
    }

    public void setArmor_class(int armor_class) {
        this.armor_class = armor_class;
    }

    public String getArmor_desc() {
        return armor_desc;
    }

    public void setArmor_desc(String armor_desc) {
        this.armor_desc = armor_desc;
    }

    public int getHit_points() {
        return hit_points;
    }

    public void setHit_points(int hit_points) {
        this.hit_points = hit_points;
    }

    public String getHit_dice() {
        return hit_dice;
    }

    public void setHit_dice(String hit_dice) {
        this.hit_dice = hit_dice;
    }

    public Map<String, Integer> getSpeed() {
        return speed;
    }

    public void setSpeed(Map<String, Integer> speed) {
        this.speed = speed;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getStrength_save() {
        return strength_save;
    }

    public void setStrength_save(int strength_save) {
        this.strength_save = strength_save;
    }

    public int getDexterity_save() {
        return dexterity_save;
    }

    public void setDexterity_save(int dexterity_save) {
        this.dexterity_save = dexterity_save;
    }

    public int getConstitution_save() {
        return constitution_save;
    }

    public void setConstitution_save(int constitution_save) {
        this.constitution_save = constitution_save;
    }

    public int getIntelligence_save() {
        return intelligence_save;
    }

    public void setIntelligence_save(int intelligence_save) {
        this.intelligence_save = intelligence_save;
    }

    public int getWisdom_save() {
        return wisdom_save;
    }

    public void setWisdom_save(int wisdom_save) {
        this.wisdom_save = wisdom_save;
    }

    public int getCharisma_save() {
        return charisma_save;
    }

    public void setCharisma_save(int charisma_save) {
        this.charisma_save = charisma_save;
    }

    public int getPerception() {
        return perception;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public Map<String, Integer> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, Integer> skills) {
        this.skills = skills;
    }

    public String getDamage_vulnerabilities() {
        return damage_vulnerabilities;
    }

    public void setDamage_vulnerabilities(String damage_vulnerabilities) {
        this.damage_vulnerabilities = damage_vulnerabilities;
    }

    public String getDamage_resistances() {
        return damage_resistances;
    }

    public void setDamage_resistances(String damage_resistances) {
        this.damage_resistances = damage_resistances;
    }

    public String getDamage_immunities() {
        return damage_immunities;
    }

    public void setDamage_immunities(String damage_immunities) {
        this.damage_immunities = damage_immunities;
    }

    public String getCondition_immunities() {
        return condition_immunities;
    }

    public void setCondition_immunities(String condition_immunities) {
        this.condition_immunities = condition_immunities;
    }

    public String getSenses() {
        return senses;
    }

    public void setSenses(String senses) {
        this.senses = senses;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getChallenge_rating() {
        return challenge_rating;
    }

    public void setChallenge_rating(String challenge_rating) {
        this.challenge_rating = challenge_rating;
    }

    public int getCr() {
        return cr;
    }

    public void setCr(int cr) {
        this.cr = cr;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getBonus_actions() {
        return bonus_actions;
    }

    public void setBonus_actions(String bonus_actions) {
        this.bonus_actions = bonus_actions;
    }

    public String getReactions() {
        return reactions;
    }

    public void setReactions(String reactions) {
        this.reactions = reactions;
    }

    public String getLegendary_desc() {
        return legendary_desc;
    }

    public void setLegendary_desc(String legendary_desc) {
        this.legendary_desc = legendary_desc;
    }

    public String getLegendary_actions() {
        return legendary_actions;
    }

    public void setLegendary_actions(String legendary_actions) {
        this.legendary_actions = legendary_actions;
    }

    public String getSpecial_abilities() {
        return special_abilities;
    }

    public void setSpecial_abilities(String special_abilities) {
        this.special_abilities = special_abilities;
    }

    public String getSpell_list() {
        return spell_list;
    }

    public void setSpell_list(String spell_list) {
        this.spell_list = spell_list;
    }

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }

    public String getEnvironments() {
        return environments;
    }

    public void setEnvironments(String environments) {
        this.environments = environments;
    }

    public String getImg_main() {
        return img_main;
    }

    public void setImg_main(String img_main) {
        this.img_main = img_main;
    }

    public String getDocument__slug() {
        return document__slug;
    }

    public void setDocument__slug(String document__slug) {
        this.document__slug = document__slug;
    }

    public String getDocument__title() {
        return document__title;
    }

    public void setDocument__title(String document__title) {
        this.document__title = document__title;
    }

    public String getDocument__license_url() {
        return document__license_url;
    }

    public void setDocument__license_url(String document__license_url) {
        this.document__license_url = document__license_url;
    }

    public String getDocument__url() {
        return document__url;
    }

    public void setDocument__url(String document__url) {
        this.document__url = document__url;
    }

    @JsonIgnoreProperties(value = { "y" })
    public boolean getResourceExists() {
        return this.resourceExists;
    }

    public void setResourceExists(boolean resourceExists) {
        this.resourceExists = resourceExists;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.slug);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        TemplateCreature other = (TemplateCreature) o;
        return this.slug.equals(other.slug);
    }

}

package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.*;
import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.*;

@DynamoDBTable(tableName = "TabletopBeholder_CreatureTable")
public class Creature implements BeholderObject {
    private String userEmail;
    private String objectId;
    private String encounterCreatureId;
    private String encounterCreatureName;
    private Boolean draftStatus;
    private String objectName;
    private Boolean isPC;
    private Integer pcLevel;
    private String sourceBook;
    private String creatureDescription;
    private String size;
    private String type;
    private String subType;
    private String group;
    private String alignment;
    private Integer armorClass;
    private String armorType;
    private List<Effect> activeEffects;
    private Boolean knockedOut;
    private Boolean dead;
    private Integer deathSaves;
    private Integer hitPoints;
    private Integer currentHitPoints;
    private String hitDice;
    private Integer currentHitDice;
    private Map<String, Integer> speedMap;
    private Map<String, Integer> statMap;
    private Map<String, Integer> saveMap;
    private Integer passivePerception;
    private Map<String, Integer> skillsMap;
    private String vulnerabilities;
    private String resistances;
    private String immunities;
    private String conditionImmunities;
    private String senses;
    private String languages;
    private Double challengeRating;
    private Map<String, List<Action>> actionMap;
    private String legendaryDesc;
    private List<Spell> spellList;
    private Map<Integer, Integer> spellSlots;
    private String spellcastingAbility;
    private String spellSaveDC;
    private Integer spellAttackModifier;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @Override
    @DynamoDBHashKey(attributeName = "userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    @DynamoDBRangeKey(attributeName = "objectId")
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    @DynamoDBAttribute(attributeName = "objectName")
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = WordUtils.capitalizeFully(objectName);
    }

    @DynamoDBAttribute(attributeName = "encounterCreatureId")
    public String getEncounterCreatureId() {
        return encounterCreatureId;
    }

    public void setEncounterCreatureId(String encounterCreatureId) {
        this.encounterCreatureId = encounterCreatureId;
    }

    @DynamoDBAttribute(attributeName = "encounterCreatureName")
    public String getEncounterCreatureName() {
        return encounterCreatureName;
    }

    public void setEncounterCreatureName(String encounterCreatureName) {
        this.encounterCreatureName = encounterCreatureName;
    }

    @DynamoDBAttribute(attributeName = "draftStatus")
    @DynamoDBTyped(DynamoDBAttributeType.BOOL)
    public Boolean getDraftStatus() {
        return draftStatus;
    }

    public void setDraftStatus(Boolean draftStatus) {
        this.draftStatus = draftStatus;
    }

    @DynamoDBAttribute(attributeName = "isPC")
    @DynamoDBTyped(DynamoDBAttributeType.BOOL)
    public Boolean getIsPC() {
        return isPC;
    }

    public void setIsPC(Boolean PC) {
        isPC = PC;
    }

    @DynamoDBAttribute(attributeName = "pcLevel")
    public Integer getPcLevel() {
        return pcLevel;
    }

    public void setPcLevel(Integer pcLevel) {
        this.pcLevel = pcLevel;
    }

    @DynamoDBAttribute(attributeName = "sourceBook")
    public String getSourceBook() {
        return sourceBook;
    }

    public void setSourceBook(String sourceBook) {
        this.sourceBook = sourceBook;
    }

    @DynamoDBAttribute(attributeName = "creatureDescription")
    public String getCreatureDescription() {
        return creatureDescription;
    }

    public void setCreatureDescription(String creatureDescription) {
        this.creatureDescription = creatureDescription;
    }

    @DynamoDBAttribute(attributeName = "size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @DynamoDBAttribute(attributeName = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DynamoDBAttribute(attributeName = "subType")
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    @DynamoDBAttribute(attributeName = "group")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @DynamoDBAttribute(attributeName = "creatureAlignment")
    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    @DynamoDBAttribute(attributeName = "creatureAC")
    public Integer getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(Integer armorClass) {
        this.armorClass = armorClass;
    }

    @DynamoDBAttribute(attributeName = "creatureArmor")
    public String getArmorType() {
        return armorType;
    }

    public void setArmorType(String armorType) {
        this.armorType = armorType;
    }

    @DynamoDBAttribute(attributeName = "activeEffects")
    @DynamoDBTypeConverted(converter = EffectConverter.class)
    public List<Effect> getActiveEffects() {
        return activeEffects;
    }

    public void setActiveEffects(List<Effect> activeEffects) {
        this.activeEffects = activeEffects;
    }

    @DynamoDBAttribute(attributeName = "knockedOut")
    @DynamoDBTyped(DynamoDBAttributeType.BOOL)
    public Boolean getKnockedOut() {
        return knockedOut;
    }

    public void setKnockedOut(Boolean knockedOut) {
        this.knockedOut = knockedOut;
    }

    @DynamoDBAttribute(attributeName = "dead")
    @DynamoDBTyped(DynamoDBAttributeType.BOOL)
    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
    }

    @DynamoDBAttribute(attributeName = "deathSaves")
    public Integer getDeathSaves() {
        return deathSaves;
    }

    public void setDeathSaves(Integer deathSaves) {
        this.deathSaves = deathSaves;
    }

    @DynamoDBAttribute(attributeName = "hitPoints")
    public Integer getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(Integer hitPoints) {
        this.hitPoints = hitPoints;
    }

    @DynamoDBAttribute(attributeName = "currentHitPoints")
    public Integer getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(Integer currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }

    @DynamoDBAttribute(attributeName = "hitDice")
    public String getHitDice() {
        return hitDice;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }

    @DynamoDBAttribute(attributeName = "currentHitDice")
    public Integer getCurrentHitDice() {
        return currentHitDice;
    }

    public void setCurrentHitDice(Integer currentHitDice) {
        this.currentHitDice = currentHitDice;
    }

    @DynamoDBAttribute(attributeName = "speedMap")
    @DynamoDBTypeConverted(converter = StringIntMapConverter.class)
    public Map<String, Integer> getSpeedMap() {
        return speedMap;
    }

    public void setSpeedMap(Map<String, Integer> speedMap) {
        this.speedMap = speedMap;
    }

    @DynamoDBAttribute(attributeName = "statMap")
    @DynamoDBTypeConverted(converter = StringIntMapConverter.class)
    public Map<String, Integer> getStatMap() {
        return statMap;
    }

    public void setStatMap(Map<String, Integer> statMap) {
        this.statMap = statMap;
    }

    @DynamoDBAttribute(attributeName = "saveMap")
    @DynamoDBTypeConverted(converter = StringIntMapConverter.class)
    public Map<String, Integer> getSaveMap() {
        return saveMap;
    }

    public void setSaveMap(Map<String, Integer> saveMap) {
        this.saveMap = saveMap;
    }

    @DynamoDBAttribute(attributeName = "passivePerception")
    public Integer getPassivePerception() {
        return passivePerception;
    }

    public void setPassivePerception(Integer passivePerception) {
        this.passivePerception = passivePerception;
    }

    @DynamoDBAttribute(attributeName = "skillsMap")
    @DynamoDBTypeConverted(converter = StringIntMapConverter.class)
    public Map<String, Integer> getSkillsMap() {
        return skillsMap;
    }

    public void setSkillsMap(Map<String, Integer> skillsMap) {
        this.skillsMap = skillsMap;
    }

    @DynamoDBAttribute(attributeName = "vulnerabilities")
    public String getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(String vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    @DynamoDBAttribute(attributeName = "resistances")
    public String getResistances() {
        return resistances;
    }

    public void setResistances(String resistances) {
        this.resistances = resistances;
    }

    @DynamoDBAttribute(attributeName = "immunities")
    public String getImmunities() {
        return immunities;
    }

    public void setImmunities(String immunities) {
        this.immunities = immunities;
    }

    @DynamoDBAttribute(attributeName = "conditionImmunities")
    public String getConditionImmunities() {
        return conditionImmunities;
    }

    public void setConditionImmunities(String conditionImmunities) {
        this.conditionImmunities = conditionImmunities;
    }

    @DynamoDBAttribute(attributeName = "senses")
    public String getSenses() {
        return senses;
    }

    public void setSenses(String senses) {
        this.senses = senses;
    }

    @DynamoDBAttribute(attributeName = "languages")
    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    @DynamoDBAttribute(attributeName = "challengeRating")
    public Double getChallengeRating() {
        return challengeRating;
    }

    public void setChallengeRating(Double challengeRating) {
        this.challengeRating = challengeRating;
    }

    @DynamoDBAttribute(attributeName = "actionMap")
    @DynamoDBTypeConverted(converter = StringActionsMapConverter.class)
    public Map<String, List<Action>> getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map<String, List<Action>> actionMap) {
        this.actionMap = actionMap;
    }

    @DynamoDBAttribute(attributeName = "legendaryDesc")
    public String getLegendaryDesc() {
        return legendaryDesc;
    }

    public void setLegendaryDesc(String legendaryDesc) {
        this.legendaryDesc = legendaryDesc;
    }

    @DynamoDBAttribute(attributeName = "spellList")
    @DynamoDBTypeConverted(converter = SpellConverter.class)
    public List<Spell> getSpellList() {
        return spellList;
    }

    public void setSpellList(List<Spell> spellList) {
        this.spellList = spellList;
    }

    @DynamoDBAttribute(attributeName = "spellSlots")
    @DynamoDBTypeConverted(converter =  IntIntMapConverter.class)
    public Map<Integer, Integer> getSpellSlots() {
        return spellSlots;
    }

    public void setSpellSlots(Map<Integer, Integer> spellSlots) {
        this.spellSlots = spellSlots;
    }

    @DynamoDBAttribute(attributeName = "spellcastingAbility")
    public String getSpellcastingAbility() {
        return spellcastingAbility;
    }

    public void setSpellcastingAbility(String spellcastingAbility) {
        this.spellcastingAbility = spellcastingAbility;
    }

    @DynamoDBAttribute(attributeName = "spellSaveDC")
    public String getSpellSaveDC() {
        return spellSaveDC;
    }

    public void setSpellSaveDC(String spellSaveDC) {
        this.spellSaveDC = spellSaveDC;
    }

    @DynamoDBAttribute(attributeName = "spellAttackModifier")
    public Integer getSpellAttackModifier() {
        return spellAttackModifier;
    }

    public void setSpellAttackModifier(Integer spellAttackModifier) {
        this.spellAttackModifier = spellAttackModifier;
    }
    @DynamoDBAttribute(attributeName= "createDateTime")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @DynamoDBAttribute(attributeName = "editDateTime")
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getEditDateTime() {
        return editDateTime;
    }

    @Override
    public void setEditDateTime(ZonedDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userEmail + this.objectId);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(this == o) {
            return true;
        }
        if(this.getClass() != o.getClass()) {
            return false;
        }
        Creature other = (Creature) o;
        return (this.userEmail.equals(other.userEmail)) && (this.objectId.equals(other.objectId));
    }
}
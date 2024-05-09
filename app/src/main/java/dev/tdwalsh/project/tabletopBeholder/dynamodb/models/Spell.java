package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import dev.tdwalsh.project.tabletopBeholder.converters.EffectConverter;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "TabletopBeholder_SpellTable")
public class Spell {
    private String userEmail;
    private String spellId;
    private String spellName;
    private String spellDescription;
    private String spellHigherLevel;
    private String spellRange;
    private String spellComponents;
    private String spellMaterial;
    private Boolean ritualCast;
    private Integer castingTime;
    private Integer spellLevel;
    private String spellSchool;
    private Integer innateCasts;
    private List<Effect> appliesEffects;

    @DynamoDBHashKey(attributeName = "userEmail")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @DynamoDBRangeKey(attributeName = "spellId")
    public String getSpellId() {
        return spellId;
    }

    public void setSpellId(String spellId) {
        this.spellId = spellId;
    }

    @DynamoDBAttribute(attributeName = "spellName")
    public String getSpellName() {
        return spellName;
    }

    public void setSpellName(String spellName) {
        this.spellName = spellName;
    }

    @DynamoDBAttribute(attributeName = "spellDescription")
    public String getSpellDescription() {
        return spellDescription;
    }

    public void setSpellDescription(String spellDescription) {
        this.spellDescription = spellDescription;
    }

    @DynamoDBAttribute(attributeName = "spellHigherLevel")
    public String getSpellHigherLevel() {
        return spellHigherLevel;
    }

    public void setSpellHigherLevel(String spellHigherLevel) {
        this.spellHigherLevel = spellHigherLevel;
    }

    @DynamoDBAttribute(attributeName = "spellRange")
    public String getSpellRange() {
        return spellRange;
    }

    public void setSpellRange(String spellRange) {
        this.spellRange = spellRange;
    }

    @DynamoDBAttribute(attributeName = "spellComponents")
    public String getSpellComponents() {
        return spellComponents;
    }

    public void setSpellComponents(String spellComponents) {
        this.spellComponents = spellComponents;
    }

    @DynamoDBAttribute(attributeName = "spellMaterial")
    public String getSpellMaterial() {
        return spellMaterial;
    }

    public void setSpellMaterial(String spellMaterial) {
        this.spellMaterial = spellMaterial;
    }

    @DynamoDBAttribute(attributeName = "ritualCast")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    public Boolean getRitualCast() {
        return ritualCast;
    }

    public void setRitualCast(Boolean ritualCast) {
        this.ritualCast = ritualCast;
    }

    @DynamoDBAttribute(attributeName = "castingTime")
    public Integer getCastingTime() {
        return castingTime;
    }

    public void setCastingTime(Integer castingTime) {
        this.castingTime = castingTime;
    }

    @DynamoDBAttribute(attributeName = "spellLevel")
    public Integer getSpellLevel() {
        return spellLevel;
    }

    public void setSpellLevel(Integer spellLevel) {
        this.spellLevel = spellLevel;
    }

    @DynamoDBAttribute(attributeName = "spellSchool")
    public String getSpellSchool() {
        return spellSchool;
    }

    public void setSpellSchool(String spellSchool) {
        this.spellSchool = spellSchool;
    }

    @DynamoDBAttribute(attributeName = "innateCasts")
    public Integer getInnateCasts() {
        return innateCasts;
    }

    public void setInnateCasts(Integer innateCasts) {
        this.innateCasts = innateCasts;
    }

    @DynamoDBAttribute(attributeName = "appliesEffects")
    @DynamoDBTypeConverted(converter = EffectConverter.class)
    public List<Effect> getAppliesEffects() {
        return appliesEffects;
    }

    public void setAppliesEffects(List<Effect> appliesEffects) {
        this.appliesEffects = appliesEffects;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userEmail + this.spellId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.getClass() == o.getClass()) {
            return true;
        }
        Spell other = (Spell) o;
        return (this.userEmail.equals(other.userEmail)) && (this.spellId.equals(other.spellId));
    }
}

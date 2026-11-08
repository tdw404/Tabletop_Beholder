package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import dev.tdwalsh.project.tabletopBeholder.converters.EffectConverter;
import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

@DynamoDbBean
public class Spell implements BeholderObject {
    public static final String TABLE_NAME = "TabletopBeholder_SpellTable";
    private String userEmail;
    private String objectId;
    private String objectName;
    private String spellDescription;
    private String spellHigherLevel;
    private String spellRange;
    private String spellComponents;
    private String spellMaterial;
    private String reaction;
    private Boolean ritualCast;
    private String castingTime;
    private Integer castingTurns;
    private Integer spellLevel;
    private String spellSchool;
    private Integer innateCasts;
    private Map<String, Effect> appliesEffects;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @Override
    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "SpellsSortByNameIndex")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    @DynamoDbSortKey
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    @DynamoDbSecondarySortKey(indexNames = "SpellsSortByNameIndex")
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = "".equals(objectName) ? null : WordUtils.capitalizeFully(objectName);
    }

    public String getSpellDescription() {
        return spellDescription;
    }

    public void setSpellDescription(String spellDescription) {
        this.spellDescription = spellDescription;
    }

    public String getSpellHigherLevel() {
        return spellHigherLevel;
    }

    public void setSpellHigherLevel(String spellHigherLevel) {
        this.spellHigherLevel = spellHigherLevel;
    }

    public String getSpellRange() {
        return spellRange;
    }

    public void setSpellRange(String spellRange) {
        this.spellRange = spellRange;
    }

    public String getSpellComponents() {
        return spellComponents;
    }

    public void setSpellComponents(String spellComponents) {
        this.spellComponents = spellComponents;
    }

    public String getSpellMaterial() {
        return spellMaterial;
    }

    public void setSpellMaterial(String spellMaterial) {
        this.spellMaterial = spellMaterial;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Boolean getRitualCast() {
        return ritualCast;
    }

    public void setRitualCast(Boolean ritualCast) {
        this.ritualCast = ritualCast;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public void setCastingTime(String castingTime) {
        this.castingTime = castingTime;
    }

    public Integer getCastingTurns() {
        return castingTurns;
    }

    public void setCastingTurns(Integer castingTurns) {
        this.castingTurns = castingTurns;
    }

    public Integer getSpellLevel() {
        return spellLevel;
    }

    public void setSpellLevel(Integer spellLevel) {
        this.spellLevel = spellLevel;
    }

    public String getSpellSchool() {
        return spellSchool;
    }

    public void setSpellSchool(String spellSchool) {
        this.spellSchool = spellSchool;
    }

    public Integer getInnateCasts() {
        return innateCasts;
    }

    public void setInnateCasts(Integer innateCasts) {
        this.innateCasts = innateCasts;
    }

    @DynamoDbConvertedBy(EffectConverter.class)
    public Map<String, Effect> getAppliesEffects() {
        return appliesEffects;
    }

    public void setAppliesEffects(Map<String, Effect> appliesEffects) {
        this.appliesEffects = appliesEffects;
    }
    @DynamoDbConvertedBy(ZonedDateTimeConverter.class)
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @DynamoDbConvertedBy(ZonedDateTimeConverter.class)
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
        return (this.userEmail.equals(other.userEmail)) && (this.objectId.equals(other.objectId));
    }
}

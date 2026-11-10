package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public class Action implements BeholderObject {
    private String userEmail;
    private String objectId;
    private String objectName;
    private String actionType;
    private String actionDescription;
    private Integer attackBonus;
    private String damageDice;
    private Integer damageModifier;
    private Integer uses;
    private Integer rechargeOn;
    private Map<String, Effect> appliesEffects;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(Integer attackBonus) {
        this.attackBonus = attackBonus;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getDamageDice() {
        return damageDice;
    }

    public void setDamageDice(String damageDice) {
        this.damageDice = damageDice;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer getDamageModifier() {
        return damageModifier;
    }

    public void setDamageModifier(Integer damageModifier) {
        this.damageModifier = damageModifier;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = WordUtils.capitalizeFully(objectName);
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public Integer getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = uses;
    }

    public Integer getRechargeOn() {
        return rechargeOn;
    }

    public void setRechargeOn(Integer rechargeOn) {
        this.rechargeOn = rechargeOn;
    }

    public Map<String, Effect> getAppliesEffects() {
        return appliesEffects;
    }

    public void setAppliesEffects(Map<String, Effect> appliesEffects) {
        this.appliesEffects = appliesEffects;
    }
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public ZonedDateTime getEditDateTime() {
        return editDateTime;
    }

    @Override
    public void setEditDateTime(ZonedDateTime editDateTime) {
        this.editDateTime = editDateTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.objectId);
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
        Action other = (Action) o;
        return this.objectId.equals(other.objectId);
    }
}

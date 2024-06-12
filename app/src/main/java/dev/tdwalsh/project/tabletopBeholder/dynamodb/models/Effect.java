package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import java.util.Objects;
import java.util.Set;

public class Effect {
    private String objectId;
    private String effectName;
    private Integer turnDuration;
    private String blameSource;
    private String blameCreatureId;
    private boolean blameConcentration;
    private String saveType;
    private Integer saveDC;
    private Set<String> saveOn;
    private Set<String> endOn;
    private Integer damageAmount;
    private String damageType;
    private Set<String> endDamageOn;

    public Integer getDamageAmount() {
        return damageAmount;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public Integer getTurnDuration() {
        return turnDuration;
    }

    public void setTurnDuration(Integer turnDuration) {
        this.turnDuration = turnDuration;
    }

    public String getBlameSource() {
        return blameSource;
    }

    public void setBlameSource(String blameSource) {
        this.blameSource = blameSource;
    }

    public String getBlameCreatureId() {
        return blameCreatureId;
    }

    public void setBlameCreatureId(String blameCreatureId) {
        this.blameCreatureId = blameCreatureId;
    }

    public boolean isBlameConcentration() {
        return blameConcentration;
    }

    public void setBlameConcentration(boolean blameConcentration) {
        this.blameConcentration = blameConcentration;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public Integer getSaveDC() {
        return saveDC;
    }

    public void setSaveDC(Integer saveDC) {
        this.saveDC = saveDC;
    }

    public Set<String> getSaveOn() {
        return saveOn;
    }

    public void setSaveOn(Set<String> saveOn) {
        this.saveOn = saveOn;
    }

    public Set<String> getEndOn() {
        return endOn;
    }

    public void setEndOn(Set<String> endOn) {
        this.endOn = endOn;
    }

    public void setDamageAmount(Integer damageAmount) {
        this.damageAmount = damageAmount;
    }

    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }

    public Set<String> getEndDamageOn() {
        return endDamageOn;
    }

    public void setEndDamageOn(Set<String> endDamageOn) {
        this.endDamageOn = endDamageOn;
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
        Effect other = (Effect) o;
        return this.objectId.equals(other.objectId);
    }
}

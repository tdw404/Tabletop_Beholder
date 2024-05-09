package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import java.util.List;
import java.util.Objects;

public class Effect {
    private String effectName;
    private Integer turnDuration;
    private String blameSource;
    private String blameCreatureId;
    private boolean blameConcentration;
    private String saveType;
    private Integer saveDC;
    private List<String> saveOn;
    private List<String> endOn;

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

    public List<String> getSaveOn() {
        return saveOn;
    }

    public void setSaveOn(List<String> saveOn) {
        this.saveOn = saveOn;
    }

    public List<String> getEndOn() {
        return endOn;
    }

    public void setEndOn(List<String> endOn) {
        this.endOn = endOn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.effectName + blameSource + blameCreatureId);
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
        Effect other = (Effect) o;
        return (this.effectName.equals(other.effectName)) && (this.blameSource.equals(other.blameSource)) && (this.blameCreatureId.equals(blameCreatureId));
    }
}

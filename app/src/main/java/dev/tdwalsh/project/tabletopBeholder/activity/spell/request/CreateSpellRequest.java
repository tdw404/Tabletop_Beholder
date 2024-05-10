package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;

import java.util.List;

public class CreateSpellRequest {
    private final String userEmail;
    private final String spellName;
    private final String spellDescription;
    private final String spellHigherLevel;
    private final String spellRange;
    private final String spellComponents;
    private final String spellMaterial;
    private final Boolean ritualCast;
    private final Integer castingTime;
    private final Integer spellLevel;
    private final String spellSchool;
    private final List<Effect> appliesEffects;

    private CreateSpellRequest(String userEmail,
                               String spellName,
                               String spellDescription,
                               String spellHigherLevel,
                               String spellRange,
                               String spellComponents,
                               String spellMaterial,
                               Boolean ritualCast,
                               Integer castingTime,
                               Integer spellLevel,
                               String spellSchool,
                               List<Effect> appliesEffects) {
        this.userEmail = userEmail;
        this.spellName = spellName;
        this.spellDescription = spellDescription;
        this.spellHigherLevel = spellHigherLevel;
        this.spellRange = spellRange;
        this.spellComponents = spellComponents;
        this.spellMaterial = spellMaterial;
        this.ritualCast = ritualCast;
        this.castingTime = castingTime;
        this.spellLevel = spellLevel;
        this.spellSchool = spellSchool;
        this.appliesEffects = appliesEffects;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getSpellName() { return this.spellName; }

    public String getSpellDescription() {
        return spellDescription;
    }

    public String getSpellHigherLevel() {
        return spellHigherLevel;
    }

    public String getSpellRange() {
        return spellRange;
    }

    public String getSpellComponents() {
        return spellComponents;
    }

    public String getSpellMaterial() {
        return spellMaterial;
    }

    public Boolean getRitualCast() {
        return ritualCast;
    }

    public Integer getCastingTime() {
        return castingTime;
    }

    public Integer getSpellLevel() {
        return spellLevel;
    }

    public String getSpellSchool() {
        return spellSchool;
    }

    public List<Effect> getAppliesEffects() {
        return appliesEffects;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;
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
        private List<Effect> appliesEffects;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withSpellName(String spellName) {
            this.spellName = spellName;
            return this;
        }
        public Builder withSpellDescription(String spellDescription) {
            this.spellDescription = spellDescription;
            return this;
        }
        public Builder withSpellHigherLevel(String spellHigherLevel) {
            this.spellHigherLevel = spellHigherLevel;
            return this;
        }
        public Builder withSpellRange(String spellRange) {
            this.spellRange = spellRange;
            return this;
        }
        public Builder withSpellComponents(String spellComponents) {
            this.spellComponents = spellComponents;
            return this;
        }
        public Builder withSpellMaterial(String spellMaterial) {
            this.spellMaterial = spellMaterial;
            return this;
        }
        public Builder withRitualCast(Boolean ritualCast) {
            this.ritualCast = ritualCast;
            return this;
        }
        public Builder withCastingTime(Integer castingTime) {
            this.castingTime = castingTime;
            return this;
        }public Builder withSpellLevel(Integer spellLevel) {
            this.spellLevel = spellLevel;
            return this;
        }
        public Builder withSpellSchool(String spellSchool) {
            this.spellSchool = spellSchool;
            return this;
        }public Builder withAppliesEffects(List<Effect> appliesEffects) {
            this.appliesEffects = appliesEffects;
            return this;
        }

        public CreateSpellRequest build() {
            return new CreateSpellRequest(userEmail,
                                            spellName,
                                            spellDescription,
                                            spellHigherLevel,
                                            spellRange,
                                            spellComponents,
                                            spellMaterial,
                                            ritualCast,
                                            castingTime,
                                            spellLevel,
                                            spellSchool,
                                            appliesEffects);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import java.util.List;

@JsonDeserialize(builder = UpdateSpellRequest.Builder.class)
public class UpdateSpellRequest {
    private final Spell spell;
//    private final String userEmail;
//    private final String objectId;
//    private final String objectName;
//    private final String spellDescription;
//    private final String spellHigherLevel;
//    private final String spellRange;
//    private final String spellComponents;
//    private final String spellMaterial;
//    private final Boolean ritualCast;
//    private final Integer castingTime;
//    private final Integer spellLevel;
//    private final String spellSchool;
//    private final List<Effect> appliesEffects;


    private UpdateSpellRequest(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return this.spell;
    }
//    private UpdateSpellRequest(String userEmail,
//                               String objectId,
//                               String objectName,
//                               String spellDescription,
//                               String spellHigherLevel,
//                               String spellRange,
//                               String spellComponents,
//                               String spellMaterial,
//                               Boolean ritualCast,
//                               Integer castingTime,
//                               Integer spellLevel,
//                               String spellSchool,
//                               List<Effect> appliesEffects) {
//        this.userEmail = userEmail;
//        this.objectId = objectId;
//        this.objectName = objectName;
//        this.spellDescription = spellDescription;
//        this.spellHigherLevel = spellHigherLevel;
//        this.spellRange = spellRange;
//        this.spellComponents = spellComponents;
//        this.spellMaterial = spellMaterial;
//        this.ritualCast = ritualCast;
//        this.castingTime = castingTime;
//        this.spellLevel = spellLevel;
//        this.spellSchool = spellSchool;
//        this.appliesEffects = appliesEffects;
//    }
//
//    public String getUserEmail() {
//        return this.userEmail;
//    }
//
//    public String getObjectId() { return this.objectId; }
//
//    public String getObjectName() { return this.objectName; }
//
//    public String getSpellDescription() {
//        return spellDescription;
//    }
//
//    public String getSpellHigherLevel() {
//        return spellHigherLevel;
//    }
//
//    public String getSpellRange() {
//        return spellRange;
//    }
//
//    public String getSpellComponents() {
//        return spellComponents;
//    }
//
//    public String getSpellMaterial() {
//        return spellMaterial;
//    }
//
//    public Boolean getRitualCast() {
//        return ritualCast;
//    }
//
//    public Integer getCastingTime() {
//        return castingTime;
//    }
//
//    public Integer getSpellLevel() {
//        return spellLevel;
//    }
//
//    public String getSpellSchool() {
//        return spellSchool;
//    }
//
//    public List<Effect> getAppliesEffects() {
//        return appliesEffects;
//    }

    public static Builder builder() {
        return new Builder();
    }
//    public static class Builder {
//        private String userEmail;
//        private String objectId;
//        private String objectName;
//        private String spellDescription;
//        private String spellHigherLevel;
//        private String spellRange;
//        private String spellComponents;
//        private String spellMaterial;
//        private Boolean ritualCast;
//        private Integer castingTime;
//        private Integer spellLevel;
//        private String spellSchool;
//        private List<Effect> appliesEffects;
//
//        public Builder withUserEmail(String userEmail) {
//            this.userEmail = userEmail;
//            return this;
//        }
//
//        public Builder withObjectId(String objectId) {
//            this.objectId = objectId;
//            return this;
//        }
//
//        public Builder withObjectName(String objectName) {
//            this.objectName = objectName;
//            return this;
//        }
//        public Builder withSpellDescription(String spellDescription) {
//            this.spellDescription = spellDescription;
//            return this;
//        }
//        public Builder withSpellHigherLevel(String spellHigherLevel) {
//            this.spellHigherLevel = spellHigherLevel;
//            return this;
//        }
//        public Builder withSpellRange(String spellRange) {
//            this.spellRange = spellRange;
//            return this;
//        }
//        public Builder withSpellComponents(String spellComponents) {
//            this.spellComponents = spellComponents;
//            return this;
//        }
//        public Builder withSpellMaterial(String spellMaterial) {
//            this.spellMaterial = spellMaterial;
//            return this;
//        }
//        public Builder withRitualCast(Boolean ritualCast) {
//            this.ritualCast = ritualCast;
//            return this;
//        }
//        public Builder withCastingTime(Integer castingTime) {
//            this.castingTime = castingTime;
//            return this;
//        }public Builder withSpellLevel(Integer spellLevel) {
//            this.spellLevel = spellLevel;
//            return this;
//        }
//        public Builder withSpellSchool(String spellSchool) {
//            this.spellSchool = spellSchool;
//            return this;
//        }public Builder withAppliesEffects(List<Effect> appliesEffects) {
//            this.appliesEffects = appliesEffects;
//            return this;
//        }
//
//        public UpdateSpellRequest build() {
//            return new UpdateSpellRequest(userEmail,
//                                            objectId,
//                                            objectName,
//                                            spellDescription,
//                                            spellHigherLevel,
//                                            spellRange,
//                                            spellComponents,
//                                            spellMaterial,
//                                            ritualCast,
//                                            castingTime,
//                                            spellLevel,
//                                            spellSchool,
//                                            appliesEffects);
//        }
//    }
public static class Builder {
        private Spell spell;

        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        public UpdateSpellRequest build() {
            return new UpdateSpellRequest(spell);
        }
}

}

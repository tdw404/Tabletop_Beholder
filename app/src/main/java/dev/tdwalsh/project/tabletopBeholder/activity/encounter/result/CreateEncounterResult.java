package dev.tdwalsh.project.tabletopBeholder.activity.encounter.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

public class CreateEncounterResult {
    private final Encounter encounter;

    private CreateEncounterResult(Encounter encounter) {
        this.encounter = encounter;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Encounter encounter;

        /**
         * Builder settter.
         * @param encounter varible to set
         * @return builder
         */
        public Builder withEncounter(Encounter encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public CreateEncounterResult build() {
            return new CreateEncounterResult(encounter);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

public class RunEncounterResult {
    private final Encounter encounter;

    private RunEncounterResult(Encounter encounter) {
        this.encounter = encounter;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    /**
     * Builder.
     * @return - Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Encounter encounter;

        /**
         * Builder setter.
         * @param encounter - To set.
         * @return - Return builder.
         */
        public Builder withEncounter(Encounter encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public RunEncounterResult build() {
            return new RunEncounterResult(encounter);
        }
    }
}

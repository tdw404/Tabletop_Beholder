package dev.tdwalsh.project.tabletopBeholder.activity.encounter.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

public class GetEncounterResult {
    private final Encounter encounter;

    private GetEncounterResult(Encounter encounter) {
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
         * Builder setter.
         * @param encounter variable to set
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
        public GetEncounterResult build() {
            return new GetEncounterResult(encounter);
        }
    }
}

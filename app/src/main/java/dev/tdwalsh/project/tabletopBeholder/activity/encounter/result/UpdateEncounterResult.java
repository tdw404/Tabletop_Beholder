package dev.tdwalsh.project.tabletopBeholder.activity.encounter.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

public class UpdateEncounterResult {
    private final Encounter encounter;

    private UpdateEncounterResult(Encounter encounter) {
        this.encounter = encounter;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Encounter encounter;

        public Builder withEncounter(Encounter encounter) {
            this.encounter = encounter;
            return this;
        }

        public UpdateEncounterResult build() {
            return new UpdateEncounterResult(encounter);
        }
    }
}

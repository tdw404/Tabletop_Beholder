package dev.tdwalsh.project.tabletopBeholder.activity.encounter.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import java.util.List;

public class GetAllEncountersResult {
    private final List<Encounter> encounterList;

    private GetAllEncountersResult(List<Encounter> encounterList) {
        this.encounterList = encounterList;
    }

    public List<Encounter> getEncounterList() {
        return this.encounterList;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Encounter> encounterList;

        /**
         * Builder setter.
         * @param encounterList variable to set
         * @return builder
         */
        public Builder withEncounterList(List<Encounter> encounterList) {
            this.encounterList = encounterList;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public GetAllEncountersResult build() {
            return new GetAllEncountersResult(encounterList);
        }
    }
}

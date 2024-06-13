package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.EncounterName;

import java.util.List;

public class GetEncounterListResult {
    private final List<EncounterName> encounterNameList;

    private GetEncounterListResult(List<EncounterName> encounterNameList) {
        this.encounterNameList = encounterNameList;
    }

    public List<EncounterName> getEncounterNameList() {
        return encounterNameList;
    }

    /**
     * Builder.
     * @return - Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<EncounterName> encounterNameList;

        /**
         * Builder setter.
         * @param encounterNameList - To set.
         * @return - Return builder.
         */
        public Builder withEncounterNameList(List<EncounterName> encounterNameList) {
            this.encounterNameList = encounterNameList;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public GetEncounterListResult build() {
            return new GetEncounterListResult(encounterNameList);
        }
    }
}

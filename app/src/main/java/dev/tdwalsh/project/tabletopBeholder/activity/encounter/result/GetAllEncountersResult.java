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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Encounter> encounterList;

        public Builder withEncounterList(List<Encounter> encounterList) {
            this.encounterList = encounterList;
            return this;
        }

        public GetAllEncountersResult build() {
            return new GetAllEncountersResult(encounterList);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.encounter.result;

public class DeleteEncounterResult {

    private DeleteEncounterResult() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        public DeleteEncounterResult build() {
            return new DeleteEncounterResult();
        }
    }
}

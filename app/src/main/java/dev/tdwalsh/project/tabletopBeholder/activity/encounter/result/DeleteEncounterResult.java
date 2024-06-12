package dev.tdwalsh.project.tabletopBeholder.activity.encounter.result;

public class DeleteEncounterResult {

    private DeleteEncounterResult() {
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * Builder.
         * @return builder
         */
        public DeleteEncounterResult build() {
            return new DeleteEncounterResult();
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.creature.result;

public class DeleteCreatureResult {

    private DeleteCreatureResult() {
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
        public DeleteCreatureResult build() {
            return new DeleteCreatureResult();
        }
    }
}

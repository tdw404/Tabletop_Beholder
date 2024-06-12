package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

public class DeleteSpellResult {

    private DeleteSpellResult() {
    }

    /**
     * Builder.
     * @return - Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        /**
         * Builder.
         * @return - Builder.
         */
        public DeleteSpellResult build() {
            return new DeleteSpellResult();
        }
    }
}

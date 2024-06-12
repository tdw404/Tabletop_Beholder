package dev.tdwalsh.project.tabletopBeholder.activity.session.result;

public class DeleteSessionResult {
    private DeleteSessionResult() { }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public static class Builder {

        /**
         * Builder.
         * @return - Builder.
         */
        public DeleteSessionResult build() {
            return new DeleteSessionResult();
        }
    }
}

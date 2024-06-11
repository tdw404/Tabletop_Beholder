package dev.tdwalsh.project.tabletopBeholder.activity.session.result;

public class DeleteSessionResult {

    private DeleteSessionResult() {
    }

    /**
     * Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder
     */
    public static class Builder {


        public DeleteSessionResult build() {
            return new DeleteSessionResult();
        }
    }
}

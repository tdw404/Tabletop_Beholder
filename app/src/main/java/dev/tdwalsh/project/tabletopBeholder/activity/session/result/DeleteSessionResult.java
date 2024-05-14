package dev.tdwalsh.project.tabletopBeholder.activity.session.result;

public class DeleteSessionResult {

    private DeleteSessionResult() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        public DeleteSessionResult build() {
            return new DeleteSessionResult();
        }
    }
}

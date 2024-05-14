package dev.tdwalsh.project.tabletopBeholder.activity.action.result;

public class DeleteActionResult {

    private DeleteActionResult() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        public DeleteActionResult build() {
            return new DeleteActionResult();
        }
    }
}

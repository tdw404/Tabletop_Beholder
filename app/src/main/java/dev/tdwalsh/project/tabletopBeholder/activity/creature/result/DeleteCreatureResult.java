package dev.tdwalsh.project.tabletopBeholder.activity.creature.result;

public class DeleteCreatureResult {

    private DeleteCreatureResult() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        public DeleteCreatureResult build() {
            return new DeleteCreatureResult();
        }
    }
}

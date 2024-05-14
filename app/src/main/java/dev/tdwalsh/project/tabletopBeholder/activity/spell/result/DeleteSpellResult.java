package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

public class DeleteSpellResult {

    private DeleteSpellResult() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        public DeleteSpellResult build() {
            return new DeleteSpellResult();
        }
    }
}

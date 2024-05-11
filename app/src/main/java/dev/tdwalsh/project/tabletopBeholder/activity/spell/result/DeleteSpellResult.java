package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

public class DeleteSpellResult {
    //private final Spell spell;

    private DeleteSpellResult() {
    }

    //public Spell getSpell() {
    //    return spell;
    //}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {


        public DeleteSpellResult build() {
            return new DeleteSpellResult();
        }
    }
}

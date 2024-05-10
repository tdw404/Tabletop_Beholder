package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

public class GetSpellResult {
    private final Spell spell;

    private GetSpellResult(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Spell spell;

        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        public GetSpellResult build() {
            return new GetSpellResult(spell);
        }
    }
}

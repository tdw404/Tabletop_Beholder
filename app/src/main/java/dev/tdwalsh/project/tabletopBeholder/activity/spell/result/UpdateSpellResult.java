package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

public class UpdateSpellResult {
    private final Spell spell;

    private UpdateSpellResult(Spell spell) {
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

        public UpdateSpellResult build() {
            return new UpdateSpellResult(spell);
        }
    }
}

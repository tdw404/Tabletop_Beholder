package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

public class CreateSpellResult {
    private final Spell spell;

    private CreateSpellResult(Spell spell) {
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

        public CreateSpellResult build() {
            return new CreateSpellResult(spell);
        }
    }
}

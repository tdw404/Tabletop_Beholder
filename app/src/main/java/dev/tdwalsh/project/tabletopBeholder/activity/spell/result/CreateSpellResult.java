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

    /**
     * Builder.
     * @return - Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Spell spell;

        /**
         * Builder setter.
         * @param spell - object to set.
         * @return Builder
         */
        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public CreateSpellResult build() {
            return new CreateSpellResult(spell);
        }
    }
}

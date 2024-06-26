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

    /**
     * Builder.
     * @return - Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Spell spell;

        /**
         * Builder setter.
         * @param spell - To set.
         * @return - Return builder.
         */
        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public GetSpellResult build() {
            return new GetSpellResult(spell);
        }
    }
}

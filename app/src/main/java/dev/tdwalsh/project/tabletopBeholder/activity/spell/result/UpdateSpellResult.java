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

    /**
     * Builder constructor.
     * @return Builder.
     */
    public static Builder builder() {
        return new Builder();
    }
    /**
     * Builder constructor.
     */
    public static class Builder {
        private Spell spell;

        /**
         * Builder setter.
         * @param spell - Set value.
         * @return - Builder.
         */
        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        /**
         * Builder.
         * @return UpdateSpellResult
         */
        public UpdateSpellResult build() {
            return new UpdateSpellResult(spell);
        }
    }
}

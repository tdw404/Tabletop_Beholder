package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

public class CreateTemplateSpellResult {
    private final Spell spell;

    private CreateTemplateSpellResult(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Spell spell;

        /**
         * Builder setter.
         * @param spell variable to set
         * @return builder
         */
        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public CreateTemplateSpellResult build() {
            return new CreateTemplateSpellResult(spell);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

public class GetTemplateSpellResult {
    private final TemplateSpell templateSpell;

    private GetTemplateSpellResult(TemplateSpell templateSpell) {
        this.templateSpell = templateSpell;
    }

    public TemplateSpell getTemplateSpell() {
        return templateSpell;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TemplateSpell templateSpell;

        /**
         * Builder
         * @param templateSpell variable to set
         * @return builder
         */
        public Builder withTemplateSpell(TemplateSpell templateSpell) {
            this.templateSpell = templateSpell;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public GetTemplateSpellResult build() {
            return new GetTemplateSpellResult(templateSpell);
        }
    }
}

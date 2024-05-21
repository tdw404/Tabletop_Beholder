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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TemplateSpell templateSpell;

        public Builder withTemplateSpell(TemplateSpell templateSpell) {
            this.templateSpell = templateSpell;
            return this;
        }

        public GetTemplateSpellResult build() {
            return new GetTemplateSpellResult(templateSpell);
        }
    }
}

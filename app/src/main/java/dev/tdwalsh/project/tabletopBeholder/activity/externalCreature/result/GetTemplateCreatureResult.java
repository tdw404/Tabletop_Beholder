package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

public class GetTemplateCreatureResult {
    private final TemplateCreature templateCreature;

    private GetTemplateCreatureResult(TemplateCreature templateCreature) {
        this.templateCreature = templateCreature;
    }

    public TemplateCreature getTemplateCreature() {
        return templateCreature;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TemplateCreature templateCreature;

        /**
         * Builder setter.
         * @param templateCreature variable to set
         * @return builder
         */
        public Builder withTemplateCreature(TemplateCreature templateCreature) {
            this.templateCreature = templateCreature;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public GetTemplateCreatureResult build() {
            return new GetTemplateCreatureResult(templateCreature);
        }
    }
}

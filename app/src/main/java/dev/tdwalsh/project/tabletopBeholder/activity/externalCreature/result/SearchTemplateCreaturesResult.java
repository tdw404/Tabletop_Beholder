package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import java.util.List;

public class SearchTemplateCreaturesResult {
    private final int count;
    private final List<TemplateCreature> templateCreatureList;

    private SearchTemplateCreaturesResult(int count, List<TemplateCreature> templateCreatureList) {
        this.count = count;
        this.templateCreatureList = templateCreatureList;
    }

    public int getCount() {
        return this.count;
    }

    public List<TemplateCreature> getTemplateCreatureList() {
        return this.templateCreatureList;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<TemplateCreature> templateCreatureList;

        /**
         * Builder setter.
         * @param templateCreatureList variable to set
         * @return builder
         */
        public Builder withTemplateCreatureList(List<TemplateCreature> templateCreatureList) {
            this.templateCreatureList = templateCreatureList;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public SearchTemplateCreaturesResult build() {
            return new SearchTemplateCreaturesResult(templateCreatureList.size(), templateCreatureList);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.List;

public class SearchTemplateSpellsResult {
    private final int count;
    private final List<TemplateSpell> templateSpellList;

    private SearchTemplateSpellsResult(int count, List<TemplateSpell> templateSpellList) {
        this.count = count;
        this.templateSpellList = templateSpellList;
    }

    public int getCount() {
        return this.count;
    }

    public List<TemplateSpell> getTemplateSpellList() {
        return this.templateSpellList;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<TemplateSpell> templateSpellList;

        /**
         * Builder setter.
         * @param templateSpellList variable to set
         * @return builder
         */
        public Builder withTemplateSpellList(List<TemplateSpell> templateSpellList) {
            this.templateSpellList = templateSpellList;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public SearchTemplateSpellsResult build() {
            return new SearchTemplateSpellsResult(templateSpellList.size(), templateSpellList);
        }
    }
}

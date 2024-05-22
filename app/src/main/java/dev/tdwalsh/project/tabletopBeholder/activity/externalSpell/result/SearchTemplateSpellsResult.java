package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.result;

import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import java.util.List;

public class SearchTemplateSpellsResult {
    private final List<TemplateSpell> templateSpellList;

    private SearchTemplateSpellsResult(List<TemplateSpell> templateSpellList) {
        this.templateSpellList = templateSpellList;
    }

    public List<TemplateSpell> getTemplateSpellList() {
        return this.templateSpellList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<TemplateSpell> templateSpellList;

        public Builder withTemplateSpellList(List<TemplateSpell> templateSpellList) {
            this.templateSpellList = templateSpellList;
            return this;
        }

        public SearchTemplateSpellsResult build() {
            return new SearchTemplateSpellsResult(templateSpellList);
        }
    }
}

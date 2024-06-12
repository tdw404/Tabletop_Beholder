package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import java.util.List;

public class GetAllSpellsResult {
    private final List<Spell> spellList;

    private GetAllSpellsResult(List<Spell> spellList) {
        this.spellList = spellList;
    }

    public List<Spell> getSpellList() {
        return this.spellList;
    }

    /**
     * Builder.
     * @return - Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Spell> spellList;

        /**
         * Builder setter.
         * @param spellList - To set.
         * @return Builder.
         */
        public Builder withSpellList(List<Spell> spellList) {
            this.spellList = spellList;
            return this;
        }

        /**
         * Builder method.
         * @return GetAllSpellsResult
         */
        public GetAllSpellsResult build() {
            return new GetAllSpellsResult(spellList);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.spell.result;

import com.amazonaws.services.dynamodbv2.xspec.S;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Spell> spellList;

        public Builder withSpellList(List<Spell> spellList) {
            this.spellList = spellList;
            return this;
        }

        public GetAllSpellsResult build() {
            return new GetAllSpellsResult(spellList);
        }
    }
}

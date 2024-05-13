package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Effect;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import java.util.List;

@JsonDeserialize(builder = CreateSpellRequest.Builder.class)
public class CreateSpellRequest {
    private final Spell spell;

    private CreateSpellRequest(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Spell spell;

        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }
        public CreateSpellRequest build() {
            return new CreateSpellRequest(spell);
        }
    }
}

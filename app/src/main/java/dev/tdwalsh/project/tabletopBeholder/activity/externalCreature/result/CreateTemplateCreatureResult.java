package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

public class CreateTemplateCreatureResult {
    private final Creature creature;

    private CreateTemplateCreatureResult(Creature creature) {
        this.creature = creature;
    }

    public Creature getCreature() {
        return creature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Creature creature;

        public Builder withCreature(Creature creature) {
            this.creature = creature;
            return this;
        }

        public CreateTemplateCreatureResult build() {
            return new CreateTemplateCreatureResult(creature);
        }
    }
}

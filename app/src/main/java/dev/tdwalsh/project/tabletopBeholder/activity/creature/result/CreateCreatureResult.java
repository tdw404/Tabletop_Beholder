package dev.tdwalsh.project.tabletopBeholder.activity.creature.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

public class CreateCreatureResult {
    private final Creature creature;

    private CreateCreatureResult(Creature creature) {
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

        public CreateCreatureResult build() {
            return new CreateCreatureResult(creature);
        }
    }
}

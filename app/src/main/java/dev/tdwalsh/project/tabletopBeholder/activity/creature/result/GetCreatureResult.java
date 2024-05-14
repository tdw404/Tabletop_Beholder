package dev.tdwalsh.project.tabletopBeholder.activity.creature.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

public class GetCreatureResult {
    private final Creature creature;

    private GetCreatureResult(Creature creature) {
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

        public GetCreatureResult build() {
            return new GetCreatureResult(creature);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.creature.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

public class UpdateCreatureResult {
    private final Creature creature;

    private UpdateCreatureResult(Creature creature) {
        this.creature = creature;
    }

    public Creature getCreature() {
        return creature;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Creature creature;

        /**
         * Builder setter.
         * @param creature variable to set
         * @return builder
         */
        public Builder withCreature(Creature creature) {
            this.creature = creature;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public UpdateCreatureResult build() {
            return new UpdateCreatureResult(creature);
        }
    }
}

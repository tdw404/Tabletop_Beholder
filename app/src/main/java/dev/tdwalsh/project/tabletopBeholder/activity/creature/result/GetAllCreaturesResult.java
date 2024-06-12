package dev.tdwalsh.project.tabletopBeholder.activity.creature.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import java.util.List;

public class GetAllCreaturesResult {
    private final List<Creature> creatureList;

    private GetAllCreaturesResult(List<Creature> creatureList) {
        this.creatureList = creatureList;
    }

    public List<Creature> getCreatureList() {
        return this.creatureList;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Creature> creatureList;

        /**
         * Builder setter.
         * @param creatureList variable to set
         * @return builder
         */
        public Builder withCreatureList(List<Creature> creatureList) {
            this.creatureList = creatureList;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public GetAllCreaturesResult build() {
            return new GetAllCreaturesResult(creatureList);
        }
    }
}

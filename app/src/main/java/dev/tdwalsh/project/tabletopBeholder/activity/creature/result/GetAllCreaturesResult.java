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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Creature> creatureList;

        public Builder withCreatureList(List<Creature> creatureList) {
            this.creatureList = creatureList;
            return this;
        }

        public GetAllCreaturesResult build() {
            return new GetAllCreaturesResult(creatureList);
        }
    }
}

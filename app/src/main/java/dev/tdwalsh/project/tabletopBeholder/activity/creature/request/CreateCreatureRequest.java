package dev.tdwalsh.project.tabletopBeholder.activity.creature.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

@JsonDeserialize(builder = CreateCreatureRequest.Builder.class)
public class CreateCreatureRequest {
    private final Creature creature;
    private final String userEmail;

    private CreateCreatureRequest(Creature creature, String userEmail) {

        this.creature = creature;
        this.userEmail = userEmail;
    }

    public Creature getCreature() {
        return this.creature;
    }

    public String getUserEmail() { return this.userEmail; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Creature creature;
        private String userEmail;

        public Builder withCreature(Creature creature) {
            this.creature = creature;
            return this;
        }

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public CreateCreatureRequest build() {
            return new CreateCreatureRequest(creature, userEmail);
        }
    }
}

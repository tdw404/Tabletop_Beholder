package dev.tdwalsh.project.tabletopBeholder.activity.creature.request;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = UpdateCreatureRequest.Builder.class)
public class UpdateCreatureRequest {
    private final Creature creature;
    private final String userEmail;

    private UpdateCreatureRequest(Creature creature, String userEmail) {
        this.creature = creature;
        this.userEmail = userEmail;
    }

    public Creature getCreature() {
        return this.creature;
    }

    public String getUserEmail() {
        return this.userEmail; }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private Creature creature;
        private String userEmail;

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
         * Builder setter.
         * @param userEmail variable to set
         * @return builder
         */
        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public UpdateCreatureRequest build() {
            return new UpdateCreatureRequest(creature, userEmail);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.creature.request;

public class DeleteCreatureRequest {
    private final String userEmail;
    private final String objectId;

    private DeleteCreatureRequest(String userEmail, String objectId) {
        this.userEmail = userEmail;
        this.objectId = objectId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getObjectId() {
        return this.objectId;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;
        private String objectId;

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
         * Builder setter.
         * @param objectId variable to set
         * @return builder
         */
        public Builder withObjectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public DeleteCreatureRequest build() {
            return new DeleteCreatureRequest(userEmail, objectId);
        }
    }
}

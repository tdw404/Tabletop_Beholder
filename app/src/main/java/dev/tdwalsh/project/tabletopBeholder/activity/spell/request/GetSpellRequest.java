package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

public class GetSpellRequest {
    private final String userEmail;
    private final String objectId;

    private GetSpellRequest(String userEmail, String objectId) {
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
         * @param userEmail - Variable to set.
         * @return - Builder
         */
        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        /**
         * Builder setter.
         * @param objectId  - Variable to set.
         * @return - Builder.
         */
        public Builder withObjectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public GetSpellRequest build() {
            return new GetSpellRequest(userEmail, objectId);
        }
    }
}

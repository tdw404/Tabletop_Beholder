package dev.tdwalsh.project.tabletopBeholder.activity.encounter.request;

public class GetEncounterRequest {
    private final String userEmail;
    private final String objectId;

    private GetEncounterRequest(String userEmail, String objectId) {
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
        public GetEncounterRequest build() {
            return new GetEncounterRequest(userEmail, objectId);
        }
    }
}

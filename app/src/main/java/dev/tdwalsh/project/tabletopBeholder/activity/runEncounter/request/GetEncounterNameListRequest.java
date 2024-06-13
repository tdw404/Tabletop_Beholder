package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request;

public class GetEncounterNameListRequest {
    private final String userEmail;
    private final String sessionId;

    private GetEncounterNameListRequest(String userEmail, String sessionId) {
        this.userEmail = userEmail;
        this.sessionId = sessionId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getObjectId() {
        return this.sessionId;
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
        private String sessionId;

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
         * @param sessionId  - Variable to set.
         * @return - Builder.
         */
        public Builder withObjectId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public GetEncounterNameListRequest build() {
            return new GetEncounterNameListRequest(userEmail, sessionId);
        }
    }
}

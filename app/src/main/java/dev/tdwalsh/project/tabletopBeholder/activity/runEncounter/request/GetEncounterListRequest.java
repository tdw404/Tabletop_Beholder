package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request;

public class GetEncounterListRequest {
    private final String userEmail;
    private final String sessionId;

    private GetEncounterListRequest(String userEmail, String sessionId) {
        this.userEmail = userEmail;
        this.sessionId = sessionId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getSessionId() {
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
        public Builder withSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public GetEncounterListRequest build() {
            return new GetEncounterListRequest(userEmail, sessionId);
        }
    }
}

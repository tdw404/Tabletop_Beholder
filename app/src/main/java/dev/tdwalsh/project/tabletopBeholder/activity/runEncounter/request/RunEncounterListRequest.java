package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request;

public class RunEncounterListRequest {
    private final String userEmail;
    private final String encounterId;
    private final String activity;
    private final String initiativeList;

    private RunEncounterListRequest(String userEmail, String encounterId,
                                    String activity, String initiativeList) {
        this.userEmail = userEmail;
        this.encounterId = encounterId;
        this.activity = activity;
        this.initiativeList = initiativeList;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getEncounterId() {
        return this.encounterId;
    }

    public String getActivity() {
        return this.activity;
    }

    public String getInitiativeList() {
        return this.initiativeList;
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
        private String encounterId;
        private String activity;
        private String initiativeList;

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
         * @param encounterId  - Variable to set.
         * @return - Builder.
         */
        public Builder withEncounterId(String encounterId) {
            this.encounterId = encounterId;
            return this;
        }

        /**
         * Builder setter.
         * @param activity  - Variable to set.
         * @return - Builder.
         */
        public Builder withActivity(String activity) {
            this.activity = activity;
            return this;
        }

        /**
         * Builder setter.
         * @param initiativeList  - Variable to set.
         * @return - Builder.
         */
        public Builder withInitiativeList(String initiativeList) {
            this.initiativeList = initiativeList;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public RunEncounterListRequest build() {
            return new RunEncounterListRequest(userEmail, encounterId, activity, initiativeList);
        }
    }
}

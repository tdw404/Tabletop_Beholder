package dev.tdwalsh.project.tabletopBeholder.activity.runEncounter.request;

import org.json.JSONObject;

public class RunEncounterRequest {
    private final String userEmail;
    private final String encounterId;
    private final String activity;
    private final JSONObject body;

    private RunEncounterRequest(String userEmail, String encounterId,
                                String activity, JSONObject body) {
        this.userEmail = userEmail;
        this.encounterId = encounterId;
        this.activity = activity;
        this.body = body;
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

    public JSONObject getBody() {
        return this.body;
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
        private JSONObject body;

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
         * @param body  - Variable to set.
         * @return - Builder.
         */
        public Builder withBody(JSONObject body) {
            this.body = body;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public RunEncounterRequest build() {
            return new RunEncounterRequest(userEmail, encounterId, activity, body);
        }
    }
}

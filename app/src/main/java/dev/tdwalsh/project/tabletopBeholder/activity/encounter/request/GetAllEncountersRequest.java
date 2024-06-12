package dev.tdwalsh.project.tabletopBeholder.activity.encounter.request;

public class GetAllEncountersRequest {
    private final String userEmail;

    private GetAllEncountersRequest(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return this.userEmail;
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
        public GetAllEncountersRequest build() {
            return new GetAllEncountersRequest(userEmail);
        }
    }
}

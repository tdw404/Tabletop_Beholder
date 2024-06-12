package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

public class GetAllSpellsRequest {
    private final String userEmail;

    private GetAllSpellsRequest(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    /**
     * Builder.
     * @return - Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;

        /**
         * Builder setter.
         * @param userEmail - Variable to set.
         * @return - Builder.
         */
        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        /**
         * Builder.
         * @return - Builder.
         */
        public GetAllSpellsRequest build() {
            return new GetAllSpellsRequest(userEmail);
        }
    }
}

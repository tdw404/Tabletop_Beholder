package dev.tdwalsh.project.tabletopBeholder.activity.creature.request;

public class GetAllCreaturesRequest {
    private final String userEmail;

    private GetAllCreaturesRequest(String userEmail) {
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
        public GetAllCreaturesRequest build() {
            return new GetAllCreaturesRequest(userEmail);
        }
    }
}

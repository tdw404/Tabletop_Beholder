package dev.tdwalsh.project.tabletopBeholder.activity.action.request;

public class GetAllActionsRequest {
    private final String userEmail;

    private GetAllActionsRequest(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public GetAllActionsRequest build() {
            return new GetAllActionsRequest(userEmail);
        }
    }
}

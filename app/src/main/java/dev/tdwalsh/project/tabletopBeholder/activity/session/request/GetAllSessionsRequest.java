package dev.tdwalsh.project.tabletopBeholder.activity.session.request;

public class GetAllSessionsRequest {
    private final String userEmail;

    private GetAllSessionsRequest(String userEmail) {
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

        public GetAllSessionsRequest build() {
            return new GetAllSessionsRequest(userEmail);
        }
    }
}

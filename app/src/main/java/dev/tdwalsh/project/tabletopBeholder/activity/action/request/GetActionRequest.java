package dev.tdwalsh.project.tabletopBeholder.activity.action.request;

public class GetActionRequest {
    private final String userEmail;
    private final String actionId;

    private GetActionRequest(String userEmail, String actionId) {
        this.userEmail = userEmail;
        this.actionId = actionId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getActionId() {
        return this.actionId;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;
        private String actionId;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withActionId(String actionId) {
            this.actionId = actionId;
            return this;
        }

        public GetActionRequest build() {
            return new GetActionRequest(userEmail, actionId);
        }
    }
}

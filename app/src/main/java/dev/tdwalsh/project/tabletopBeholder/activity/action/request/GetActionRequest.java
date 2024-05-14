package dev.tdwalsh.project.tabletopBeholder.activity.action.request;

public class GetActionRequest {
    private final String userEmail;
    private final String objectId;

    private GetActionRequest(String userEmail, String objectId) {
        this.userEmail = userEmail;
        this.objectId = objectId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;
        private String objectId;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withObjectId(String objectId) {
            this.objectId = objectId;
            return this;
        }

        public GetActionRequest build() {
            return new GetActionRequest(userEmail, objectId);
        }
    }
}

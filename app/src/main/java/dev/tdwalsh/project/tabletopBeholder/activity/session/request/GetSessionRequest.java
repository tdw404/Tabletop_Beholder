package dev.tdwalsh.project.tabletopBeholder.activity.session.request;

public class GetSessionRequest {
    private final String userEmail;
    private final String objectId;

    private GetSessionRequest(String userEmail, String objectId) {
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

        public GetSessionRequest build() {
            return new GetSessionRequest(userEmail, objectId);
        }
    }
}

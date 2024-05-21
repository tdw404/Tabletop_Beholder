package dev.tdwalsh.project.tabletopBeholder.activity.creature.request;

public class GetCreatureRequest {
    private final String userEmail;
    private final String objectId;

    private GetCreatureRequest(String userEmail, String objectId) {
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

        public GetCreatureRequest build() {
            return new GetCreatureRequest(userEmail, objectId);
        }
    }
}
package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

public class GetAllSpellsRequest {
    private final String userEmail;

    private GetAllSpellsRequest(String userEmail) {
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

        public GetAllSpellsRequest build() {
            return new GetAllSpellsRequest(userEmail);
        }
    }
}

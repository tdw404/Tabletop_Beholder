package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request;

public class CreateTemplateCreatureRequest {
    private final String userEmail;
    private final String slug;

    private CreateTemplateCreatureRequest(String userEmail, String slug) {
        this.userEmail = userEmail;
        this.slug = slug;
    }

    public String getUserEmail() { return this.userEmail; }

    public String getSlug() { return this.slug; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String userEmail;
        private String slug;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withSlug(String slug) {
            this.slug = slug;
            return this;
        }

        public CreateTemplateCreatureRequest build() {
            return new CreateTemplateCreatureRequest(userEmail, slug);
        }

    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request;

public class CreateTemplateCreatureRequest {
    private final String userEmail;
    private final String slug;

    private CreateTemplateCreatureRequest(String userEmail, String slug) {
        this.userEmail = userEmail;
        this.slug = slug;
    }

    public String getUserEmail() {
        return this.userEmail; }

    public String getSlug() {
        return this.slug; }

    /**
     * Builder.
     * @return builder.
     */
    public static Builder builder() {
        return new Builder(); }

    public static class Builder {
        private String userEmail;
        private String slug;

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
         * Builder setter.
         * @param slug variable to set
         * @return builder
         */
        public Builder withSlug(String slug) {
            this.slug = slug;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public CreateTemplateCreatureRequest build() {
            return new CreateTemplateCreatureRequest(userEmail, slug);
        }

    }
}

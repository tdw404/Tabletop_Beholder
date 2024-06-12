package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request;

public class CreateTemplateSpellRequest {
    private final String userEmail;
    private final String slug;

    private CreateTemplateSpellRequest(String userEmail, String slug) {
        this.userEmail = userEmail;
        this.slug = slug;
    }

    public String getUserEmail() {
        return this.userEmail; }

    public String getSlug() {
        return this.slug; }

    /**
     * Builder setter.
     * @return builder
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
        public CreateTemplateSpellRequest build() {
            return new CreateTemplateSpellRequest(userEmail, slug);
        }
    }
}

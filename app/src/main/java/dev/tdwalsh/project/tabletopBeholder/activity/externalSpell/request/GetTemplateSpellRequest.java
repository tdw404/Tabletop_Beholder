package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request;

public class GetTemplateSpellRequest {
    private final String userEmail;
    private final String slug;

    private GetTemplateSpellRequest(String userEmail, String slug) {
        this.userEmail = userEmail;
        this.slug = slug;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getSlug() {
        return this.slug;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }
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
         * @param slug - object to set.
         * @return Builder
         */
        public Builder withSlug(String slug) {
            this.slug = slug;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public GetTemplateSpellRequest build() {
            return new GetTemplateSpellRequest(userEmail, slug);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.request;

public class SearchTemplateCreaturesRequest {
    private final String userEmail;

    private final String searchTerms;

    private SearchTemplateCreaturesRequest(String userEmail, String searchTerms) {
        this.userEmail = userEmail;
        this.searchTerms = searchTerms;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getSearchTerms() {
        return this.searchTerms;
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
        private String searchTerms;

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
         * @param searchTerms variable to set
         * @return builder
         */
        public Builder withSearchTerms(String searchTerms) {
            this.searchTerms = searchTerms;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public SearchTemplateCreaturesRequest build() {
            return new SearchTemplateCreaturesRequest(userEmail, searchTerms);
        }
    }
}

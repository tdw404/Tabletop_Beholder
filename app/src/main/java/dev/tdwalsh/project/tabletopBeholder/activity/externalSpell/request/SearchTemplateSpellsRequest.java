package dev.tdwalsh.project.tabletopBeholder.activity.externalSpell.request;

public class SearchTemplateSpellsRequest {
    private final String userEmail;

    private final String searchTerms;

    private SearchTemplateSpellsRequest(String userEmail, String searchTerms) {
        this.userEmail = userEmail;
        this.searchTerms = searchTerms;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getSearchTerms() {
        return this.searchTerms;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;
        private String searchTerms;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withSearchTerms(String searchTerms) {
            this.searchTerms = searchTerms;
            return this;
        }

        public SearchTemplateSpellsRequest build() {
            return new SearchTemplateSpellsRequest(userEmail, searchTerms);
        }
    }
}

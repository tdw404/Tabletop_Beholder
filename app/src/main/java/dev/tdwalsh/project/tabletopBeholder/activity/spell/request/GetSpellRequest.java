package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

import java.util.Locale;

public class GetSpellRequest {
    private final String userEmail;
    private final String spellId;

    private GetSpellRequest (String userEmail, String spellId) {
        this.userEmail = userEmail;
        this.spellId = spellId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getSpellId() {
        return this.spellId;
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String userEmail;
        private String spellId;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder withSpellId(String spellId) {
            this.spellId = spellId;
            return this;
        }

        public GetSpellRequest build() {
            return new GetSpellRequest(userEmail, spellId);
        }
    }
}

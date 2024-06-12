package dev.tdwalsh.project.tabletopBeholder.activity.session.request;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = UpdateSessionRequest.Builder.class)
public class UpdateSessionRequest {
    private final Session session;
    private final String userEmail;

    private UpdateSessionRequest(Session session, String userEmail) {
        this.session = session;
        this.userEmail = userEmail;
    }

    public Session getSession() {
        return this.session;
    }

    public String getUserEmail() {
        return this.userEmail; }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Session session;
        private String userEmail;

        /**
         * Builder setter.
         * @param session variable to set
         * @return builder
         */
        public Builder withSession(Session session) {
            this.session = session;
            return this;
        }

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
         * Builder.
         * @return builder
         */
        public UpdateSessionRequest build() {
            return new UpdateSessionRequest(session, userEmail);
        }
    }
}

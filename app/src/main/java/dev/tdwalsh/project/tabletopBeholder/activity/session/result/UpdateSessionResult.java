package dev.tdwalsh.project.tabletopBeholder.activity.session.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

public class UpdateSessionResult {
    private final Session session;

    private UpdateSessionResult(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    /**
     * Builder.
     * @return - Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Session session;

        /**
         * Builder setter.
         * @param session - Variable to set.
         * @return - builder
         */
        public Builder withSession(Session session) {
            this.session = session;
            return this;
        }

        /**
         * Builder.
         * @return Builder.
         */
        public UpdateSessionResult build() {
            return new UpdateSessionResult(session);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.session.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

public class CreateSessionResult {
    private final Session session;

    private CreateSessionResult(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Session session;

        /**
         * Builder setter.
         * @param session - variable to set
         * @return builder
         */
        public Builder withSession(Session session) {
            this.session = session;
            return this;
        }

        /**
         * Builds object.
         * @return builder
         */
        public CreateSessionResult build() {
            return new CreateSessionResult(session);
        }
    }
}

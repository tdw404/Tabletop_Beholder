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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Session session;

        public Builder withSession(Session session) {
            this.session = session;
            return this;
        }

        public UpdateSessionResult build() {
            return new UpdateSessionResult(session);
        }
    }
}

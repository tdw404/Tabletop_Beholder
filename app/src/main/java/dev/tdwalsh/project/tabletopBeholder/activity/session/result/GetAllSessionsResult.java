package dev.tdwalsh.project.tabletopBeholder.activity.session.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import java.util.List;

public class GetAllSessionsResult {
    private final List<Session> sessionList;

    private GetAllSessionsResult(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public List<Session> getSessionList() {
        return this.sessionList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Session> sessionList;

        public Builder withSessionList(List<Session> sessionList) {
            this.sessionList = sessionList;
            return this;
        }

        public GetAllSessionsResult build() {
            return new GetAllSessionsResult(sessionList);
        }
    }
}

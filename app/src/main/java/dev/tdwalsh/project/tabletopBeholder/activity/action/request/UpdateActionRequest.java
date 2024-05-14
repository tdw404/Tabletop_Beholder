package dev.tdwalsh.project.tabletopBeholder.activity.action.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

@JsonDeserialize(builder = UpdateActionRequest.Builder.class)
public class UpdateActionRequest {
    private final Action action;
    private final String userEmail;

    private UpdateActionRequest(Action action, String userEmail) {
        this.action = action;
        this.userEmail = userEmail;
    }

    public Action getAction() {
        return this.action;
    }

    public String getUserEmail() { return this.userEmail; }

    public static Builder builder() {
        return new Builder();
    }

public static class Builder {
        private Action action;
        private String userEmail;

        public Builder withAction(Action action) {
            this.action = action;
            return this;
        }

    public Builder withUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

        public UpdateActionRequest build() {
            return new UpdateActionRequest(action, userEmail);
        }
}

}

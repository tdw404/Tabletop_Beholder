package dev.tdwalsh.project.tabletopBeholder.activity.action.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

@JsonDeserialize(builder = CreateActionRequest.Builder.class)
public class CreateActionRequest {
    private final Action action;
    private final String userEmail;

    private CreateActionRequest(Action action, String userEmail) {

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

        public CreateActionRequest build() {
            return new CreateActionRequest(action, userEmail);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.action.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

public class GetActionResult {
    private final Action action;

    private GetActionResult(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Action action;

        public Builder withAction(Action action) {
            this.action = action;
            return this;
        }

        public GetActionResult build() {
            return new GetActionResult(action);
        }
    }
}

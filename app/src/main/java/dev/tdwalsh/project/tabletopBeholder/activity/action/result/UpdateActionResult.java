package dev.tdwalsh.project.tabletopBeholder.activity.action.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

public class UpdateActionResult {
    private final Action action;

    private UpdateActionResult(Action action) {
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

        public UpdateActionResult build() {
            return new UpdateActionResult(action);
        }
    }
}

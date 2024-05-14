package dev.tdwalsh.project.tabletopBeholder.activity.action.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;

import java.util.List;

public class GetAllActionsResult {
    private final List<Action> actionList;

    private GetAllActionsResult(List<Action> actionList) {
        this.actionList = actionList;
    }

    public List<Action> getActionList() {
        return this.actionList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Action> actionList;

        public Builder withActionList(List<Action> actionList) {
            this.actionList = actionList;
            return this;
        }

        public GetAllActionsResult build() {
            return new GetAllActionsResult(actionList);
        }
    }
}

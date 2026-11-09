package dev.tdwalsh.project.tabletopBeholder.activity.externalCreature.result;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;

import java.util.List;

public class CreateTemplateCreatureResult {
    private final Creature creature;

    private final List<String> warnings;

    private CreateTemplateCreatureResult(Creature creature, List<String> warnings) {
        this.creature = creature;
        this.warnings = List.copyOf(warnings);
    }

    public Creature getCreature() {
        return creature;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Creature creature;
        private List<String> warnings = List.of();

        public Builder withWarnings(List<String> warnings) {
            this.warnings = warnings;
            return this;
        }

        /**
         * Builder setter.
         * @param creature variable to set
         * @return builder
         */
        public Builder withCreature(Creature creature) {
            this.creature = creature;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public CreateTemplateCreatureResult build() {
            return new CreateTemplateCreatureResult(creature, warnings);
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.activity.spell.request;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Spell;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = UpdateSpellRequest.Builder.class)
public class UpdateSpellRequest {
    private final Spell spell;
    private final String userEmail;

    private UpdateSpellRequest(Spell spell, String userEmail) {
        this.spell = spell;
        this.userEmail = userEmail;
    }

    public Spell getSpell() {
        return this.spell;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    /**
     * Builder.
     * @return - Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Spell spell;
        private String userEmail;

        /**
         * Builder setter.
         * @param spell - Value to set.
         * @return Builder
         */
        public Builder withSpell(Spell spell) {
            this.spell = spell;
            return this;
        }

        /**
         * Builder setter.
         * @param userEmail - value to set.
         * @return Builder
         */
        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        /**
         * Builder.
         * @return - Builder
         */
        public UpdateSpellRequest build() {
            return new UpdateSpellRequest(spell, userEmail);
        }
    }
}

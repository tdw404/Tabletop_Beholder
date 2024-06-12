package dev.tdwalsh.project.tabletopBeholder.activity.encounter.request;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = UpdateEncounterRequest.Builder.class)
public class UpdateEncounterRequest {
    private final Encounter encounter;
    private final String userEmail;

    private UpdateEncounterRequest(Encounter encounter, String userEmail) {
        this.encounter = encounter;
        this.userEmail = userEmail;
    }

    public Encounter getEncounter() {
        return this.encounter;
    }

    public String getUserEmail() {
        return this.userEmail; }

    /**
     * Builder.
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private Encounter encounter;
        private String userEmail;

        /**
         * Builder setter.
         * @param encounter variable to set
         * @return builder
         */
        public Builder withEncounter(Encounter encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Builder setter.
         * @param userEmail variable to set
         * @return builder
         */
        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        /**
         * Builder.
         * @return builder
         */
        public UpdateEncounterRequest build() {
            return new UpdateEncounterRequest(encounter, userEmail);
        }
    }
}

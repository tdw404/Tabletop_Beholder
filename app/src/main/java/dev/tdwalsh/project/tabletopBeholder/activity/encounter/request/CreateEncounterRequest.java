package dev.tdwalsh.project.tabletopBeholder.activity.encounter.request;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CreateEncounterRequest.Builder.class)
public class CreateEncounterRequest {
    private final Encounter encounter;
    private final String userEmail;

    private CreateEncounterRequest(Encounter encounter, String userEmail) {

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
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Encounter encounter;
        private String userEmail;

        /**
         * Builder setter.
         * @param encounter - variable to set
         * @return builder
         */
        public Builder withEncounter(Encounter encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * Builder setter.
         * @param userEmail - variable to set
         * @return Builder
         */
        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        /**
         * Builder.
         * @return Builder
         */
        public CreateEncounterRequest build() {
            return new CreateEncounterRequest(encounter, userEmail);
        }
    }
}

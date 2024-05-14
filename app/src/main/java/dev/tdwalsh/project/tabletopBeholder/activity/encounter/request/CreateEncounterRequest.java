package dev.tdwalsh.project.tabletopBeholder.activity.encounter.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;

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

    public String getUserEmail() { return this.userEmail; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Encounter encounter;
        private String userEmail;

        public Builder withEncounter(Encounter encounter) {
            this.encounter = encounter;
            return this;
        }

        public Builder withUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public CreateEncounterRequest build() {
            return new CreateEncounterRequest(encounter, userEmail);
        }
    }
}

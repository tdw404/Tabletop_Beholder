package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import java.util.Objects;

public class EncounterName {
    private String userEmail;
    private String objectId;
    private String objectName;

    /**
     * Constructor.
     * @param userEmail - email to assign
     * @param objectId - objectId to assign
     * @param objectName - objectName to assign
     */
    public EncounterName(String userEmail, String objectId, String objectName) {
        this.userEmail = userEmail;
        this.objectId = objectId;
        this.objectName = objectName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userEmail + this.objectId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        EncounterName other = (EncounterName) o;
        return (this.userEmail.equals(other.userEmail)) && (this.objectId.equals(other.objectId));
    }
}

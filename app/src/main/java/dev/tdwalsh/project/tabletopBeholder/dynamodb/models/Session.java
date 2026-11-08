package dev.tdwalsh.project.tabletopBeholder.dynamodb.models;

import dev.tdwalsh.project.tabletopBeholder.converters.ZonedDateTimeConverter;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import org.apache.commons.text.WordUtils;

import java.time.ZonedDateTime;
import java.util.Objects;

@DynamoDbBean
public class Session implements BeholderObject {
    public static final String TABLE_NAME = "TabletopBeholder_SessionTable";
    private String userEmail;
    private String objectId;
    private String objectName;
    private ZonedDateTime createDateTime;
    private ZonedDateTime editDateTime;

    @Override
    @DynamoDbPartitionKey
    @DynamoDbSecondaryPartitionKey(indexNames = "SessionsSortByNameIndex")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    @DynamoDbSortKey
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    @DynamoDbSecondarySortKey(indexNames = "SessionsSortByNameIndex")
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = "".equals(objectName) ? null : WordUtils.capitalizeFully(objectName);
    }

    @DynamoDbConvertedBy(ZonedDateTimeConverter.class)
    public ZonedDateTime getCreateDateTime() {
        return createDateTime;
    }

    @Override
    public void setCreateDateTime(ZonedDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @DynamoDbConvertedBy(ZonedDateTimeConverter.class)
    public ZonedDateTime getEditDateTime() {
        return editDateTime;
    }

    @Override
    public void setEditDateTime(ZonedDateTime editDateTime) {
        this.editDateTime = editDateTime;
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

        Session other = (Session) o;
        return this.userEmail.equals(other.userEmail) && this.objectId.equals(other.objectId);
    }
}

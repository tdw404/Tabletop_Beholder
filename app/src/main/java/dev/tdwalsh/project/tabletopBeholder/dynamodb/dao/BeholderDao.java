package dev.tdwalsh.project.tabletopBeholder.dynamodb.dao;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.BeholderObject;

import java.util.List;

public interface BeholderDao {
    public BeholderObject getSingle(String userEmail, String objectId);
    public List<? extends BeholderObject> getMultiple(String userEmail);
    public void writeObject(BeholderObject object);
    public void deleteObject(String userEmail, String objectId);
    public Boolean objectNameExists (String userEmail, String objectName);
}

package dev.tdwalsh.project.tabletopBeholder.resource;

import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Encounter;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Session;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionHelper {
    public static Session provideSession(Integer mod) {
        Session session = new Session();
        session.setUserEmail("userEmail" + mod);
        session.setObjectId("objectId" + mod);
        session.setObjectName("objectName" + mod);
        session.setCreateDateTime(ZonedDateTime.now());
        session.setEditDateTime(ZonedDateTime.now());
        return session;
    }
}

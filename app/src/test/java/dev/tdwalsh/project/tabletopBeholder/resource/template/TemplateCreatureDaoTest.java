package dev.tdwalsh.project.tabletopBeholder.resource.template;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class TemplateCreatureDaoTest {
    @InjectMocks
    private TemplateCreatureDao dao;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

//    @Test
//    public void getSingle_creatureExists_returnsCreature() {
//        //GIVEN
//        //WHEN
//        TemplateCreature result = dao.getSingle("abominable-snowman-a5e");
//
//        //THEN
//        assertEquals("Abominable Snowman", result.getName(), "Expected dao to return single matching result");
//    }

//    @Test
//    public void getSingle_creatureDoesNotExist_throwsException() {
//        //GIVEN
//        //WHEN
//        //THEN
//        assertThrows(MissingResourceException.class, () -> dao.getSingle("doesnotexist"), "Expected dao to throw Missing Resource Exception");
//    }

//    @Test
//    public void getMultiple_creatureExists_returnsCreature() {
//        //GIVEN
//        //WHEN
//        List<TemplateCreature> result = dao.getMultiple("a");
//
//        //THEN
//        assertEquals("Abominable Snowman", result.getName(), "Expected dao to return single matching result");
//    }

}

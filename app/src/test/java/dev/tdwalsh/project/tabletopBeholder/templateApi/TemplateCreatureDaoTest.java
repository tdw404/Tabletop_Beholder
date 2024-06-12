package dev.tdwalsh.project.tabletopBeholder.templateApi;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.Open5EClient;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class TemplateCreatureDaoTest {
    @InjectMocks
    private TemplateCreatureDao dao;

    @Mock
    Open5EClient open5EClient;

    @Mock
    HttpResponse<String> response;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    @Test
    public void getSingle_200_returnsCreature() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"name\": \"testName\"}").when(response).body();
        //WHEN
        TemplateCreature result = dao.getSingle("slug");

        //THEN
        assertEquals("testName", result.getName(), "Expected dao to return single matching result");
    }

    @Test
    public void getSingle_404_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(404).when(response).statusCode();
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> dao.getSingle("slug"));
    }

    @Test
    public void getSingle_badObject_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{//}").when(response).body();
        //WHEN
        //THEN
        assertThrows(CurlException.class, () -> dao.getSingle("slug"));
    }

    @Test
    public void getSingle_500_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(500).when(response).statusCode();
        //WHEN
        //THEN
        assertThrows(CurlException.class, () -> dao.getSingle("slug"));
    }

    @Test
    public void getMultiple_200_returnsCreature() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"results\": [{\"name\": \"testName\"}]}").when(response).body();
        //WHEN
        List<TemplateCreature> result = dao.getMultiple("slug");

        //THEN
        assertEquals("testName", result.get(0).getName(), "Expected dao to return single matching result");
    }

    @Test
    public void getMultiple_404_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(404).when(response).statusCode();
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> dao.getMultiple("slug"));
    }

    @Test
    public void getMultiple_500_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(500).when(response).statusCode();
        //WHEN
        //THEN
        assertThrows(CurlException.class, () -> dao.getMultiple("slug"));
    }

    @Test
    public void getMultiple_badObject_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{//}").when(response).body();
        //WHEN
        //THEN
        assertThrows(CurlException.class, () -> dao.getSingle("slug"));
    }


}

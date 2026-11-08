package dev.tdwalsh.project.tabletopBeholder.templateApi;

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
        verify(open5EClient).call("https://api.open5e.com/v1/monsters/slug/");

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
        List<TemplateCreature> result = dao.getMultiple("search=wind dragon&limit=50");
        verify(open5EClient).call("https://api.open5e.com/v1/monsters/?search=wind%20dragon&limit=50");

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



    @Test
    public void getMultiple_invalidJson_reportsUpstreamFailure() {
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("<html>Temporary error</html>").when(response).body();

        assertThrows(CurlException.class, () -> dao.getMultiple("search=dragon&limit=50"));
    }

    @Test
    public void searchAndDetail_acceptOpen5eActionExtrasAndHoverFlag() {
        String json = "{\"name\":\"Dragon\",\"speed\":{\"fly\":80,\"hover\":true},"
                + "\"actions\":[{\"name\":\"Bite\",\"damage_dice\":\"2d10\",\"damage_bonus\":6}]}";
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"results\":[" + json + "]}", json).when(response).body();

        TemplateCreature searchResult = dao.getMultiple("search=dragon&limit=50").get(0);
        TemplateCreature detail = dao.getSingle("dragon");

        for (TemplateCreature creature : List.of(searchResult, detail)) {
            assertEquals(java.util.Map.of("fly", 80), creature.getSpeed());
            assertEquals("Bite", creature.getActions().get(0).getName());
            assertEquals("2d10", creature.getActions().get(0).getDamage_dice());
        }
    }
}

package dev.tdwalsh.project.tabletopBeholder.templateApi;

import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.converters.templateTranslators.TemplateCreatureTranslator;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.dao.CreatureDao;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Creature;
import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.resource.CreatureHelper;
import dev.tdwalsh.project.tabletopBeholder.templateApi.Open5EClient;
import dev.tdwalsh.project.tabletopBeholder.templateApi.TemplateCreatureDao;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;
import org.junit.jupiter.api.BeforeEach;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
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
    public void getMultiple_200_searchesByPartialName() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"results\": [{\"name\": \"Ancient Wind Dragon\"}]}").when(response).body();
        //WHEN
        List<TemplateCreature> result = dao.getMultiple("search=WiNd DrAgOn&limit=50");
        verify(open5EClient).call("https://api.open5e.com/v1/monsters/?name__icontains=WiNd+DrAgOn&limit=50");

        //THEN
        assertEquals("Ancient Wind Dragon", result.get(0).getName(), "Expected dao to return single matching result");
    }

    @Test
    public void getMultiple_treatsQueryCharactersAsPartOfTheName() {
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"results\":[]}").when(response).body();

        assertTrue(dao.getMultiple("search=Fire & Ice&limit=10").isEmpty());
        verify(open5EClient).call("https://api.open5e.com/v1/monsters/?name__icontains=Fire+%26+Ice&limit=10");
    }

    @Test
    public void getMultiple_404_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(404).when(response).statusCode();
        //WHEN
        //THEN
        assertThrows(MissingResourceException.class, () -> dao.getMultiple("search=dragon"));
    }

    @Test
    public void getMultiple_500_throwsException() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(500).when(response).statusCode();
        //WHEN
        //THEN
        assertThrows(CurlException.class, () -> dao.getMultiple("search=dragon"));
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
            assertEquals(Boolean.TRUE, creature.getHover());
            assertEquals(6, creature.getActions().get(0).getDamage_bonus());
            assertEquals("Bite", creature.getActions().get(0).getName());
            assertEquals("2d10", creature.getActions().get(0).getDamage_dice());
        }
    }
    @Test
    public void importedHoverAndAttackDetailsSurviveStorageRoundTrip() throws Exception {
        var mapper = new ObjectMapper();
        var template = mapper.readValue("{\"name\":\"Blink Dog\","
                + "\"challenge_rating\":\"1/4\",\"speed\":{\"swim\":20,\"burrow\":10,\"hover\":true},"
                + "\"actions\":[{\"name\":\"Bite\",\"desc\":\"Bite description\","
                + "\"attack_bonus\":3,\"damage_dice\":\"1d6\",\"damage_bonus\":0},"
                + "{\"name\":\"Teleport\",\"desc\":\"Teleport description\"}]}", TemplateCreature.class);
        var translator = new TemplateCreatureTranslator(null, null);
        Creature imported = translator.translate(template, "owner");
        var schema = TableSchema.fromBean(Creature.class);
        Creature saved = schema.mapToItem(schema.itemToMap(imported, true));

        assertEquals(Boolean.TRUE, saved.getHover());
        assertEquals(java.util.Map.of("swim", 20, "burrow", 10), saved.getSpeedMap());
        var bite = saved.getActionMap().values().stream()
                .filter(action -> action.getObjectName().equals("Bite")).findFirst().orElseThrow();
        assertEquals(3, bite.getAttackBonus());
        assertEquals("1d6", bite.getDamageDice());
        assertEquals(0, bite.getDamageModifier());
        assertEquals("Bite description", bite.getActionDescription());
        var teleport = saved.getActionMap().values().stream()
                .filter(action -> action.getObjectName().equals("Teleport")).findFirst().orElseThrow();
        assertNull(teleport.getAttackBonus());
        assertNull(teleport.getDamageDice());
        assertNull(teleport.getDamageModifier());
        assertEquals("Teleport description", teleport.getActionDescription());

        Creature legacy = new Creature();
        legacy.setSpeedMap(java.util.Map.of("fly", 40, "hover", 10));
        Creature reloaded = schema.mapToItem(schema.itemToMap(legacy, true));
        assertNull(reloaded.getHover());
        assertEquals(10, reloaded.getSpeedMap().get("hover"));
    }
}

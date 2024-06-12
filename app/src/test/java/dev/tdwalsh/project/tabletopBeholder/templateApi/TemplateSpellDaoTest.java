package dev.tdwalsh.project.tabletopBeholder.templateApi;

import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

public class TemplateSpellDaoTest {
    @InjectMocks
    private TemplateSpellDao dao;

    @Mock
    Open5EClient open5EClient;

    @Mock
    HttpResponse<String> response;

    @BeforeEach
    public void setup() {
        openMocks(this);
    }

    @Test
    public void getSingle_200_returnsSpell() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"name\": \"testName\"}").when(response).body();
        //WHEN
        TemplateSpell result = dao.getSingle("slug");

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
    public void getMultiple_200_returnsSpell() {
        //GIVEN
        doReturn(response).when(open5EClient).call(anyString());
        doReturn(200).when(response).statusCode();
        doReturn("{\"results\": [{\"name\": \"testName\"}]}").when(response).body();
        //WHEN
        List<TemplateSpell> result = dao.getMultiple("slug");

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

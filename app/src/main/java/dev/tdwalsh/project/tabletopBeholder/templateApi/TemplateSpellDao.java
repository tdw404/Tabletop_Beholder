package dev.tdwalsh.project.tabletopBeholder.templateApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for taking in query parameters and returning results as {@link TemplateSpell}.
 */
@Singleton
public class TemplateSpellDao {
    private final static String URI_PATH = "https://api.open5e.com/spells/";
    ObjectMapper objectMapper;

    @Inject
    public TemplateSpellDao() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves an {@link TemplateSpell} from an api call.
     *
     * @param spellSlug The external id of the resource to be retrieved
     * @return A single {@link TemplateSpell} if found, or throws error if none found
     */
    public TemplateSpell getSingle(String spellSlug)  {
        //First, uses the 'slug'/id of the external object to build a uri
        //Then, makes a GET call with that uri
        //Then, converts the result to a model object and returns it
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_PATH + spellSlug))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()) {
                case 404:
                    throw new MissingResourceException("5E API could not return resource: " + spellSlug);
                case 200:
                    return  objectMapper.readValue(response.body(), TemplateSpell.class);
                default:
                    throw new CurlException("Error making call to 5E API: " + response.statusCode() + "  " + response.body());
            }
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
        } catch (InterruptedException e) {
            throw new CurlException("Call to 5E API interrupted: ", e);
        }
    }

    /**
     * Retrieves multiple {@link TemplateSpell} from an api call.
     *
     * @param searchTerms A list of search modifiers to be appended to the uri
     * @return A list of {@link TemplateSpell} if found, or an empty list if none found
     */
    public List<TemplateSpell> getMultiple(String searchTerms)  {
        //First, uses the search term chain of the external object to build a uri
        //Then, makes a GET call with that url
        //Then, converts the result to a list of model objects and returns it
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_PATH + "?" + searchTerms))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()) {
                case 404:
                    throw new MissingResourceException("5E API could not return resource with parameters: " + searchTerms);
                case 200:
                    JSONObject spellJson = new JSONObject(response.body());
                    JSONArray spellArray =  spellJson.getJSONArray("results");
                    List<TemplateSpell> templateSpellList = new ArrayList<>();
                    for(int i = 0; i < spellArray.length(); i++) {
                        templateSpellList.add(objectMapper.readValue(spellArray.get(i).toString(), TemplateSpell.class));
                    }
                    return templateSpellList;
                default:
                    throw new CurlException("Error making call to 5E API: " + response.statusCode() + "  " + response.body());
            }
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
        } catch (InterruptedException e) {
            throw new CurlException("Call to 5E API interrupted: ", e);
        }
    }
}
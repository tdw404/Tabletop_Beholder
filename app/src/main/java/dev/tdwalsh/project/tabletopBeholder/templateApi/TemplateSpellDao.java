package dev.tdwalsh.project.tabletopBeholder.templateApi;

import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class responsible for taking in query parameters and returning results as {@link TemplateSpell}.
 */
@Singleton
public class TemplateSpellDao {
    private static final String URI_PATH = "https://api.open5e.com/spells/";
    ObjectMapper objectMapper;
    private final Open5EClient open5EClient;

    /**
     * Constructor.
     * @param open5EClient - HTTP call client.
     */
    @Inject
    public TemplateSpellDao(Open5EClient open5EClient) {
        objectMapper = new ObjectMapper();
        this.open5EClient = open5EClient;
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
        try {
            HttpResponse<String> response = open5EClient.call(URI_PATH + spellSlug);
            switch (response.statusCode()) {
                case 404:
                    throw new MissingResourceException("5E API could not return resource: " + spellSlug);
                case 200:
                    return  objectMapper.readValue(response.body(), TemplateSpell.class);
                default:
                    throw new CurlException("Error making call to 5E API: " +
                            response.statusCode() + "  " + response.body());
            }
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
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
        String cleanedTerms = searchTerms.replace(" ", "%20");
        try {
            HttpResponse<String> response = open5EClient.call(URI_PATH + "?" + cleanedTerms);
            switch (response.statusCode()) {
                case 404:
                    throw new MissingResourceException("5E API could not return resource with parameters: " +
                            searchTerms);
                case 200:
                    JSONObject spellJson = new JSONObject(response.body());
                    JSONArray spellArray =  spellJson.getJSONArray("results");
                    List<TemplateSpell> templateSpellList = new ArrayList<>();
                    for (int i = 0; i < spellArray.length(); i++) {
                        templateSpellList.add(objectMapper
                                .readValue(spellArray.get(i).toString(), TemplateSpell.class));
                    }
                    return templateSpellList;
                default:
                    throw new CurlException("Error making call to 5E API: " +
                            response.statusCode() + "  " + response.body());
            }
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
        }
    }
}

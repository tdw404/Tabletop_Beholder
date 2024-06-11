package dev.tdwalsh.project.tabletopBeholder.templateApi;

import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;




/**
 * Class responsible for taking in query parameters and returning results as {@link TemplateCreature}.
 */
@Singleton
public class TemplateCreatureDao {
    private static final String URI_PATH = "https://api.open5e.com/monsters/";
    private final ObjectMapper objectMapper;
    private final Open5EClient open5EClient;

    /**
     * Constructor.
     */
    @Inject
    public TemplateCreatureDao(Open5EClient open5EClient) {
        this.objectMapper = new ObjectMapper();
        this.open5EClient = open5EClient;
    }

    /**
     * Retrieves an {@link TemplateCreature} from an api call.
     *
     * @param creatureSlug The external id of the resource to be retrieved
     * @return A single {@link TemplateCreature} if found, or throws error if none found
     */
    public TemplateCreature getSingle(String creatureSlug) {
        //First, uses the 'slug'/id of the external object to build a uri
        //Then, makes a GET call with that uri
        //Then, converts the result to a model object and returns it

        try {
            HttpResponse<String> response = open5EClient.call(URI_PATH + creatureSlug);
            switch (response.statusCode()) {
                case 404:
                    throw new MissingResourceException("5E API could not return resource: " + creatureSlug);
                case 200:
                    return objectMapper.readValue(response.body(), TemplateCreature.class);
                default:
                    throw new CurlException("Error making call to 5E API: " +
                            response.statusCode() + "  " + response.body());
            }
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
        }
    }

    /**
     * Retrieves multiple {@link TemplateCreature} from an api call.
     *
     * @param searchTerms A list of search modifiers to be appended to the uri
     * @return A list of {@link TemplateCreature} if found, or an empty list if none found
     */
    public List<TemplateCreature> getMultiple(String searchTerms) {
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
                    JSONObject creatureJson = new JSONObject(response.body());
                    JSONArray creatureArray = creatureJson.getJSONArray("results");
                    List<TemplateCreature> templateCreatureList = new ArrayList<>();
                    for (int i = 0; i < creatureArray.length(); i++) {
                        try {
                            templateCreatureList.add(objectMapper
                                    .readValue(creatureArray.get(i).toString(), TemplateCreature.class));
                        } catch (MismatchedInputException e) {
                            System.out.println(e);
                        }
                    }
                    return templateCreatureList;
                default:
                    throw new CurlException("Error making call to 5E API: " +
                            response.statusCode() + "  " + response.body());
            }
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
        }
    }
}

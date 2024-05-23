package dev.tdwalsh.project.tabletopBeholder.templateApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.converters.templateConverters.TemplateCreatureConverter;
import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateCreature;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Class responsible for taking in query parameters and returning results as {@link TemplateCreature}.
 */
@Singleton
public class TemplateCreatureDao {
    private final static String URI_PATH = "https://api.open5e.com/monsters/";
    private final TemplateCreatureConverter templateCreatureConverter;
    private final ObjectMapper objectMapper;

    @Inject
    public TemplateCreatureDao() {
        this.templateCreatureConverter = new TemplateCreatureConverter();
        this.objectMapper = new ObjectMapper();
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
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_PATH + creatureSlug))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            switch (response.statusCode()) {
                case 404:
                    throw new MissingResourceException("5E API could not return resource: " + creatureSlug);
                case 200:
                    return objectMapper.readValue(response.body(), TemplateCreature.class);
                    //return templateCreatureConverter.unconvert(response.body());
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

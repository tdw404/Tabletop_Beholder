package dev.tdwalsh.project.tabletopBeholder.templateApi;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.exceptions.CommunicationException;
import dev.tdwalsh.project.tabletopBeholder.templateApi.model.TemplateSpell;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Class responsible for taking in query parameters and returning results as {@link TemplateSpell}.
 */
@Singleton
public class TemplateSpellDao {
    private final static String URL_PATH = "https://api.open5e.com/spells/";
    private final ObjectMapper objectMapper;

    @Inject
    public TemplateSpellDao(DynamoDBMapper mapper) {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves an {@link TemplateSpell} from an api call.
     *
     * @param spellSlug The external id of the resource to be retrieved
     * @return A single {@link TemplateSpell} if found, or null if none found
     */
    public TemplateSpell getSingle(String spellSlug) {
        //First, uses the 'slug'/id of the external object to build a url
        //Then, makes a GET call with that url
        //Then, converts the object to a model object and returns it
        try {
            String[] call = {"curl", "-X", "GET", URL_PATH + spellSlug};
            ProcessBuilder ps = new ProcessBuilder(call);
            Process pr = ps.start();
            pr.waitFor();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            return objectMapper.readValue(reader2.readLine(), TemplateSpell.class);
        } catch (java.io.IOException e) {
            throw new CommunicationException("Call to 5E API failed");
        } catch (InterruptedException e) {
            throw new CommunicationException("Process interrupted during call to 5E API");
        }
    }
}
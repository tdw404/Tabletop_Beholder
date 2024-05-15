package dev.tdwalsh.project.tabletopBeholder.externalApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tdwalsh.project.tabletopBeholder.dynamodb.models.Action;
import dev.tdwalsh.project.tabletopBeholder.exceptions.CommunicationException;
import dev.tdwalsh.project.tabletopBeholder.externalApi.model.ExternalSpell;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.util.Optional;

/**
 * Class responsible for taking in query parameters and returning results as {@link ExternalSpell}.
 */
@Singleton
public class ExternalSpellDao {
    private final static String URL_PATH = "https://api.open5e.com/spells/";
    private final ObjectMapper objectMapper;

    public ExternalSpellDao() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves an {@link ExternalSpell} from an api call.
     *
     * @param spellSlug The external id of the resource to be retrieved
     * @return A single {@link ExternalSpell} if found, or null if none found
     */
    public ExternalSpell getSingle(String spellSlug) {
        //First, uses the 'slug'/id of the external object to build a url
        //Then, makes a GET call with that url
        //Then, converts the object to a model object and returns it
        try {
            String[] call = {"curl", "-X", "GET", String.join(URL_PATH, spellSlug)};
            ProcessBuilder ps = new ProcessBuilder(call);
            Process pr = ps.start();
            pr.waitFor();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            return objectMapper.readValue(reader2.readLine(), ExternalSpell.class);
        } catch (java.io.IOException e) {
            throw new CommunicationException("Call to 5E API failed");
        } catch (InterruptedException e) {
            throw new CommunicationException("Process interrupted during call to 5E API");
        }
    }
}
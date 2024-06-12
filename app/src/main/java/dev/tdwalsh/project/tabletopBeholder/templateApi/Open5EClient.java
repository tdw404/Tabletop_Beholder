package dev.tdwalsh.project.tabletopBeholder.templateApi;

import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.inject.Singleton;

@Singleton
public class Open5EClient {

    /**
     * Constructor.
     */
    public Open5EClient() {
    }

    /**
     * Performs a GET call to an external API.
     * @param uri - Call URI
     * @return - HTTPResponse object.
     */
    public HttpResponse<String> call(String uri) {
        java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new CurlException("Error making call to 5E API: ", e);
        } catch (InterruptedException e) {
            throw new CurlException("Call to 5E API interrupted: ", e);
        }
    }
}

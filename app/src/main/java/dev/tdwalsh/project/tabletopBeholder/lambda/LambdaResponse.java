package dev.tdwalsh.project.tabletopBeholder.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

/**
 * An APIGateway response from a lambda function.
 */
public class LambdaResponse extends APIGatewayProxyResponseEvent {
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());


    private LambdaResponse(int statusCode, String body) {
        super.setStatusCode(statusCode);
        super.setBody(body);
        super.setHeaders(Map.of(
                "Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS",
                "Access-Control-Allow-Headers", "Content-Type,Authorization",
                "Access-Control-Allow-Origin", "*"
        ));
    }

    /**
     * Create a successful response with a given body.
     * @param payload The object to be converted to JSON and placed into the response body
     * @return A new LambdaResponse
     */
    public static LambdaResponse success(Object payload) {

        try {
            return new LambdaResponse(200, MAPPER.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to convert payload to JSON.");
        }
    }

    /**
     * Create a successful response without a body.
     * @return A new LambdaResponse
     */
    public static LambdaResponse success() {
        return new LambdaResponse(200, "");
    }

    /**
     * Create a successful response without a body.
     * @return A new LambdaResponse
     */
    public static LambdaResponse successNoContent() {
        return new LambdaResponse(204, "");
    }

    /**
     * Create a Not Found response.
     * @return A new LambdaResponse
     */
    public static LambdaResponse notFound() {
        return new LambdaResponse(404, "Not Found");
    }

    /**
     * Create a Internal Server Error response with a given message.
     * @param message A message describing the error
     * @return A new LambdaResponse
     */
    public static LambdaResponse error(String message) {
        return new LambdaResponse(500,
                String.format("{ \"error_message\": \"%s\" }", message));
    }

    /**
     * Create a Internal Server Error response with a given exception message.
     * @param e The exception representing the error that occurred.
     *          The exception's message is used to create the response.
     * @return A new LambdaResponse
     */
    public static LambdaResponse error(Exception e) {
        return error(e.getMessage());
    }
}

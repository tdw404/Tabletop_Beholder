package dev.tdwalsh.project.tabletopBeholder.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.tdwalsh.project.tabletopBeholder.exceptions.CurlException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MalformedInputException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.MissingResourceException;
import dev.tdwalsh.project.tabletopBeholder.exceptions.resourceNotFoundExceptions.DBResourceNotFoundException;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import java.net.http.HttpTimeoutException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * An APIGateway response from a lambda function.
 */
public class LambdaResponse extends APIGatewayProxyResponseEvent {
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());


    private LambdaResponse(int statusCode, String body) {
        super.setStatusCode(statusCode);
        super.setBody(body);
        super.setHeaders(Map.of(
                "Content-Type", "application/json",
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
        return error(404, "Resource not found.");
    }

    private static LambdaResponse error(int status, String message) {
        return error(status, Map.of("error_message", message));
    }

    private static LambdaResponse error(int status, Map<String, String> body) {
        try {
            return new LambdaResponse(status, MAPPER.writeValueAsString(body));
        } catch (JsonProcessingException exception) {
            return new LambdaResponse(500, "{\"error_message\":\"Unexpected server error.\"}");
        }
    }

    /**
     * Convert exceptions to public API errors without exposing internal exception messages.
     * @param exception the exception raised during request handling
     * @return a JSON error response
     */
    public static LambdaResponse error(Exception exception) {
        if (exception instanceof MalformedInputException) {
            return error(400, exception.getMessage() == null ? "Invalid request." : exception.getMessage());
        }
        if (exception instanceof MissingResourceException || exception instanceof DBResourceNotFoundException) {
            return notFound();
        }
        if (exception instanceof ConditionalCheckFailedException) {
            return error(409, "The record changed. Reload and try again.");
        }

        String requestId = UUID.randomUUID().toString();
        Logger.getLogger(LambdaResponse.class.getName()).warning(
                "Request " + requestId + " failed: " + exception.getClass().getName());
        if (exception instanceof CurlException) {
            int status = exception.getCause() instanceof HttpTimeoutException ? 504 : 502;
            return error(status, Map.of("error_message", "The upstream service could not complete the request.",
                    "request_id", requestId));
        }
        return error(500, Map.of("error_message", "Unexpected server error.", "request_id", requestId));
    }
}

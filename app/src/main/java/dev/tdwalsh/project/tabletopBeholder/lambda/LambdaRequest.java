package dev.tdwalsh.project.tabletopBeholder.lambda;

import dev.tdwalsh.project.tabletopBeholder.exceptions.MalformedInputException;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a generic "APIGateway" request made to a lambda function.
 * @param <T> The type of the concrete request that should be created from this LambdaRequest
 */
public class LambdaRequest<T> extends APIGatewayProxyRequestEvent {

    private static final int MAX_BODY_LENGTH = 2 * 1024 * 1024;
    protected static final ObjectMapper MAPPER = new ObjectMapper(JsonFactory.builder()
            .streamReadConstraints(StreamReadConstraints.builder()
                    .maxNestingDepth(64)
                    .maxStringLength(MAX_BODY_LENGTH)
                    .maxNumberLength(128)
                    .build())
            .build())
            .registerModule(new JavaTimeModule())
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    /**
     * Dserialize a T (aka 'requestClass`) from the body of the request.
     * @param requestClass The type that should be created from the body of this LambdaRequest
     * @return A new instance of T that contains data from the request body
     */
    public T fromBody(Class<T> requestClass) {
        return readJsonBody(requestClass);
    }

    /**
     * Parse a bounded JSON body using the request parser.
     * @param type expected JSON value type
     * @param <V> Java type of the parsed value
     * @return parsed request body
     */
    public <V> V readJsonBody(Class<V> type) {
        String body = getBody();
        if (body == null || body.isBlank()) {
            throw new MalformedInputException("A JSON request body is required.");
        }
        if (body.length() > MAX_BODY_LENGTH) {
            throw new MalformedInputException("The request body is too large.");
        }
        try {
            V value = MAPPER.readValue(body, type);
            if (value == null) {
                throw new MalformedInputException("A JSON request body is required.");
            }
            return value;
        } catch (JsonProcessingException exception) {
            throw new MalformedInputException("The request body contains invalid JSON.", exception);
        }
    }

    /**
     * Use the given converter to create an instance of T from the request's query string.
     * @param converter Contains the conversion code
     * @return A instance of T that contains data from the request's query string
     */
    public T fromQuery(Function<Map<String, String>, T> converter) {
        Map<String, String> query = Optional.ofNullable(super.getQueryStringParameters()).orElse(Map.of());
        return converter.apply(query);
    }

    /**
     * Use the given converter to create an instance of T from the request's path parameters.
     * @param converter Contains the conversion code
     * @return A instance of T that contains data from the request's path parameters
     */
    public T fromPath(Function<Map<String, String>, T> converter) {
        Map<String, String> path = Optional.ofNullable(super.getPathParameters()).orElse(Map.of());
        return converter.apply(path);
    }

    /**
     * Use the given converter to create an instance of T from the request's path parameters
     * and query string parameters.
     * @param converter Contains the conversion code
     * @return A instance of T that contains data from the request's path parameters
     */
    public T fromPathAndQuery(BiFunction<Map<String, String>, Map<String, String>, T> converter) {
        Map<String, String> path = Optional.ofNullable(super.getPathParameters()).orElse(Map.of());
        Map<String, String> query = Optional.ofNullable(super.getQueryStringParameters()).orElse(Map.of());
        return converter.apply(path, query);
    }
}

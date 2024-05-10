package dev.tdwalsh.project.tabletopBeholder.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    protected static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Dserialize a T (aka 'requestClass`) from the body of the request.
     * @param requestClass The type that should be created from the body of this LambdaRequest
     * @return A new instance of T that contains data from the request body
     */
    public T fromBody(Class<T> requestClass) {
        try {
            return MAPPER.readValue(super.getBody(), requestClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Unable to deserialize object from request body (%s).", requestClass.getSimpleName()),
                    e);
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

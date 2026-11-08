package dev.tdwalsh.project.tabletopBeholder.lambda;

import dev.tdwalsh.project.tabletopBeholder.exceptions.UnauthenticatedException;

import java.util.Map;
import java.util.function.Function;

/**
 * Reads the owner identity from the Cognito authorizer on an API Gateway request.
 * @param <T> the concrete activity request type
 */
public class AuthenticatedLambdaRequest<T> extends LambdaRequest<T> {

    /**
     * Builds an activity request using the verified Cognito subject.
     * @param converter builds the request from the owner ID
     * @return the activity request
     */
    public T fromOwnerId(Function<String, T> converter) {
        if (getRequestContext() == null || getRequestContext().getAuthorizer() == null) {
            throw new UnauthenticatedException();
        }
        Object claims = getRequestContext().getAuthorizer().get("claims");
        if (!(claims instanceof Map)) {
            throw new UnauthenticatedException();
        }
        Object subject = ((Map<?, ?>) claims).get("sub");
        if (!(subject instanceof String) || ((String) subject).isBlank()) {
            throw new UnauthenticatedException();
        }
        return converter.apply((String) subject);
    }
}

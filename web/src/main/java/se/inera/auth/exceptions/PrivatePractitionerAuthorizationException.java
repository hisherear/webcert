package se.inera.auth.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * Throw when an authenticated user does not pass the authorization check in privatlakarportalen. (E.g. not registered or
 * does not have the necessary roles in HSA etc.)
 */
public class PrivatePractitionerAuthorizationException extends AuthenticationException {
    public PrivatePractitionerAuthorizationException(String message) {
        super(message);
    }
}

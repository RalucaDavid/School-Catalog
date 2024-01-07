package org.school.server.jwt;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.SneakyThrows;
import org.school.database.models.UserEntity;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Optional;

public class JWTAuthFilter extends AuthFilter<String, UserEntity> {

    private final Authenticator<String, UserEntity> authenticator;

    public JWTAuthFilter(Authenticator<String, UserEntity> authenticator) {
        super();
        this.authenticator = authenticator;
    }

    @Override
    @SneakyThrows
    public void filter(ContainerRequestContext requestContext) {

        String token = extractToken(requestContext.getHeaders());

        if (token == null)
            throw new AuthenticationException("Authentication Token not provided");

        Optional<UserEntity> user = authenticator.authenticate(token);

        if (user.isEmpty())
            throw new AuthenticationException("Invalid token");

        requestContext.setSecurityContext(new JWTSecurityContext(user.get()));
    }

    private String extractToken(MultivaluedMap<String, String> headers) {

        String header = headers.getFirst("Authorization");

        if (header != null && header.startsWith("Bearer "))
            return header.substring("Bearer ".length()).trim();

        return null;
    }
}
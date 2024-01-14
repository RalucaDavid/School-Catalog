package org.school.server.jwt;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import lombok.SneakyThrows;
import org.school.database.dao.UserService;
import org.school.database.models.UserEntity;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Optional;

@Priority(Priorities.AUTHORIZATION)
public class JWTAuthFilter extends AuthFilter<String, UserEntity> {

    private final UserService userService = new UserService();

    @Override
    @SneakyThrows
    public void filter(ContainerRequestContext requestContext) {

        String token = extractToken(requestContext.getHeaders());

        if (token == null)
            throw new AuthenticationException("Authentication Token not provided");

        Optional<UserEntity> user = authenticate(token);

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

    private Optional<UserEntity> authenticate(String token) throws AuthenticationException {
        try {
            return Optional.of(userService.getByEmail(JWTGenerator.parse(token)));
        } catch (Exception e) {
            return Optional.empty(); // Authentication failed
        }
    }
}
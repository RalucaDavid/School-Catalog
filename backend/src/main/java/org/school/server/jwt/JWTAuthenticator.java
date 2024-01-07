package org.school.server.jwt;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.school.database.dao.UserService;
import org.school.database.models.UserEntity;
import java.util.Optional;

public class JWTAuthenticator implements Authenticator<String, UserEntity> {

    private final UserService userService = new UserService();

    @Override
    public Optional<UserEntity> authenticate(String token) throws AuthenticationException {
        try {
            return Optional.of(userService.getByEmail(JWTGenerator.parse(token)));
        } catch (Exception e) {
            return Optional.empty(); // Authentication failed
        }
    }
}
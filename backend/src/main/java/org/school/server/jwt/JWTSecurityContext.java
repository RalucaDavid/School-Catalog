package org.school.server.jwt;

import org.school.database.models.UserEntity;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JWTSecurityContext implements SecurityContext {

    private final UserEntity user;

    public JWTSecurityContext(UserEntity user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {

        if (role.equals("ADMIN"))
            return user.isSuperUser();

        return false;
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "JWT";
    }
}
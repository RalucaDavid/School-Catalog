package org.school.server;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.school.database.models.UserEntity;
import org.school.server.controllers.SubjectController;
import org.school.server.jwt.JWTAuthFilter;
import org.school.server.jwt.JWTGenerator;
import org.school.server.controllers.UserController;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class ServerApplication extends Application<ServerConfiguration> {

    @Override
    public void initialize(Bootstrap<ServerConfiguration> bootstrap) {}

    @Override
    public void run(ServerConfiguration configuration, Environment environment) {

        //CORS
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // JWT
        String secretKey = configuration.getJwtSecretKey();

        if (secretKey == null || secretKey.isEmpty())
            throw new RuntimeException("JWT secret key is missing from the configuration");

        JWTGenerator.setSecret(secretKey);
        environment.jersey().register(new AuthDynamicFeature(new JWTAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(UserEntity.class));

        // User Controller (Handles auth & /me endpoint)
        environment.jersey().register(new UserController());

        // Subjects routes
        environment.jersey().register(new SubjectController());
    }
}

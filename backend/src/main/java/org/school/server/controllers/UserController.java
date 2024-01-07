package org.school.server.controllers;

import io.dropwizard.auth.Auth;
import org.school.PasswordHasher;
import org.school.database.dao.UserService;
import org.school.database.models.UserEntity;
import org.school.data.*;
import org.school.server.jwt.JWTGenerator;
import org.school.server.response.ErrorResponse;
import org.school.server.response.Response;
import org.school.server.response.SuccessResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserService userService = new UserService();

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserRegisterData data) {
        try {
            userService.create(data);
        }
        catch (Exception exception) {
           return new ErrorResponse(exception.getMessage());
        }

        return new SuccessResponse("Account created.");
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginData data) {

        UserEntity user = userService.getByEmail(data.email());

        if (user == null) {
            return new ErrorResponse("Invalid credentials");
        }

        if (!PasswordHasher.checkPassword(data.password(), user.getPassword())) {
            return new ErrorResponse("Invalid credentials");
        }

        return new SuccessResponse(JWTGenerator.generate(user.getEmail()));
    }

    @GET
    @Path("/me")
    public Response getUserData(@Auth UserEntity user) {
        return new SuccessResponse(user);
    }
}

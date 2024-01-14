package org.school.server.controllers;

import org.school.database.dao.SubjectService;
import org.school.data.request.SubjectCreationData;
import org.school.server.response.ErrorResponse;
import org.school.server.response.Response;
import org.school.server.response.SuccessResponse;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
public class SubjectController {

    private final SubjectService subjectService = new SubjectService();

    @POST
    @Path("/create")
    @RolesAllowed(value="ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubject(SubjectCreationData data) {
        try {
            return new SuccessResponse(subjectService.create(data));
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }
    }

    @GET
    @Path("/list")
    @RolesAllowed(value="ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listSubjects() {
        return new SuccessResponse(subjectService.getAll());
    }
}

package org.school.server.controllers;

import org.school.database.dao.SubjectService;
import org.school.data.SubjectCreationData;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
public class SubjectController {

    private final SubjectService subjectService = new SubjectService();

    @POST
    @Path("/create")
    @RolesAllowed(value="ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSubject(SubjectCreationData data) throws Exception {
        try {
            subjectService.create(data);
        } catch (Exception e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
        return "Subject " + data.name() + " created.";
    }
}

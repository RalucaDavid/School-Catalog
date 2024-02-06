package org.school.server.controllers;

import io.dropwizard.auth.Auth;
import org.school.data.request.SubjectAddGradeData;
import org.school.data.request.TeacherAddRemoveData;
import org.school.database.dao.SubjectService;
import org.school.data.request.SubjectCreationData;
import org.school.database.models.UserEntity;
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
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSubject(SubjectCreationData data)
    {
        try
        {
            return new SuccessResponse(subjectService.create(data));
        }
        catch (Exception e)
        {
            return new ErrorResponse(e.getMessage());
        }
    }

    @GET
    @Path("/list")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listSubjects() {
        return new SuccessResponse(subjectService.getAll());
    }

    @PUT
    @Path("/teacher")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTeacher(TeacherAddRemoveData data)
    {
        try
        {
            return new SuccessResponse(subjectService.addTeacher(data));
        }
        catch (Exception e)
        {
            return new ErrorResponse(e.getMessage());
        }
    }

    @DELETE
    @Path("/teacher")
    @RolesAllowed({"ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeTeacher(TeacherAddRemoveData data)
    {
        try
        {
            subjectService.removeTeacher(data);
            return new SuccessResponse(true);
        }
        catch (Exception e)
        {
            return new ErrorResponse(e.getMessage());
        }
    }

    @GET
    @Path("/teaching")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTeachingSubjects(@Auth UserEntity user)
    {
        return new SuccessResponse(subjectService.getTeachingSubjects(user.getId()));
    }

    @GET
    @Path("/learning")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLearningSubjectsAndGrades(@Auth UserEntity user)
    {
        return new SuccessResponse(subjectService.getLearningSubjects(user.getId()));
    }

    @GET
    @Path("/students/{subjectId}")
    @RolesAllowed({"TEACHER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSubjectStudents(@Auth UserEntity user, @PathParam("subjectId") long subjectId)
    {
        if (!user.isSuperUser() && user.getTeachingSubjects().stream().anyMatch(ut -> ut.getSubjectId() == subjectId))
            return new ErrorResponse("You're not allowed to view this page.");

        return new SuccessResponse(subjectService.getSubjectStudents(subjectId));
    }

    @PUT
    @Path("/student")
    @RolesAllowed({"TEACHER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStudent(@Auth UserEntity user, TeacherAddRemoveData data)
    {

        if (!user.isSuperUser() && user.getTeachingSubjects().stream().anyMatch(ut -> ut.getSubjectId() == data.subjectId()))
            return new ErrorResponse("You're not allowed to modify this subject.");

        try
        {
            return new SuccessResponse(subjectService.addStudent(data));
        }
        catch (Exception e)
        {
            return new ErrorResponse(e.getMessage());
        }
    }

    @PUT()
    @Path("/grade")
    @RolesAllowed({"TEACHER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGrade(@Auth UserEntity user, SubjectAddGradeData data)
    {
        try
        {
            return new SuccessResponse(subjectService.addGrade(user, data));
        }
        catch (Exception e)
        {
            return new ErrorResponse(e.getMessage());
        }
    }

    @PUT()
    @Path("/final-grade/{userLearningSubjectId}")
    @RolesAllowed({"TEACHER"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGrade(@Auth UserEntity user, @PathParam("userLearningSubjectId") long userLearningSubjectId)
    {
        try
        {
            return new SuccessResponse(subjectService.setFinalGrade(userLearningSubjectId));
        }
        catch (Exception e)
        {
            return new ErrorResponse(e.getMessage());
        }
    }

}

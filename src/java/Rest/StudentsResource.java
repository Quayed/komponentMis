/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.DatabaseConfig;
import DAL.IStudentDAO;
import DAL.StudentDAO;
import DTO.StudentDTO;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("Students")
public class StudentsResource {

    @Context
    private UriInfo context;
    private IStudentDAO dao;

    /**
     * Creates a new instance of StudentsResource
     */
    public StudentsResource() {
        try {
            dao = new StudentDAO(DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOverview() {
        StudentDTO[] students = dao.getStudents();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (StudentDTO student : students) {
            arrayBuilder.add(Json.createObjectBuilder()
                .add("details", "/Students/" + student.getStudentId())
                .add("studentId", student.getStudentId())
                .add("name", student.getName())
                .add("status", student.getStatus()));
        }

        JsonArray jsonArray = arrayBuilder.build();

        return new JsonHelper().jsonArrayToString(jsonArray);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("id") String studentId) {

        if (studentId.length() != 7)
            throw new WebApplicationException(405);

        StudentDTO student = dao.getStudent(studentId);

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("details", "/Students/" + student.getStudentId())
                .add("studentId", student.getStudentId())
                .add("name", student.getName())
                .add("status", student.getStatus())
                .build();

        return new JsonHelper().jsonObjectToString(jsonObject);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createStudent(StudentDTO student) {
        int returnStatus = dao.createStudent(student);
        if (returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponent(@PathParam("id") String studentId, StudentDTO student) {

        if (studentId.length() != 7)
            throw new WebApplicationException(405);

        student.setStudentId(studentId);
        int returnStatus = dao.updateStudent(student);
        if (returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteKomponentType(@PathParam("id") String studentId) {

        if (studentId.length() != 7)
            throw new WebApplicationException(405);

        int returnValue = dao.deleteStudent(studentId);

        if (returnValue == 1)
            return "All ok";
        else if (returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }
}

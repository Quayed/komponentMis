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
import javax.ws.rs.core.*;
import java.sql.Connection;
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
    private Connection conn;

    /**
     * Creates a new instance of StudentsResource
     */
    public StudentsResource() {
        try {
            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            dao = new StudentDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOverview(@Context Request request) {
        StudentDTO[] students = dao.getStudents();
        closeConn();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (StudentDTO student : students) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("details", "/Students/" + student.getStudentId())
                    .add("studentId", student.getStudentId())
                    .add("name", student.getName())
                    .add("status", student.getStatus()));
        }

        JsonArray jsonArray = arrayBuilder.build();

        String returnString = new JsonHelper().jsonArrayToString(jsonArray);

        CacheControl cc  = new CacheControl();
        cc.setMaxAge(1);
        cc.setMustRevalidate(true);
        EntityTag etag = new EntityTag(Integer.toString(returnString.hashCode()));
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(etag);

        if (responseBuilder == null){
            responseBuilder = Response.ok(returnString);
            responseBuilder.tag(etag);
        }

        responseBuilder.cacheControl(cc);

        return responseBuilder.build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecific(@PathParam("id") String studentId, @Context Request request) {

        if (studentId.length() != 7)
            throw new WebApplicationException(405);

        StudentDTO student = dao.getStudent(studentId);
        closeConn();

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("details", "/Students/" + student.getStudentId())
                .add("studentId", student.getStudentId())
                .add("name", student.getName())
                .add("status", student.getStatus())
                .build();

        String returnString =  new JsonHelper().jsonObjectToString(jsonObject);

        CacheControl cc  = new CacheControl();
        cc.setMaxAge(1);
        cc.setMustRevalidate(true);
        EntityTag etag = new EntityTag(Integer.toString(returnString.hashCode()));
        Response.ResponseBuilder responseBuilder = request.evaluatePreconditions(etag);

        if (responseBuilder == null){
            responseBuilder = Response.ok(returnString);
            responseBuilder.tag(etag);
        }

        responseBuilder.cacheControl(cc);

        return responseBuilder.build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createStudent(StudentDTO student) {
        int returnStatus = dao.createStudent(student);
        closeConn();

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
        closeConn();

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
        closeConn();

        if (returnValue == 1)
            return "All ok";
        else if (returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }

    private void closeConn() {
        // This method is used to close the connection to the database

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

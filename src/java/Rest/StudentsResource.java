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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.json.stream.JsonParser;
import org.glassfish.json.JsonParserImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.container.ContainerRequestContext;

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
            /*InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/KomponentMis");
            Connection conn = ds.getConnection();
            dao = new StudentDAO(conn);*/
        dao = new StudentDAO(DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD));
        } catch (SQLException e) {
            e.printStackTrace();
        } /*catch (NamingException ex) {
            Logger.getLogger(StudentsResource.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOverview() {
        StudentDTO[] students = dao.getStudents();
        StringBuilder output = new StringBuilder();
        output.append("[");
        for(StudentDTO student : students){
            output.append("{");
            output.append("\"details\": \"/Students/" + student.getStudentId() + "\"");
            output.append(", \"studentId\": \"" + student.getStudentId() + "\"");
            output.append(", \"name\": \"" + student.getName() + "\"");
            output.append(", \"status\": " + student.getStatus());
            output.append("},");
        }
        output.deleteCharAt(output.length()-1);
        output.append("]");
        
        return output.toString();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("id") String studentId){

        if(studentId.length() != 7)
            throw new WebApplicationException(405);
        
        StudentDTO student = dao.getStudent(studentId);
        
        StringBuilder output = new StringBuilder();
        output.append("{");
        output.append("\"studentId\": \"" + student.getStudentId() + "\"");
        output.append(", \"name\": \"" + student.getName() + "\"");
        output.append(", \"status\": " + student.getStatus());
        output.append("}");
        
        return output.toString();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createStudent(StudentDTO student){
        int returnStatus = dao.createStudent(student);
        if(returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }
    
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponent(@PathParam("id") String studentId, StudentDTO student){
        
        if(studentId.length() != 7)
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
    public String deleteKomponentType(@PathParam("id") String studentId){

        if(studentId.length() != 7)
            throw new WebApplicationException(405);

        int returnValue = dao.deleteStudent(studentId);
        
        if(returnValue == 1)
            return "All ok";
        else if(returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }
}

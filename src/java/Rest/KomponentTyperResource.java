/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentGroupDAO;
import DAL.ComponentGroupDTO;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("KomponentTyper")
public class KomponentTyperResource {

    @Context
    private UriInfo context;
    private ComponentGroupDAO dao;

    /**
     * Creates a new instance of KomponentTyperResource
     */
    public KomponentTyperResource() {
        try {
            dao = new ComponentGroupDAO(DriverManager.getConnection("jdbc:mysql://localhost:3306/komponentMis?zeroDateTimeBehavior=convertToNull", "root", "root"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Method for getting all of the komponentTyper
     * returns a JSON array of komponentTyper
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOverview() {
        ComponentGroupDTO[] komponentTyper = dao.getComponentGroups();
        StringBuilder output = new StringBuilder();
        output.append("[");
        for(ComponentGroupDTO komponentType: komponentTyper){
            output.append("{");
            output.append("\"details\": \"/KomponentTyper/" + komponentType.getId() + "\"");
            output.append(", \"id\": " + komponentType.getId());
            output.append(", \"navn\": \"" + komponentType.getNavn() + "\"");
            output.append(", \"standardUdlånstid\": \"" + komponentType.getStandardLoanDuration() + "\"");
            output.append("},");
        }
        output.deleteCharAt(output.length()-1);
        output.append("]");
        return output.toString();
    }
    
    /**
     * Get Method for getting a single komponentType from an id
     * returns a single Json-Object representing a komponentType
     */
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("id") String id){
        //Check that ID is actually a number
        if(!id.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        
        ComponentGroupDTO komponentType = dao.getComponentGroup(Integer.parseInt(id));
        StringBuilder output = new StringBuilder();
        output.append("{");
        output.append("\"id\": " + komponentType.getId());
        output.append(", \"navn\": \"" + komponentType.getNavn() + "\"");
        output.append(", \"standardUdlånstid\": \"" + komponentType.getStandardLoanDuration() + "\"");
        output.append("}");
        
        return output.toString();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createKomponentType(ComponentGroupDTO komponentType){
        dao.createComponentGroup(komponentType);
        return "All Ok";
    }
    
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateKomponentType(@PathParam("id") String id, ComponentGroupDTO komponentType){
        //Check that ID is actually a number
        if(!id.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        komponentType.setId(Integer.parseInt(id));
        dao.updateComponentGroups(komponentType);
        return "All Ok";
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteKomponentType(@PathParam("id") String id){
        //Check that ID is actually a number
        if(!id.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        int returnValue = dao.deleteComponentGroup(Integer.parseInt(id));
        if(returnValue == 1){
            return "All ok";
        } else if(returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }
}

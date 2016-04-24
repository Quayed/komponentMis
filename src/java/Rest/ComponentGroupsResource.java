/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentGroupDAO;
import DTO.ComponentGroupDTO;
import DAL.DatabaseConfig;
import DAL.IComponentGroupDAO;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("ComponentGroups")
public class ComponentGroupsResource {

    @Context
    private UriInfo context;
    private IComponentGroupDAO dao;

    /**
     * Creates a new instance of KomponentTyperResource
     */
    public ComponentGroupsResource() {
        try {
            dao = new ComponentGroupDAO(DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD));
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
        ComponentGroupDTO[] componentGroups = dao.getComponentGroups();
        
        if(componentGroups == null)
            throw new WebApplicationException(500);
        
        StringBuilder output = new StringBuilder();
        output.append("[");
        for(ComponentGroupDTO componentGroup: componentGroups){
            output.append("{");
            output.append("\"details\": \"/ComponentGroups/" + componentGroup.getComponentGroupId() + "\"");
            output.append(", \"componentGroupId\": " + componentGroup.getComponentGroupId());
            output.append(", \"name\": \"" + componentGroup.getName() + "\"");
            output.append(", \"standardLoanDuration\": \"" + componentGroup.getStandardLoanDuration() + "\"");
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
    public String getSpecific(@PathParam("id") String componentGroupId){
        //Check that ID is actually a number
        if(!componentGroupId.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        ComponentGroupDTO componentGroup = dao.getComponentGroup(Integer.parseInt(componentGroupId));
        
        if(componentGroup == null)
            throw new WebApplicationException(404);
        
        StringBuilder output = new StringBuilder();
        output.append("{");
        output.append("\"componentGroupId\": " + componentGroup.getComponentGroupId());
        output.append(", \"name\": \"" + componentGroup.getName() + "\"");
        output.append(", \"standardLoanDuration\": \"" + (componentGroup.getStandardLoanDuration() == null ? "" : componentGroup.getStandardLoanDuration()) + "\"");
        output.append("}");
        
        return output.toString();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createComponentGroup(ComponentGroupDTO componentGroup){
        
        int returnStatus = dao.createComponentGroup(componentGroup);
        if(returnStatus == 1)
            return "All Ok";
        else
            System.out.println("Error");
            throw new WebApplicationException(500);
    }
    
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponentGroup(@PathParam("id") String id, ComponentGroupDTO componentGroup){
        //Check that ID is actually a number
        if(!id.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        
        componentGroup.setComponentGroupId(Integer.parseInt(id));
        int returnStatus = dao.updateComponentGroups(componentGroup);
        if(returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
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

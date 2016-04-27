/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentGroupDAO;
import DAL.DatabaseConfig;
import DAL.IComponentGroupDAO;
import DTO.ComponentGroupDTO;

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

        if (componentGroups == null)
            throw new WebApplicationException(500);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (ComponentGroupDTO componentGroup : componentGroups) {
            arrayBuilder.add(Json.createObjectBuilder()
                .add("details", "ComponentGroups/" + componentGroup.getComponentGroupId())
                .add("componentGroupId", componentGroup.getComponentGroupId())
                .add("name", componentGroup.getName())
                .add("standardLoanDuration", componentGroup.getStandardLoanDuration())
                .add("status", componentGroup.getStatus()));

        }
        JsonArray jsonArray = arrayBuilder.build();


        return new JsonHelper().jsonArrayToString(jsonArray);
    }

    /**
     * Get Method for getting a single komponentType from an id
     * returns a single Json-Object representing a komponentType
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("id") String componentGroupId) {
        //Check that ID is actually a number
        if (!componentGroupId.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }
        ComponentGroupDTO componentGroup = dao.getComponentGroup(Integer.parseInt(componentGroupId));

        if (componentGroup == null)
            throw new WebApplicationException(404);


        JsonObject jsonObject = Json.createObjectBuilder()
                .add("componentGroupId", componentGroup.getComponentGroupId())
                .add("name", componentGroup.getName())
                .add("standardLoanDuration", componentGroup.getStandardLoanDuration())
                .add("status", componentGroup.getStatus())
                .build();

        return new JsonHelper().jsonObjectToString(jsonObject);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createComponentGroup(ComponentGroupDTO componentGroup) {

        int returnStatus = dao.createComponentGroup(componentGroup);
        if (returnStatus > 0)
            return "{\"componentGroupId\": " + returnStatus + " }";
        else
            System.out.println("Error");
        throw new WebApplicationException(500);
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponentGroup(@PathParam("id") String id, ComponentGroupDTO componentGroup) {
        //Check that ID is actually a number
        if (!id.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }

        componentGroup.setComponentGroupId(Integer.parseInt(id));
        int returnStatus = dao.updateComponentGroups(componentGroup);
        if (returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteKomponentType(@PathParam("id") String id) {
        //Check that ID is actually a number
        if (!id.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }
        int returnValue = dao.deleteComponentGroup(Integer.parseInt(id));
        if (returnValue == 1) {
            return "All ok";
        } else if (returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }
}

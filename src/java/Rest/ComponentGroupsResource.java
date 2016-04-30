/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentDAO;
import DAL.ComponentGroupDAO;
import DAL.DatabaseConfig;
import DAL.IComponentGroupDAO;
import DTO.ComponentDTO;
import DTO.ComponentGroupDTO;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.Connection;
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
    private Connection conn;

    /**
     * Creates a new instance of KomponentTyperResource
     */
    public ComponentGroupsResource() {
        try {
            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            dao = new ComponentGroupDAO(conn);
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


        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                .add("componentGroupId", componentGroup.getComponentGroupId())
                .add("name", componentGroup.getName())
                .add("standardLoanDuration", componentGroup.getStandardLoanDuration())
                .add("status", componentGroup.getStatus());

        // Create list of all the components with this componentGroup

        ComponentDTO[] components = new ComponentDAO(conn).getComponentsFromGroup(Integer.parseInt(componentGroupId));

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for(ComponentDTO component : components){
            jsonArrayBuilder.add(Json.createObjectBuilder()
                    .add("barcode", component.getBarcode())
                    .add("componentGroupId", component.getComponentGroupId()     )
                    .add("componentNumber", component.getComponentNumber())
                    .add("status", component.getStatus()));
        }

        jsonObjectBuilder.add("components", jsonArrayBuilder);

        JsonObject jsonObject = jsonObjectBuilder.build();

        return new JsonHelper().jsonObjectToString(jsonObject);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

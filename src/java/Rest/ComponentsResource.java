/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentDAO;
import DTO.ComponentDTO;
import DAL.ComponentGroupDAO;
import DTO.ComponentGroupDTO;
import DAL.DatabaseConfig;
import DAL.IComponentDAO;

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


/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("Components")
public class ComponentsResource {

    @Context
    private UriInfo context;
    private IComponentDAO dao;
    private Connection conn;

    /**
     * Creates a new instance of KomponentResource
     */
    public ComponentsResource() {
        try {
            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            dao = new ComponentDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves representation of an instance of Rest.ComponentsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOverview() {
        ComponentDTO[] components = dao.getComponents();
        StringBuilder output = new StringBuilder();
        output.append("[");
        for (ComponentDTO component : components) {
            output.append("{");
            output.append("\"details\": \"/Components/" + component.getBarcode() + "\"");
            output.append(", \"barcode\": " + component.getBarcode());
            output.append(", \"componentGroupId\": " + component.getComponentGroupId());
            output.append(", \"name\" : \"" + component.getComponentGroup().getName() + "\"");
            output.append(", \"componentNumber\": " + component.getComponentNumber());
            output.append(", \"status\":" + component.getStatus());
            output.append("},");
        }
        output.deleteCharAt(output.length() - 1);
        output.append("]");
        return output.toString();
    }

    @GET
    @Path("{barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("barcode") String barcode) {
        ComponentDTO component = dao.getComponent(barcode);

        if (component == null)
            throw new WebApplicationException(404);

        ComponentGroupDTO componentGroup = componentGroup = new ComponentGroupDAO(conn).getComponentGroup(component.getComponentGroupId());
        if (componentGroup == null)
            throw new WebApplicationException(500);


        StringBuilder output = new StringBuilder();
        output.append("{");
        output.append("\"barcode\": " + component.getBarcode());

        // Information about the componentGroup
        output.append(", \"componentGroup\": { ");
        output.append("\"componentGroupId\": " + componentGroup.getComponentGroupId());
        output.append(", \"name\": " + "\"" + componentGroup.getName() + "\"");
        output.append(", \"standardLoanDuration\": " + "\"" + (componentGroup.getStandardLoanDuration() == null ? "" : componentGroup.getStandardLoanDuration()) + "\"");
        output.append(", \"standardLoanDuration\": " + componentGroup.getStatus());
        output.append("}");

        output.append(", \"componentNumber\": " + component.getComponentNumber());
        output.append(", \"status\":" + component.getStatus());
        output.append("}");

        return output.toString();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createComponent(ComponentDTO component) {
        int returnStatus = dao.createComponent(component);
        if (returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }

    @POST
    @Path("{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponent(@PathParam("barcode") String barcode, ComponentDTO component) {


        int returnStatus = dao.updateComponent(barcode, component);
        if (returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteKomponentType(@PathParam("id") String id) {

        int returnValue = dao.deleteComponent(id);

        System.out.println(id);
        if (returnValue == 1)
            return "All ok";
        else if (returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }

}

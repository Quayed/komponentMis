/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentDAO;
import DAL.DatabaseConfig;
import DAL.IComponentDAO;
import DAL.LoanDAO;
import DTO.ComponentDTO;

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
    public Response getOverview(@Context Request request) {
        ComponentDTO[] components = dao.getComponents();

        LoanDAO loanDAO = new LoanDAO(conn);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (ComponentDTO component : components) {
            String studentId = loanDAO.getStudentIdForActiveLoan(component.getBarcode());

            arrayBuilder.add(Json.createObjectBuilder()
                    .add("details", "/Components/" + component.getBarcode())
                    .add("barcode", component.getBarcode())
                    .add("componentGroupId", component.getComponentGroupId())
                    .add("name", component.getComponentGroup().getName())
                    .add("componentNumber", component.getComponentNumber())
                    .add("status", component.getStatus())
                    .add("studentId", (studentId == null ? "" : studentId)));
        }

        JsonArray jsonArray = arrayBuilder.build();

        closeConn();

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
    @Path("{barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecific(@PathParam("barcode") String barcode, @Context Request request) {
        ComponentDTO component = dao.getComponent(barcode);
        closeConn();

        if (component == null)
            throw new WebApplicationException(404);

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("barcode", component.getBarcode())
                .add("componentGroup", Json.createObjectBuilder()
                        .add("componentGroupId", component.getComponentGroup().getComponentGroupId())
                        .add("name", component.getComponentGroup().getName())
                        .add("standardLoanDuration", component.getComponentGroup().getStandardLoanDuration())
                        .add("status", component.getComponentGroup().getStatus()))
                .add("componentNumber", component.getComponentNumber())
                .add("status", component.getStatus())
                .build();

        String returnString = new JsonHelper().jsonObjectToString(jsonObject);

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
    @Produces(MediaType.APPLICATION_JSON)
    public String createComponent(ComponentDTO component) {
        int returnStatus = dao.createComponent(component);
        closeConn();

        if (returnStatus > 99999999)
            return "{\"barcode\": " + returnStatus + " }";
        else
            throw new WebApplicationException(500);
    }

    @POST
    @Path("{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponent(@PathParam("barcode") String barcode, ComponentDTO component) {

        int returnStatus = dao.updateComponent(barcode, component);
        closeConn();

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

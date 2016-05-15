/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.DatabaseConfig;
import DAL.ILoanDAO;
import DAL.LoanDAO;
import DTO.LoanDTO;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("Loans")
public class LoansResource {

    @Context
    private UriInfo context;
    private ILoanDAO dao;
    private Connection conn;
    private Format formatter = new SimpleDateFormat("dd/MM/yyyy");

    public LoansResource() {
        // Setup the connection and dao as soon as the resource is created.
        try {
            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            dao = new LoanDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOverview(@Context Request request) {
        // Gets all loans from the database, using the DAO
        LoanDTO[] loans = dao.getLoans();
        closeConn();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        // Builds a JSON array of all the loans
        for (LoanDTO loan : loans) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("details", "/loans/" + loan.getLoanId())
                    .add("loanId", loan.getLoanId())
                    .add("barcode", loan.getBarcode())
                    .add("studentId", loan.getStudentId())
                    .add("loanDate", loan.getLoanDate())
                    .add("dueDate", loan.getDueDate())
                    .add("deliveryDate", (loan.getDeliveryDate() != null ? loan.getDeliveryDate() : ""))
                    .add("deliveredTo", (loan.getDeliveredTo() != null ? loan.getDeliveredTo() : ""))
                    .add("mailCount", loan.getMailCount()));
        }

        JsonArray jsonArray = arrayBuilder.build();

        // Converts json array to string using help class
        String returnString = new JsonHelper().jsonArrayToString(jsonArray);

        // Sets up cache to validate every time
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
    public Response getSpecific(@PathParam("id") String loanId, @Context Request request) {
        // Check that the id supplied is actually an id.
        if (!loanId.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }

        LoanDTO loan = dao.getLoan(Integer.parseInt(loanId));
        closeConn();

        if (loan == null)
            throw new WebApplicationException(404);

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("loanId", loan.getLoanId())
                .add("component", Json.createObjectBuilder()
                        .add("barcode", loan.getComponent().getBarcode())
                        .add("componentGroup", Json.createObjectBuilder()
                                .add("componentGroupId", loan.getComponent().getComponentGroup().getComponentGroupId())
                                .add("name", loan.getComponent().getComponentGroup().getName())
                                .add("standardLoanDuration", loan.getComponent().getComponentGroup().getStandardLoanDuration())
                                .add("status", loan.getComponent().getComponentGroup().getStatus()))
                        .add("componentNumber", loan.getComponent().getComponentNumber())
                        .add("status", loan.getComponent().getStatus()))
                .add("student", Json.createObjectBuilder()
                        .add("studentId", loan.getStudent().getStudentId())
                        .add("name", loan.getStudent().getName())
                        .add("status", loan.getStudent().getStatus()))
                .add("loanDate", loan.getLoanDate())
                .add("dueDate", loan.getDueDate())
                .add("deliveryDate", (loan.getDeliveryDate() != null ? loan.getDeliveryDate() : ""))
                .add("deliveredTo", (loan.getDeliveredTo() != null ? loan.getDeliveredTo() : ""))
                .add("mailCount", loan.getMailCount())
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
    public String createComponentGroup(LoanDTO loan) {
        int returnStatus = dao.createLoan(loan);
        closeConn();

        if (returnStatus > 0)
            return "{\"loanId\": " + returnStatus + " }";
        else
            System.out.println(returnStatus);
        throw new WebApplicationException(500);
    }


    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponentGroup(@PathParam("id") String loanId, LoanDTO loan) {
        //Check that ID is actually a number
        if (!loanId.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }

        loan.setLoanId(Integer.parseInt(loanId));

        int returnStatus = dao.updateLoan(loan);
        closeConn();

        if (returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteKomponentType(@PathParam("id") String loanId) {
        //Check that ID is actually a number
        if (!loanId.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }

        int returnValue = dao.deleteLoan(Integer.parseInt(loanId));
        closeConn();

        if (returnValue == 1) {
            return "All ok";
        } else if (returnValue == -2)
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

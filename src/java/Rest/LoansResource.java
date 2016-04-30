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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
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

    /**
     * Creates a new instance of LoansResource
     */
    public LoansResource() {
        try {
            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            dao = new LoanDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getOverview() {
        LoanDTO[] loans = dao.getLoans();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

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

        return new JsonHelper().jsonArrayToString(jsonArray);
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("id") String loanId) {
        // Check that the id supplied is actually an id.
        if (!loanId.matches("^\\d+$")) {
            throw new WebApplicationException(405);
        }

        LoanDTO loan = dao.getLoan(Integer.parseInt(loanId));

        if (loan == null)
            throw new WebApplicationException(404);

        System.out.println(loan.getComponent().getComponentGroup().getStatus());

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("loanId", loan.getLoanId())
                .add("compoent", Json.createObjectBuilder()
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

        return new JsonHelper().jsonObjectToString(jsonObject);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createComponentGroup(LoanDTO loan) {
        int returnStatus = dao.createLoan(loan);
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

        if (returnValue == 1) {
            return "All ok";
        } else if (returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }

}

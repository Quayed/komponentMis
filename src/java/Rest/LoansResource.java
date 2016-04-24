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
import DAL.ILoanDAO;
import DAL.LoanDAO;
import DTO.LoanDTO;
import DAL.StudentDAO;
import DTO.StudentDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;
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
        StringBuilder output = new StringBuilder();
        output.append("[");
        for(LoanDTO loan : loans){
            output.append("{");
            output.append("\"details\": \"/Loans/" + loan.getLoanId() + "\"");
            output.append(", \"loanId\": " + loan.getLoanId());
            output.append(", \"componentId\": " + loan.getComponentId());
            output.append(", \"studentId\": \"" + loan.getStudentId() + "\"");
            output.append(", \"loanDate\": \"" + loan.getLoanDate() + "\"");
            output.append(", \"dueDate\": \"" + loan.getDueDate() + "\"");
            output.append(", \"deliveryDate\": \"" + (loan.getDeliveryDate() != null ? loan.getDeliveryDate() : "") + "\"");
            output.append(", \"deliveredTo\": \"" + (loan.getDeliveredTo() != null ? loan.getDeliveredTo() : "") + "\"");
            output.append("},");
        }
        output.deleteCharAt(output.length()-1);
        output.append("]");
        
        return output.toString();
    }

    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpecific(@PathParam("id") String loanId){
        // Check that the id supplied is actually an id.
        if(!loanId.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }  
        
        LoanDTO loan = dao.getLoan(Integer.parseInt(loanId));
        
        if (loan == null)
            throw new WebApplicationException(404);
        
        ComponentDTO component = new ComponentDAO(conn).getComponent(loan.getComponentId());
        
        ComponentGroupDTO componentGroup = new ComponentGroupDAO(conn).getComponentGroup(component.getComponentGroupId());
        
        StudentDTO student = new StudentDAO(conn).getStudent(loan.getStudentId());
        
        StringBuilder output = new StringBuilder();
        output.append("{");
        output.append("\"loanId\": " + loan.getLoanId());
        output.append(", \"component\": "); 
        
        // Adding the component information
            output.append("{");
            output.append("\"componentId\": " + component.getComponentId());

            // Information about the componentGroup
            output.append(", \"componentGroup\": { ");
            output.append(" \"componentGroupId\": " + componentGroup.getComponentGroupId());
            output.append(", \"name\": " + "\"" + componentGroup.getName() +  "\"");
            output.append(", \"standardLoanDuration\": " + "\"" + (componentGroup.getStandardLoanDuration() == null ? "" : componentGroup.getStandardLoanDuration())  + "\"");
            output.append(", \"standardLoanDuration\": " + componentGroup.getStatus());
            output.append("}");

            output.append(", \"componentNumber\": " + component.getComponentNumber());
            output.append(", \"barcode\": \"" + component.getBarcode() + "\"");
            output.append(", \"status\":" + component.getStatus());
            output.append("}");
            
        output.append(", \"student\": ");
        
        //Adding the student information
            output.append("{");
            output.append("\"studentId\": \"" + student.getStudentId() + "\"");
            output.append(", \"name\": \"" + student.getName() + "\"");
            output.append(", \"status\": " + student.getStatus());
            output.append("}");
            
        output.append(", \"loanDate\": \"" + loan.getLoanDate() + "\"");
        output.append(", \"dueDate\": \"" + loan.getDueDate() + "\"");
        output.append(", \"deliveryDate\": \"" + (loan.getDeliveryDate() != null ? loan.getDeliveryDate() : "") + "\"");
        output.append(", \"deliveredTo\": \"" + (loan.getDeliveredTo() != null ? loan.getDeliveredTo() : "") + "\"");
        output.append("}");  
        
        
        return output.toString();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createComponentGroup(LoanDTO loan){
        int returnStatus = dao.createLoan(loan);
        if(returnStatus == 1)
            return "All Ok";
        else
            System.out.println(returnStatus);
            throw new WebApplicationException(500);
    }
    
    
    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateComponentGroup(@PathParam("id") String loanId, LoanDTO loan){
         //Check that ID is actually a number
        if(!loanId.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        
        loan.setLoanId(Integer.parseInt(loanId));
        int returnStatus = dao.updateLoan(loan);
        if(returnStatus == 1)
            return "All Ok";
        else
            throw new WebApplicationException(500);
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteKomponentType(@PathParam("id") String loanId){
        //Check that ID is actually a number
        if(!loanId.matches("^\\d+$")){
            throw new WebApplicationException(405);
        }
        int returnValue = dao.deleteLoan(Integer.parseInt(loanId));
        
        if(returnValue == 1){
            return "All ok";
        } else if(returnValue == -2)
            throw new WebApplicationException(404);
        else
            throw new WebApplicationException(500);
    }
    
}

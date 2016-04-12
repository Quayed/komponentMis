/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.DatabaseConfig;
import DAL.ILoanDAO;
import DAL.LoanDAO;
import DAL.LoanDTO;
import DAL.StudentDAO;
import DAL.StudentDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
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
            output.append(", \"loanDate\": \"" + formatter.format(loan.getLoanDate()) + "\"");
            output.append(", \"dueDate\": \"" + formatter.format(loan.getDueDate()) + "\"");
            output.append(", \"deliveryDate\": \"" + (loan.getDeliveryDate() != null ? formatter.format(loan.getDeliveryDate()) : "") + "\"");
            output.append(", \"deliveredTo\": \"" + (loan.getDeliveredTo() != null ? loan.getDeliveredTo() : "") + "\"");
            output.append("}");
        }
        output.deleteCharAt(output.length()-1);
        output.append("]");
        
        return output.toString();
    }

    
}

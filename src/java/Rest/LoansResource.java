/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.DatabaseConfig;
import DAL.ILoanDAO;
import DAL.LoanDAO;
import DAL.StudentDAO;
import java.sql.DriverManager;
import java.sql.SQLException;
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
    /**
     * Creates a new instance of LoansResource
     */
    public LoansResource() {
        try {
            dao = new LoanDAO(DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}

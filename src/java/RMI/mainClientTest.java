/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import security.TokenHandler;

/**
 *
 * @author hippomormor
 */
public class mainClientTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
       /*IDatabaseRMI databaseRMI = (IDatabaseRMI) Naming.lookup("rmi://ec2-52-58-114-24.eu-central-1.compute.amazonaws.com/databaseRMI");
    
        // SQL        
        Connection conn = null;   
        try {
         conn = DriverManager.getConnection("jdbc:mysql://" + DatabaseConfig.ENDPOINT,
                                    DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
        } catch (SQLException ex) {         
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: "     + ex.getSQLState());
            System.out.println("VendorError: "  + ex.getErrorCode());
        }
        TokenHandler tokenhandler = new TokenHandler("bruger", "kode");
        tokenhandler.generateToken(tokenhandler.generateRandom());
        int answer = databaseRMI.generateToken(tokenhandler.getRandomToken());
        if (tokenhandler.checkToken(answer))
            System.out.println("True");
        else
            System.out.println("False");        
       
        System.out.println(databaseRMI.getTest(tokenhandler.getPublicToken()).getName());*/
    }   
}

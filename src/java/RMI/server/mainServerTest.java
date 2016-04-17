/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import DAL.DatabaseConfig;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hippomormor
 */

public class mainServerTest {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.rmi.NotBoundException
     * @throws java.net.MalformedURLException
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        
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
              
        // RMI         
        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");   
        DatabaseRMI databaseRMI = new DatabaseRMI(conn);
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        java.rmi.registry.LocateRegistry.createRegistry(1099);
        Naming.rebind("rmi://127.0.0.1/databaseRMI", databaseRMI);
        System.out.println("Server running..");   
    }   
}
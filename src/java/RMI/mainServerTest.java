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
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        
        //java.rmi.registry.LocateRegistry.createRegistry(1099);
        //System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Connection conn = null;
        
        try {
         conn = DriverManager.getConnection("jdbc:mysql://" + DatabaseConfig.ENDPOINT,
                                    DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
        } catch (SQLException ex) {         
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: "     + ex.getSQLState());
            System.out.println("VendorError: "  + ex.getErrorCode());
        }
    }   
}

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
import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hippomormor
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.rmi.NotBoundException
     * @throws java.net.MalformedURLException
     */
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        // Check shell arguments
        if (args.length == 1)
            System.setProperty("java.rmi.server.hostname", args[0]);
        else
            System.setProperty("java.rmi.server.hostname", "52.28.66.187");
        System.out.println(">> Remember to run in terminal <<");

        // Log-in
        boolean granted = false;
        Brugeradmin brugerAdmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Console console;
        String user;
        char[] pass;

        while (true) {
            try {
                console = System.console();
                if (console != null) {
                    user = console.readLine("Name: ");
                    pass = console.readPassword("Password: ");
                    try {
                        Bruger bruger = brugerAdmin.hentBruger(user, new String(pass));
                        if (bruger != null) {
                            granted = true;
                            System.out.println("User accepted");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Wrong password");
                    }
                }
            } catch (Exception ex) {
                System.out.println("Must be run from console");
                System.exit(-1);
            }
        }

        if (granted) {

            // SQL        
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + DatabaseConfig.ENDPOINT,
                        DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }

            // RMI            
            DatabaseRMI databaseRMI = new DatabaseRMI(conn, user, new String(pass));
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://127.0.0.1/databaseRMI", databaseRMI);
            System.out.println("Server running..");
        }
    }
}

/*      
        ComponentGroupDAO cDAO = new ComponentGroupDAO(conn);
        ComponentGroupDTO cDTO = new ComponentGroupDTO(0, "testName", "60", 0);
        cDAO.createComponentGroup(cDTO);
        cDAO.getComponentGroup(0).getName();
 */

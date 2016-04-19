/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import brugerautorisation.data.Bruger;
import brugerautorisation.transport.rmi.Brugeradmin;
import java.io.Console;
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
public class ClientMainTEMPLATE {

    /**
     * @param args the command line arguments
     * @throws java.rmi.NotBoundException
     * @throws java.net.MalformedURLException
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
       /*
        System.out.println(">> Remember to run in terminal <<");
        boolean granted = false;

        // Log-in
        Brugeradmin brugerAdmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Console console;

        while (true) {
            try {
                console = System.console();
                if (console != null) {
                    String user = console.readLine("Name: ");
                    char[] pass = console.readPassword("Password: ");
                    try {
                        Bruger bruger = brugerAdmin.hentBruger(user,new String(pass));
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
            IDatabaseRMI databaseRMI = (IDatabaseRMI) Naming.lookup("rmi://54.93.88.60/databaseRMI");

            TokenHandler tokenhandler = new TokenHandler("bruger", "kode");
            tokenhandler.generateToken(tokenhandler.generateRandom());
            int answer = databaseRMI.generateToken(tokenhandler.getRandomToken());
            if (tokenhandler.checkToken(answer)) {
                System.out.println("True");
            } else {
                System.out.println("False");
            }

            System.out.println(databaseRMI.getTest(tokenhandler.getPublicToken()).getName());

        }  */     
    }
}

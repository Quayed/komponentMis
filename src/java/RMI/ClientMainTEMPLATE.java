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
    /*  System.out.println(">> Remember to run in terminal <<");
        boolean granted = false;
        String user;
        char[] pass;
        
        // Log-in
        Brugeradmin brugerAdmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Console console;

        while (true) {
            try {
                console = System.console();
                if (console != null) {
                    user = console.readLine("Name: ");
                    pass = console.readPassword("Password: ");
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

            // RMI        
            IDatabaseRMI databaseRMI = (IDatabaseRMI) Naming.lookup("rmi://54.93.88.60/databaseRMI");

            TokenHandler tokenhandler = new TokenHandler(user, new String(pass));
            
            tokenhandler.generateKey(databaseRMI.exchangeTokens(tokenhandler.getPublicToken()));
            
            BigInteger answer = databaseRMI.exchangeKeys(tokenhandler.getKeyToken());
            
            if (tokenhandler.checkKey(answer)) {
                System.out.println("True");
            } else {
                System.out.println("False");
            }

            System.out.println(databaseRMI.getTest(tokenhandler.getPublicToken()).getName());
        }    */   
    }
}

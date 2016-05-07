
package RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import DAL.DatabaseConfig;
import Mail.MailHandler;
import brugerautorisation.data.Bruger;
import java.io.Console;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author Christian Genter
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
            System.setProperty("java.rmi.server.hostname", "54.93.171.44");
        
        System.out.println(">> Remember to run in terminal <<");

        // Log-in
        boolean granted = false;
        
        // Connect to brugerautorisation at javabog (SOAP)
        URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
        QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
        Service service = Service.create(url, qname);
        brugerautorisation.transport.soap.Brugeradmin brugerAdmin = service.getPort(brugerautorisation.transport.soap.Brugeradmin.class);

        
        Console console;
        String user;
        char[] pass;

        // Get credentials 
        while (true) {
            try {
                console = System.console();
                if (console != null) {
                    user = console.readLine("Name: ");
                    
                    // Hide password
                    pass = console.readPassword("Password: ");
                    try {
                        // Check user with brugerautorisation at javabog
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
                conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT,
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
            
            // Mail
            MailHandler mailhandler = new MailHandler(conn);
            Thread mailThread = new Thread(mailhandler);
            mailThread.start();
            System.out.println("MailHandler running..");
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.DatabaseConfig;
import DAL.ITokenDAO;
import DAL.TokenDAO;
import DTO.TokenDTO;
import brugerautorisation.data.Bruger;
import security.TokenHandlerServer;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("Login")
public class LoginResource {

    @Context
    private UriInfo context;


    public LoginResource() {
    }


    /**
     * Login Resource used to get a token required for every other resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(TokenDTO credentials){
        Connection conn = null;
        TokenHandlerServer tokenHandler = null;

        // check that credentials has been set.
        if (credentials.getUsername() == null || credentials.getUsername().equals("") || credentials.getPassword() == null || credentials.getPassword().equals(""))
            throw new WebApplicationException(400);

        try {

            URL url = new URL("http://javabog.dk:9901/brugeradmin?wsdl");
            QName qname = new QName("http://soap.transport.brugerautorisation/", "BrugeradminImplService");
            Service service = Service.create(url, qname);
            brugerautorisation.transport.soap.Brugeradmin brugerAdmin = service.getPort(brugerautorisation.transport.soap.Brugeradmin.class);

            // Verify the user with brugerautorisation made by Jakob.
            Bruger user = brugerAdmin.hentBruger(credentials.getUsername(), credentials.getPassword());
            if (user == null)
                throw new WebApplicationException(401);

            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);

            tokenHandler = new TokenHandlerServer(credentials.getUsername(), credentials.getPassword());

            ITokenDAO dao = new TokenDAO(conn);
            TokenDTO dto = new TokenDTO();
            dto.setToken(tokenHandler.getPublicToken().toString());

            if(dao.createToken(dto) == 1){
                // Return the new token to the user
                return "{\"Access-token\" : \"" + tokenHandler.getPublicToken().toString() + "\"}";
            } else{
                // Something went wrong, return internal server error
                throw new WebApplicationException(500);
            }
        } catch (SQLException | MalformedURLException e) {
            // print stack trace, so it can be viewed in the log
            e.printStackTrace();
            // return with an internal server error.
            throw new WebApplicationException(500);
        }  catch (com.sun.xml.ws.fault.ServerSOAPFaultException ex){
            ex.printStackTrace();
            // unauthorized credentials, so return 401.
            throw new WebApplicationException(401);
        } finally{
            if (conn != null)
                try{
                    conn.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
        }

    }
}

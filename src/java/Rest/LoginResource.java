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
import security.TokenHandlerServer;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
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

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(TokenDTO credentials){
        Connection conn = null;
        TokenHandlerServer tokenHandler = null;

        try {
            conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
            ITokenDAO dao = new TokenDAO(conn);

            do{
                tokenHandler = new TokenHandlerServer(credentials.getUsername(), credentials.getPassword());
            }while(dao.getToken(tokenHandler.getPublicToken().toString()) != null);

            TokenDTO dto = new TokenDTO();
            dto.setToken(tokenHandler.getPublicToken().toString());

            if(dao.createToken(dto) == 1){
                return "{\"token\" : \"" + tokenHandler.getPublicToken().toString() + "\"}";
            } else{
                throw new WebApplicationException(500);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WebApplicationException(500);
        } finally {
            if (conn != null)
                try{
                    conn.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
        }

    }
}

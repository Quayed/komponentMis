/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest.Filters;

import DAL.DatabaseConfig;
import DAL.TokenDAO;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mathias
 */
@Provider
@PreMatching
public class RestRequestFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Check that the token supplied by the user is valid, else abort with 401
        if (!requestContext.getUriInfo().getPath().equals("Login")){
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(DatabaseConfig.ENDPOINT, DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);

                if (!new TokenDAO(conn).isValidToken(requestContext.getHeaderString("Access-token")))
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) try{ conn.close(); } catch(SQLException e){ e.printStackTrace();}
            }
        }

        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        if ( requestContext.getRequest().getMethod().equals( "OPTIONS" ) ) {
            // Just send a OK signal back to the browser
            requestContext.abortWith( Response.status( Response.Status.OK ).build() );
        }
    }
    
}

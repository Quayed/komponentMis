/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

/**
 *
 * @author mathias
 */
public class Helper {
    public static void addHeaders(HttpServletResponse response, ContainerRequestContext requestContext){
        
        if ( requestContext.getRequest().getMethod().equals( "OPTIONS" ) ) {
            System.out.println("Hi there");
            response.addHeader("Access-Control-Allow-Origin", requestContext.getHeaderString("Access-Control-Allow-Origin"));
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            requestContext.abortWith( Response.status( Response.Status.OK ).build() );
        }
        
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    }
}

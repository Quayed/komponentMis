/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest.Filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 *
 * @author mathias
 */
@Provider
@PreMatching
public class RestResponseFilter implements ContainerResponseFilter{

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if(requestContext.getRequest().getMethod().equals( "OPTIONS" )){
            responseContext.getHeaders().add( "Access-Control-Allow-Origin", requestContext.getHeaderString("Origin"));
            responseContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
            responseContext.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, PUT" );
            responseContext.getHeaders().add( "Access-Control-Allow-Headers", requestContext.getHeaderString("Access-Control-Request-Headers") );
        }else{
            responseContext.getHeaders().add( "Access-Control-Allow-Origin", "*" );
            responseContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
            responseContext.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
            if (!requestContext.getRequest().getMethod().equals("GET")){
                // Explicitly turn of caching on all other methods than GET
                responseContext.getHeaders().add("Cache-Control", "no-cache, no-store, must-revalidate");
                responseContext.getHeaders().add("Pragma", "no-cache");
                responseContext.getHeaders().add("Expires", "0");
            }
        }
    }
    
}

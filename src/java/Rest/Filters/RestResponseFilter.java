/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest.Filters;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

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
            responseContext.getHeaders().add( "Access-Control-Allow-Origin", requestContext.getHeaderString("Access-Control-Allow-Origin"));
            responseContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
            responseContext.getHeaders().add( "Access-Control-Allow-Methods", requestContext.getHeaderString("Access-Control-Allow-Methods") );
        }else{
            responseContext.getHeaders().add( "Access-Control-Allow-Origin", "*" );
            responseContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
            responseContext.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
        }
    }
    
}

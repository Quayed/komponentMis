/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest.Filters;

import java.io.IOException;
import javax.servlet.annotation.WebFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author mathias
 */
@Provider
@PreMatching
public class RestRequestFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // When HttpMethod comes as OPTIONS, just acknowledge that it accepts...
        if ( requestContext.getRequest().getMethod().equals( "OPTIONS" ) ) {
            System.out.println("Running");
            // Just send a OK signal back to the browser
            requestContext.abortWith( Response.status( Response.Status.OK ).build() );
        }
    }
    
}

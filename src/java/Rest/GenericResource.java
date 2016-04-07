/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentGroupDTO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of Rest.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getXml() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/komponentMis?zeroDateTimeBehavior=convertToNull", "root", "root");
            ComponentGroupDTO[] komponentTyper = new DAL.ComponentGroupDAO(conn).getComponentGroups();
            String returnString = "";
            for(int i = 0; i < komponentTyper.length; i++){
                returnString += komponentTyper[i].getNavn();
            }
            return returnString;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Hi there";
        //TODO return proper representation object
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}

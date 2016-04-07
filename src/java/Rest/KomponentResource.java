/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rest;

import DAL.ComponentDAO;
import DAL.ComponentDTO;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.stream.JsonParser;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.glassfish.json.JsonParserImpl;

/**
 * REST Web Service
 *
 * @author mathias
 */
@Path("Komponenter")
public class KomponentResource {

    @Context
    private UriInfo context;
    private ComponentDAO dao;
    /**
     * Creates a new instance of KomponentResource
     */
    public KomponentResource() {
        try {
            dao = new ComponentDAO(DriverManager.getConnection("jdbc:mysql://localhost:3306/komponentMis?zeroDateTimeBehavior=convertToNull", "root", "root"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves representation of an instance of Rest.KomponentResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        ComponentDTO[] komponenter = dao.getComponents();
        StringBuilder output = new StringBuilder();
        output.append("{");
        for(ComponentDTO komponent : komponenter){
            output.append("{");
            output.append("\"details\": Komponenter/" + komponent.getComponentId());
            output.append("\"id\" : ");
            output.append(komponent.getComponentId());
            output.append(", \"komponentType\": ");
            output.append(komponent.getComponentGroupId());
            output.append(", \"komponentNummer\": ");
            output.append(komponent.getComponentNumber());
            output.append(", \"stregkode\": ");
            output.append(komponent.getBarcode());
            output.append("}");

        }
        output.append("}");
        return output.toString();
    }

    /**
     * PUT method for updating or creating an instance of KomponentResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}

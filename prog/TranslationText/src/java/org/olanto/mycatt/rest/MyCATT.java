/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author simple
 */
@Path("TranslationText")
public class MyCATT {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MyCATT
     */
    public MyCATT() {
    }

    /**
     * Retrieves representation of an instance of org.olanto.mycatt.rest.MyCATT
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        return "<Qd>"
                + "<htmlRefDoc>"
                + "</htmlRefDoc>"
                + "</QD>";
    }

    /**
     * PUT method for updating or creating an instance of MyCATT
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}

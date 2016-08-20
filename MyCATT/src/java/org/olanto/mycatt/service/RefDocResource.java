/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.service;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author simple
 */
@Path("refdoc")
public class RefDocResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RefDocResource
     */
    public RefDocResource() {
    }

    /**
     * Retrieves representation of an instance of
     * org.olanto.mycatt.service.RefDocResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        return "<QD>"
                + "<htmlRefDoc> Some ref doc here"
                + "</htmlRefDoc>"
                + "</QD>";
    }

    /**
     * PUT method for updating or creating an instance of RefDocResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }

    @POST
    @Consumes("application/xml")
    @Produces("application/xml")
    public String postHandler(String content) {
        return content;
    }
}

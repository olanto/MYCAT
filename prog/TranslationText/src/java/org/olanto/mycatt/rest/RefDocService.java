/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author simple
 */
@Path("RefDoc")
public class RefDocService {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RefDocService
     */
    public RefDocService() {
    }

    /**
     * Retrieves representation of an instance of org.olanto.mycatt.rest.RefDocService
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml(@DefaultValue("TEST") @QueryParam("TxtSrc") String TxtSrc,
            @DefaultValue("Direct XML output") @QueryParam("TxtTgt") String TxtTgt,
            @DefaultValue("EN") @QueryParam("LngSrc") String LngSrc,
            @DefaultValue("FR") @QueryParam("LngTgt") String LngTgt,
            @DefaultValue("WT/TPR;UR;G/AG") @QueryParam("Filter") String Filter,
            @DefaultValue("6") @QueryParam("MinLen") Integer MinLen,
            @DefaultValue("TRUE") @QueryParam("RemFirst") Boolean RemFirst,
            @DefaultValue("FALSE") @QueryParam("Fast") Boolean Fast) {
        return "<Qd>"
                + "<htmlRefDoc>"
                + "</htmlRefDoc>"
                + "</QD>";
    }

    /**
     * PUT method for updating or creating an instance of RefDocService
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}

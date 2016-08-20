/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.olanto.idxvli.server.IndexService_MyCat;
import org.apache.log4j.Logger;
import org.olanto.idxvli.ref.UploadedFile;

/**
 * REST Web Service
 *
 * @author simple
 */
@Path("RefDoc")
public class RefDocService {

    private static final Logger _logger = Logger.getLogger(RefDocService.class);
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RefDocService
     */
    public RefDocService() {
    }

    /**
     * Retrieves representation of an instance of
     * org.olanto.mycatt.rest.RefDocService
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        return "<QD>"
                + "<htmlRefDoc>"
                + "</htmlRefDoc>"
                + "</QD>";
    }

    @GET
    @Produces("application/xml")
    @Path("params")
    public String getRefDocXML(@DefaultValue("TEST") @QueryParam("TxtSrc") String TxtSrc,
            @DefaultValue("Direct XML output") @QueryParam("TxtTgt") String TxtTgt,
            @DefaultValue("EN") @QueryParam("LngSrc") String LngSrc,
            @DefaultValue("FR") @QueryParam("LngTgt") String LngTgt,
            @DefaultValue("WT/TPR;UR;G/AG") @QueryParam("Filter") String Filter,
            @DefaultValue("6") @QueryParam("MinLen") Integer MinLen,
            @DefaultValue("TRUE") @QueryParam("RemFirst") Boolean RemFirst,
            @DefaultValue("FALSE") @QueryParam("Fast") Boolean Fast) {

        String refDoc = "empty ref";
        String[] collections = Filter.split(";", -1);
        try {
            Remote r = Naming.lookup("rmi://localhost/VLI");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                _logger.info(is.getInformation());
                UploadedFile up = new UploadedFile(TxtSrc, TxtTgt);
                refDoc = is.getHtmlReferences(up, MinLen, LngSrc, LngTgt, collections, RemFirst, Fast);
            }

        } catch (NotBoundException | IOException ex) {
            _logger.error(ex);
        }
        return "<QD>"
                + "<htmlRefDoc>"
                + refDoc
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.Logger;
import org.olanto.idxvli.server.IndexService_MyCat;

/**
 * REST Web Service
 *
 * @author simple
 */
@Path("RefDocMerge")
public class RefDocMergeService {

    private static final Logger _logger = Logger.getLogger(RefDocMergeService.class);
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RefDocService
     */
    public RefDocMergeService() {
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
                + "<Info>"
                + "RefDocMerge"
                + "</Info>"
                + "</QD>";
    }

    @GET
    @Produces("application/xml")
    @Path("params")
    public String mergeRefDocXML(@DefaultValue("") @QueryParam("RefType") String RefType,
            @DefaultValue("") @QueryParam("DocSrc1") String DocSrc1,
            @DefaultValue("") @QueryParam("DocSrc2") String DocSrc2,
            @DefaultValue("") @QueryParam("DocTgt") String DocTgt,
            @DefaultValue("") @QueryParam("RepTag1") String RepTag1,
            @DefaultValue("") @QueryParam("RepTag2") String RepTag2,
            @DefaultValue("") @QueryParam("Color2") String Color2) {
        String response = "";
        if (DocSrc1.isEmpty() || DocSrc2.isEmpty() || DocTgt.isEmpty()) {
            response = "ERROR: You need to specifiy source and target documents\n";
        }
        if (RepTag2.isEmpty()) {
            response = "ERROR: You need to specifiy a replacement tag for the target document to merge\n";
        }
        if (Color2.isEmpty()) {
            response = "ERROR: You need to specifiy second document's references color\n";
        }
        try {
            Remote r = Naming.lookup("rmi://localhost/VLI");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                _logger.info(is.getInformation());
                response = is.mergeXMLReferences(RefType, DocSrc1, DocSrc2, DocTgt, RepTag1, RepTag2, Color2);
            }
        } catch (NotBoundException | IOException ex) {
            _logger.error(ex);
            response = "ERROR: RMI call unsuccessful because of unmarshalling issue \n(Check if myCat service is up/ restart tomcat)";
        }
        String result = "<QD>"
                + WSTUtil.niceXMLParams(RefType, DocSrc1, DocSrc2, DocTgt, RepTag1, RepTag2, Color2)
                + "<response>\n"
                + response
                + "</response>\n"
                + "</QD>";
        return result;
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

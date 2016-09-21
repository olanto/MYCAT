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
import org.olanto.idxvli.doc.PropertiesList;
import org.olanto.idxvli.ref.UploadedFile;
import org.olanto.idxvli.server.IndexService_MyCat;

/**
 * REST Web Service
 *
 * @author simple
 */
@Path("RefDoc")
public class RefDocService {

    public final static String COLLECTION_PREFIX = "COLLECTION.";
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
                + "<Info>"
                + "</Info>"
                + "</QD>";
    }

    @GET
    @Produces("application/xml")
    @Path("params")
    public String getRefDocXML(@DefaultValue("") @QueryParam("TxtSrc") String TxtSrc,
            @DefaultValue("") @QueryParam("RefType") String RefType,
            @DefaultValue("") @QueryParam("DocSrc") String DocSrc,
            @DefaultValue("") @QueryParam("DocTgt") String DocTgt,
            @DefaultValue("EN") @QueryParam("LngSrc") String LngSrc,
            @DefaultValue("FR") @QueryParam("LngTgt") String LngTgt,
            @DefaultValue("") @QueryParam("Filter") String Filter,
            @DefaultValue("3") @QueryParam("MinLen") Integer MinLen,
            @DefaultValue("FALSE") @QueryParam("RemFirst") Boolean RemFirst,
            @DefaultValue("FALSE") @QueryParam("Fast") Boolean Fast) {

        String msg = "ok";
        String refDoc = "empty ref";
        boolean fromFile = false;
        // process collection
        String[] collections = null;
        if (!Filter.equals("")) {
            collections = Filter.split(";");
            for (int i = 0; i < collections.length; i++) {
                collections[i] = collections[i].trim(); // remove space
                if (!collections[i].startsWith(COLLECTION_PREFIX)) {
                    collections[i] = COLLECTION_PREFIX + collections[i];  // add prefix if missing 
                }
            }
        }
        if (!TxtSrc.equals("") && !DocSrc.equals("")) {
            msg = "TxtSrc is not null, DocSrc will be ignored";
            fromFile = false;
        }

        try {
            Remote r = Naming.lookup("rmi://localhost/VLI");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                _logger.info(is.getInformation());
                UploadedFile up = null;
                if (!fromFile) {  // text
                    up = new UploadedFile(TxtSrc, null);
                }

                if (collections != null) {  // check if collection are ok
                    //System.out.println("collection nb:" + collections.length);
                    for (int i = 0; i < collections.length; i++) {

                        PropertiesList CollectionsList = is.getDictionnary(collections[i]);

                        //System.out.println("test:" + collections[i] + CollectionsList.result);
                        if (!(CollectionsList.result.length >= 1 && CollectionsList.result != null)) {
                            //System.out.println("test:" + collections[i] + "Error");
                            if (msg.equals("ok")) {
                                msg = ""; // reset msg
                            }
                            msg += "ERROR: This collection: " + collections[i] + " doesnt exist , Filter is considered EMPTY\n";
                            collections = null;
                            break;
                        } else {
                            boolean found = false;
                            for (int j = 0; j < CollectionsList.result.length; j++) {
                                if (CollectionsList.result[j].equals(collections[i])) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                if (msg.equals("ok")) {
                                    msg = ""; // reset msg
                                }
                                msg += "ERROR: This collection: " + collections[i] + " doesnt exist , Filter is set to EMPTY\n";
                                collections = null;
                                break;
                            }
                        }
                    }
                }

                refDoc = is.getXMLReferences(up, MinLen, LngSrc, LngTgt, collections, RemFirst, Fast, fromFile, DocSrc, DocTgt, RefType);

            }
        } catch (NotBoundException | IOException ex) {
            msg = "RMI call unsuccessful because of unmarshalling issue \n(Check if myCat service is up/ restart tomcat)";
            _logger.error(ex);
        }

        String result = "<QD>"
                + WSTUtil.niceXMLParameters(msg, TxtSrc, RefType, DocSrc, DocTgt, LngSrc, LngTgt, Filter, MinLen, RemFirst, Fast)
                + refDoc
                + "</QD>";

        //System.out.println("RefDocService:\n" + result);  // ONLY FOR DEBUG

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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.idxvli.ref;

import java.rmi.Naming;
import java.rmi.Remote;
import org.olanto.convsrv.server.ConvertService;
import org.olanto.idxvli.IdxConstant;
import org.olanto.idxvli.util.BytesAndFiles;

/**
 *
 * @author simple
 */
public class WSRESTUtil {
    
    static String organisationTemplate=null;

    public static void main(String[] args) {
        byte[] bytes = null;
        System.out.println(convertFileWithRMI("C:\\MYCAT\\corpus\\docs\\small-collection\\UNO\\A_RES_53_144_EN.pdf"));
    }

    public static String unCommentRefDoc(String s) {
        s = s.replace("<!--", "</htmlstartcomment>");
        s = s.replace("-->", "</htmlendcomment>");
        return s;
    }

    public static String reCommentRefDoc(String s) {
        s = s.replace("</htmlstartcomment>", "<!--");
        s = s.replace("</htmlendcomment>", "-->");
        return s;
    }

    /*
     * (@DefaultValue("") @QueryParam("TxtSrc") String TxtSrc,
     @DefaultValue("") @QueryParam("TxtTgt") String DocSrc,
     @DefaultValue("") @QueryParam("TxtTgt") String DocTgt,
     @DefaultValue("EN") @QueryParam("LngSrc") String LngSrc,
     @DefaultValue("FR") @QueryParam("LngTgt") String LngTgt,
     @DefaultValue("") @QueryParam("Filter") String Filter,
     @DefaultValue("3") @QueryParam("MinLen") Integer MinLen,
     @DefaultValue("FALSE") @QueryParam("RemFirst") Boolean RemFirst,
     @DefaultValue("FALSE") @QueryParam("Fast") Boolean Fast) 
     */
    public static String niceXMLParameters(String msg, String TxtSrc, String RefType, String DocSrc, String DocTgt,
            String LngSrc, String LngTgt, String[] Filter, Integer MinLen, Boolean RemFirst, Boolean Fast) {

        String collections = "";
        if (Filter != null) {
            for (int i = 0; i < Filter.length; i++) {
                collections += Filter[i] + ";";
            }
        }
        return "<parameters>\n"
                + "   <msg>" + msg + "</msg>\n"
                + "   <TxtSrc>" + TxtSrc + "</TxtSrc>\n"
                + "   <RefType>" + RefType + "</RefType>\n"
                + "   <DocSrc>" + DocSrc + "</DocSrc>\n"
                + "   <DocTgt>" + DocTgt + "</DocTgt>\n"
                + "   <LngSrc>" + LngSrc + "</LngSrc>\n"
                + "   <LngTgt>" + LngTgt + "</LngTgt>\n"
                + "   <Filter>" + collections + "</Filter>\n"
                + "   <MinLen>" + MinLen + "</MinLen>\n"
                + "   <Fast>" + Fast + "</Fast>\n"
                + "</parameters>\n";

    }


    public static String niceXMLInfo(String RefDocFullName, String RefDocType,
            String RefDocLng, String RefDocPerCent, String RefDocOccurences) {

        if (organisationTemplate==null){
            organisationTemplate=BytesAndFiles.file2String(IdxConstant.IDX_XML_ORGANISATION_TEMPLATE, "UTF-8");
             if (organisationTemplate==null)  organisationTemplate="<!-- Error impossible to load: "
                     +IdxConstant.IDX_XML_ORGANISATION_TEMPLATE+"-->";
        }

        return "<statistics>\n"
                + "  <mycat>\n"
                + "    <RefDocFullName>" + RefDocFullName + "</RefDocFullName>\n"
                + "    <RefDocType>" + RefDocType + "</RefDocType>\n"
                + "    <RefDocLng>" + RefDocLng + "</RefDocLng>\n"
                + "    <RefDocPerCent>" + RefDocPerCent + "</RefDocPerCent>\n"
                + "    <RefDocOccurences>" + RefDocOccurences + "</RefDocOccurences>\n"
                + "  </mycat>\n"
                + "  <organisation>\n"
                + "     <!-- imported template from /config -->"
                + organisationTemplate
                + "  </organisation>\n"
                + "</statistics>\n";

    }

    public static String convertFileWithRMI(String fileName) {
        String ret = "Conversion Error";
        System.out.println("Request to convert file from WebService: " + fileName);
        try {
            Remote r = Naming.lookup("rmi://localhost/CONVSRV");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;
                // ret = is.getInformation();


                ret = is.File2Txt(fileName);

                //System.out.println("DEBUG Converted file content: " + ret);

            } else {
                return "Error: CONVSRV Service not found or not compatible.";
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return ret;
    }
}

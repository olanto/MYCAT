/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

/**
 *
 * @author simple
 */
public class WSTUtil {

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

    public static String extractHTML(String s) {
        s = s.replace("<!--", "");
        s = s.replace("-->", "");
        int index1 = s.indexOf("<htmlRefDoc>");
        int index2 = s.indexOf("</htmlRefDoc>");
        s = s.substring(index1, index2);
        s = reCommentRefDoc(s);
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
    public static String niceXMLParameters(String msg, String TxtSrc, String RefType, String DocSrc, String DocTgt, String LngSrc, String LngTgt, String Filter, Integer MinLen, Boolean RemFirst, Boolean Fast) {

        return "<parameters>\n"
                + "   <msg>" + msg + "</msg>\n"
                + "   <TxtSrc>" + TxtSrc + "</TxtSrc>\n"
                + "   <RefType>" + RefType + "</RefType>\n"
                + "   <DocSrc>" + DocSrc + "</DocSrc>\n"
                + "   <DocTgt>" + DocTgt + "</DocTgt>\n"
                + "   <LngSrc>" + LngSrc + "</LngSrc>\n"
                + "   <LngTgt>" + LngTgt + "</LngTgt>\n"
                + "   <Filter>" + Filter + "</Filter>\n"
                + "   <MinLen>" + MinLen + "</MinLen>\n"
                + "   <RemFirst>" + RemFirst + "</RemFirst>\n"
                + "   <Fast>" + Fast + "</Fast>\n"
                + "</parameters>\n";
    }
    
    public static String niceXMLParams(String response, String RefType, String DocSrc1, String DocSrc2, String DocTgt, String RepTag1, String RepTag2, String Color2) {

        return "<params>\n"
                + "   <Response>" + response + "</Response>\n"
                + "   <RefType>" + RefType + "</RefType>\n"
                + "   <DocSrc1>" + DocSrc1 + "</DocSrc1>\n"
                + "   <DocSrc2>" + DocSrc2 + "</DocSrc2>\n"
                + "   <DocTgt>" + DocTgt + "</DocTgt>\n"
                + "   <RepTag1>" + RepTag1 + "</RepTag1>\n"
                + "   <RepTag2>" + RepTag2 + "</RepTag2>\n"
                + "   <Color2>" + Color2 + "</Color2>\n"
                + "</params>\n";
    }
}

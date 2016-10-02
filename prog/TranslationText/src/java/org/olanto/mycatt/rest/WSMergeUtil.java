/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

/**
 *
 * @author simple
 */
public class WSMergeUtil {

    public static String niceXMLParameters(String msg, String RefType, String DocSrc1, String DocSrc2, String DocTgt, String RepTag1, String RepTag2, String Color2) {

        return "<params>\n"
                + "   <msg>" + msg + "</msg>\n"
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

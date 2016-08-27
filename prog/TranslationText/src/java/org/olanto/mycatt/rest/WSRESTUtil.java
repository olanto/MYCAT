/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.olanto.convsrv.server.ConvertService;

/**
 *
 * @author simple
 */


public class WSRESTUtil {
    
    public static void main(String[] args){
        byte[] bytes=null;
        System.out.println(convertFileWithRMI( bytes,
        "C:\\MYCAT\\corpus\\docs\\small-collection\\UNO\\A_RES_53_144_AR.pdf")
                );
    }
    
    public static String unCommentRefDoc(String s){
        s=s.replace("<!--","</htmlstartcomment>");
        s=s.replace("-->","</htmlendcomment>");
        return s;
    }
  
        public static String reCommentRefDoc(String s){
        s=s.replace("</htmlstartcomment>","<!--");
        s=s.replace("</htmlendcomment>","-->");
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
    public static String niceXMLParameters(String msg,String TxtSrc, String RefType,String DocSrc,String DocTgt, String LngSrc,String LngTgt,String Filter,Integer MinLen, Boolean RemFirst,Boolean Fast) {
        
       return "<parameters>\n"
                + "   <msg>"+msg+"</msg>\n"
                + "   <TxtSrc>"+TxtSrc+"</TxtSrc>\n"
               + "   <RefType>"+RefType+"</RefType>\n"
                + "   <DocSrc>"+DocSrc+"</DocSrc>\n"
                + "   <DocTgt>"+DocTgt+"</DocTgt>\n"
                + "   <LngSrc>"+LngSrc+"</LngSrc>\n"
                + "   <LngTgt>"+LngTgt+"</LngTgt>\n"
                + "   <Filter>"+Filter+"</Filter>\n"
                + "   <MinLen>"+MinLen+"</MinLen>\n"
                + "   <Fast>"+Fast+"</Fast>\n"
                + "</parameters>\n";
        
    }   
    
        public static String convertFileWithRMI(byte[] bytes, String fileName) {
        String ret = "Warning: System seems to be unavailable, please contact the Translation Support Section";
        System.out.println("Request to convert file from WebService: " + fileName);
           try {
            Remote r = Naming.lookup("rmi://localhost/CONVSRV");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;
                // ret = is.getInformation();

                int pos = fileName.lastIndexOf('/');

                if (pos >= 0) {
                    fileName = fileName.substring(pos + 1);
                }

                ret = is.File2Txt(bytes, fileName);
                
                        System.out.println("Converted file content: " + ret);
                
            } else {
                return "CONVSRV Service not found or not compatible.";
            }

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
           System.out.println(ex);
        }

        return ret;
    }  
            
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.rest;

/**
 *
 * @author simple
 */


public class WSRESTUtil {
    
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

    
}

/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myCAT.

   myCAT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myCAT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myCAT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package org.olanto.zahir.align;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static org.olanto.util.Messages.*;



/**
 * Une classe pour écrire un tmx.
   * 
*
 */

public class WriteTMX {
     OutputStreamWriter isr;
     BufferedWriter  out ;
     String srcLanguage,targetLanguage;
    
    /**
     *
     * @param fname
     * @param _srcLanguage
     * @param _targetLanguage
     */
    public WriteTMX (String fname, String _srcLanguage, String _targetLanguage) {
        srcLanguage=_srcLanguage;
        targetLanguage=_targetLanguage;
        try{
            isr= new OutputStreamWriter(new FileOutputStream(fname+".tmx"), "UTF-8");
            out = new BufferedWriter(isr);
            out.write("<?xml version=\"1.0\" ?>"+"\n"+
                    "<!DOCTYPE tmx SYSTEM \"tmx14.dtd\">"+"\n"+
                    "<tmx version=\"1.4\">"+"\n"+
                    "<header creationtoolversion=\"1.0.0\" datatype=\"plaintext\" segtype=\"sentence\" adminlang=\"EN-US\" srclang=\""+srcLanguage+"\" o-tmf=\"txt\" creationtool=\"olanto.org Align\" >"+"\n"+
                    "</header>"+"\n"+
                    "<body>"+"\n"
                    );
        } catch (Exception e) { error("IO error savetmx",e);}
    }
    
    /**
     *
     * @param src
     * @param target
     */
    public  void add2Sentence(String src, String target){
        try{
        out.write("<tu>\n");
        out.write("  <tuv xml:lang=\""+srcLanguage+"\"><seg>"+cleanXML(src)+"</seg></tuv>\n");
        out.write("  <tuv xml:lang=\""+targetLanguage+"\"><seg>"+cleanXML(target)+"</seg></tuv>\n"); // normal
        //out.write("  <tuv xml:lang=\""+targetLanguage+"\"><seg>"+cleanXML(clean2gram(target))+"</seg></tuv>\n"); // 2gram
        out.write("</tu>\n");
        } catch (Exception e) { error("IO error add tmx",e);} 
    }

    /**
     *
     * @param s
     * @return
     */
    public static String cleanSpaceAndTab(String s) {
//        System.out.println("-------cst:"+s);
//        for (int i=0; i<s.length();i++){
//            int v=s.charAt(i);
//             System.out.println(i+":"+s.substring(i, i+1)+":"+v);
//        }
        s.replace("\t", " ");
        char x20 = 0x20;
        char xa0 = 0xa0;
//        System.out.println("nbsp")
//            System.out.println("nbsp:" + s);
        s.replace("" + xa0 + x20, " ");
//        System.out.println("1e");
        char x1e = 0x1e;
        s.replace("" + x1e, " ");
//        System.out.println("1f");
        char x1f = 0x1f;
        s.replace("" + x1f, "");
//         System.out.println("02");
        char x02 = 0x02;
        s.replace("" + x02, " ");
//          System.out.println("13");
        char x13 = 0x13;
        s.replace("" + x13, " ");
//          System.out.println("15");
        char x15 = 0x15;
        s.replace("" + x15, " ");
//          System.out.println("00");
        char x00 = 0x00;
        s.replace("" + x00, " ");
//          System.out.println("0b");
        char x0b = 0x0b;
        s.replace("" + x0b, " ");
//          System.out.println("0c");
        char x0c = 0x0c;
        s.replace("" + x0c, " ");
//          System.out.println("double blanc");
        s = s.replace("  ", " ");
//            System.out.println("return");
        return s.trim();
    }

  
    
    static String clean2gram(String s){
        String res="";
        for (int i=0;i<s.length();i+=3)res=res+s.substring(i+1,i+2);
        return res;
    }
    
    static String cleanXML(String s) {
        int ix = 0;
        //msg("s:"+s);
            while ((ix = s.indexOf("&", ix)) > -1) {
                s = s.substring(0, ix) + "&amp;" + s.substring(ix + 1, s.length());
                //msg("rep &:"+s);
                ix += 4;
            }
            ix = 0;
            while ((ix = s.indexOf("<", ix)) > -1) {
                s = s.substring(0, ix) + "&lt;" + s.substring(ix + 1, s.length());
                //msg("rep <:"+s);
                ix += 3;
            }
        return cleanSpaceAndTab(s);       
    }
     
    /**
     *
     */
    public  void tmxClose() {
        
        try{
            out.write("</body>"+"\n"+
                    "</tmx>\n");
            out.flush();
            out.close();
        } catch (Exception e) {error("IO error closetmx",e);}
    }
    
    
}




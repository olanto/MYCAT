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

package org.olanto.zahir.create.bitext;


import org.olanto.zahir.align.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import static org.olanto.util.Messages.*;



/**
 * Une classe pour écrire un tmx.
   * 
*
 */

public class WriteBITEXT {
     OutputStreamWriter isr;
     BufferedWriter  out ;
     String srcLanguage,targetLanguage;
     int idcount=0;
    
    /**
     *
     * @param fname
     * @param _srcLanguage
     * @param _targetLanguage
     */
    public WriteBITEXT (String fname, String _srcLanguage, String _targetLanguage) {
        srcLanguage=_srcLanguage;
        targetLanguage=_targetLanguage;
         idcount=0;
        try{
            isr= new OutputStreamWriter(new FileOutputStream(fname+".xml"), "UTF-8");
            out = new BufferedWriter(isr);
            out.write("<?xml version=\"1.0\"  encoding=\"utf-8\"?>"+"\n"+
                    "<?xml-stylesheet href=\"bitext.css\" type=\"text/css\"?>"+"\n"+
                    "<bitext version=\"1.2\">"+"\n"+
                     "<segments>"+"\n"
                    );
        } catch (Exception e) { error("IO error savexml",e);}
    }
    
    /**
     *
     * @param src
     * @param target
     * @param nfrom
     * @param nto
     */
    public  void add2Sentence(String src, String target,int nfrom, int nto){
         idcount++;
        try{
        out.write("<seg  match=\""+nfrom+"-"+nto+"\" id=\""+idcount+"\">\n");
        out.write("  <src xml:lang=\""+srcLanguage+"\">"+cleanXML(src)+"</src>\n");
        out.write("  <tgt xml:lang=\""+targetLanguage+"\">"+cleanXML(target)+"</tgt>\n"); // normal
        //out.write("  <tuv xml:lang=\""+targetLanguage+"\"><seg>"+cleanXML(clean2gram(target))+"</seg></tuv>\n"); // 2gram
        out.write("</seg>\n");
        } catch (Exception e) { error("IO error add tmx",e);} 
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
            s=s.replace("$$$$$$", "<br/>");
        return s;       
    }
     
    /**
     *
     */
    public  void tmxClose() {
        
        try{
            out.write("</segments>"+"\n"+
                    "</bitext>\n");
            out.flush();
            out.close();
        } catch (Exception e) {error("IO error closetmx",e);}
    }
    
    
}




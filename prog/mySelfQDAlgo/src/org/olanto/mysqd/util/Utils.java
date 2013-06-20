/**
 * ********
 * Copyright © 2010-2012 Jacques Guyot
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.mysqd.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static org.olanto.mysqd.util.HTML4Type.*;
import static org.olanto.util.Messages.*;

/**
 *
 * @author simple
 */
public class Utils {

    /**
     * lit le contenu d'un fichier texte encodé
     *
     * @param fname nom du fichier
     * @param txt_encoding encodage
     * @return le contenu du fichier
     */
    public static final String file2String(String fname, String txt_encoding) {
        StringBuffer txt = new StringBuffer("");
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(fname), txt_encoding);
            BufferedReader in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                txt.append(w);
                txt.append("\n");
                w = in.readLine();
            }
            return txt.toString();
        } catch (Exception e) {
            error("file2String", e);
            return null;
        }

    }

    public static final boolean[] removeEntities(String s) {
        boolean[] ischar=new boolean[s.length()]; 
        for (int i=0;i<s.length();i++){
            ischar[i]=true;
        }
        for (int i=0;i<html4Size();i++){
            HTMLEntity ent=html4get(i);
      //       System.out.println("symbol: "+ ent.entity+ " = "+(char)ent.val);
      //        System.out.println("  {new String(\""+ent.entity+"\"), new Integer("+ent.val+"),new Boolean(false)},    //symbol: "+ ent.entity+ " = "+(char)ent.val);
           if (!(Character.isLetter((char) ent.val))) markchar(ischar, s, ent.entity);
        }
        
//        for (int i=0;i<s.length();i++){
//            System.out.println(s.charAt(i)+ " "+ ischar[i]);
//        } 
        return ischar;
    }
    
    private static final boolean[] markchar( boolean[] b,String s, String toberemove){
        int curpos=s.indexOf(toberemove, 0);
        int remlen=toberemove.length();
        int count=0;
        while (curpos!=-1){
            count++;
            for (int i=curpos;i<curpos+remlen;i++){
                b[i]=false;         
            }
            curpos=s.indexOf(toberemove, curpos+1);
        }
       // System.out.println("mark: "+ toberemove+" #"+count);
        return b;
    }
    
    
    
}

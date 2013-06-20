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
package org.olanto.util;

import java.util.HashMap;

/**
 * pour permettre l'intégration des glossaires dans myCAT
 * @author JG
 */
public class ExtensionUtils {

    static HashMap<String, String> extension = new HashMap<String, String>();
    static boolean verbose = true;

    public ExtensionUtils(String[] SetOfLang){
            for (int i = 0; i < SetOfLang.length; i++) { // génére les _XX
            extension.put("_" + SetOfLang[i], "ok");
            if (verbose) {
                System.out.println("add this extension:" + "_" + SetOfLang[i]);
            }
        }
   
    }
    
 
    /** retourne l'extension linguistique */
    public  String getNExt(String name) {
        String res = "";
        int lastposOflang = name.lastIndexOf("_");
        if (lastposOflang == -1) { // pas de  _XX
            if (verbose) {
                System.out.println("no extension _XX:" + name);
            }
            return res;
        }
//        System.out.println("last:" + name.substring(lastposOflang, lastposOflang + 3));
//        System.out.println("dot:" + name.substring(lastposOflang+ 3, lastposOflang + 4));
        if (!name.substring(lastposOflang + 3, lastposOflang + 4).equals(".")) { // pas de _XX.
            if (verbose) {
                System.out.println("no dot _XX.:" + name);
            }
            return res;
        }
        res = name.substring(lastposOflang, lastposOflang + 3);
        String ok = extension.get(res);
        if (ok == null) {
            if (verbose) {
                System.out.println("not in corpus languages:" + name);
            }
            return "";
        }
        if (lastposOflang < 3) {  // seulement une seule extensiion
            return res;
        }
        lastposOflang -= 3; // positionne sur l'extension d'avant
        while (extension.get(name.substring(lastposOflang, lastposOflang + 3)) != null) { // oui une nouvelle extension
            res = name.substring(lastposOflang, lastposOflang + 3) + res;
            if (lastposOflang < 3) {  // plus possible
                return res;
            }
            lastposOflang -= 3;
        }
        return res;
    }
}

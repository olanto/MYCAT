/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
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
package org.olanto.mycat.tmx.multiun.extract;

import org.olanto.mycat.tmx.dgt2014.extract.*;
import java.util.HashMap;

/**
 *
 * @author x
 */
public class LangMapMULTIUN {

    public static HashMap<String, Integer> langmap = new HashMap<>();
    public static String[] decodelang;
 
    public static void main(String[] args) {
        init();
        System.out.println("FR=" + getpos("FR"));
        // System.out.println("XX=" + getpos("XX"));
        System.out.println("size=" + size());
        System.out.println("#9=" + getlang(9));
    }

    public static int getpos(String lang) {
        //!!! no check
        return langmap.get(lang);
    }

    public static String getlang(int pos) {
        //!!! no check
        return decodelang[pos];
    }

    public static int deltaSOTA(String langSO, String langTA) {
        int posSO = getpos(langSO);
        int posTA = getpos(langTA);
        int delta = posSO - posTA;
        //System.out.println ("posSO="+posSO+", posTA="+posTA+", delta="+delta);
        return delta;

    }

    public static int size() {
        return langmap.size();
    }

    public static void init() {
        if (decodelang == null) {
            langmap.put("AR", 0);
            langmap.put("DE", 1);
            langmap.put("EN", 2);
            langmap.put("ES", 3);
            langmap.put("FR", 4);
            langmap.put("RU", 5);
            langmap.put("ZH", 6);
              decodelang = new String[langmap.size()];

            for (String key : langmap.keySet()) {
                decodelang[langmap.get(key)] = key;
            }
        }
    }
}

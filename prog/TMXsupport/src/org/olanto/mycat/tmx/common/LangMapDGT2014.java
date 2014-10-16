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
package org.olanto.mycat.tmx.common;

import java.util.HashMap;

/**
 *
 * @author x
 */
public class LangMapDGT2014 {

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
            langmap.put("BG", 0);
            langmap.put("CS", 1);
            langmap.put("DA", 2);
            langmap.put("DE", 3);
            langmap.put("EL", 4);
            langmap.put("EN", 5);
            langmap.put("ES", 6);
            langmap.put("ET", 7);
            langmap.put("FI", 8);
            langmap.put("FR", 9);
            langmap.put("GA", 10);
            langmap.put("HU", 11);
            langmap.put("IT", 12);
            langmap.put("LT", 13);
            langmap.put("LV", 14);
            langmap.put("MT", 15);
            langmap.put("NL", 16);
            langmap.put("PL", 17);
            langmap.put("PT", 18);
            langmap.put("RO", 19);
            langmap.put("SH", 20);
            langmap.put("SK", 21);
            langmap.put("SL", 22);
            langmap.put("SV", 23);
            decodelang = new String[langmap.size()];

            for (String key : langmap.keySet()) {
                decodelang[langmap.get(key)] = key;
            }
        }
    }
}

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
package org.olanto.mysqd.util;

import org.olanto.util.Timer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.olanto.util.Messages.*;

/**
 *  cette classe fournit les méthodes pour retrouver les mots avec des wildchar
 *  * = 0 ou n char
 *  . =1 char
 *
 */
public class WildCharExpander {

    private String target;
    public static final char ITEM_START = '\n';
    public static final char ITEM_STOP = '\r';

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    public WildCharExpander(StringBuilder list) {
        target = list.toString();
    }

    public String getFirstExpand(String regex) {
//       System.out.println("---------- target size :" + target.length());
//           System.out.println("---------- look 0 for :" + regex);
        regex = regex.replaceAll("\\*", ".*");
//        System.out.println("---------- look 1 for :" + regex);

        regex = ITEM_START + regex + ITEM_STOP;
////         System.out.println("---------- look 2 for :" + regex);
        Pattern pattern = Pattern.compile(regex);
        // Get a Matcher based on the target string. 
        Matcher matcher = pattern.matcher(target);
        String match = "";
        if (matcher.find()) {
            match = matcher.group();
        }
        return match;
    }

    public boolean Contains(String regex) {
//       System.out.println("---------- target size :" + target.length());
//           System.out.println("---------- look 0 for :" + regex);
        regex = regex.replaceAll("\\*", ".*");
//        System.out.println("---------- look 1 for :" + regex);

        regex = ITEM_START + regex + ITEM_STOP;
////         System.out.println("---------- look 2 for :" + regex);
        Pattern pattern = Pattern.compile(regex);
        // Get a Matcher based on the target string. 
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
}

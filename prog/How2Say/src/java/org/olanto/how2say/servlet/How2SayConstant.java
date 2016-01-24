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
package org.olanto.how2say.servlet;

import org.olanto.senseos.SenseOS;

/**
 * Une classe pour déclarer des constantes du conteneur de MAP.
 *
 */
public class How2SayConstant {//extends org.olanto.idxvli.IdxConstant {

    /**
     * **********************************************************************************
     */
    /**
     * liste des langues
     */
    public static String LIST_OF_LANG = "XX YY ZZ";
    /**
     * corpus name
     */
    public static String CORPUS_NAME = "Corpus name?";
    /**
     * corpus name
     */
    public static String CACHE = "CACHE";
    /**
     * encodage des paramètres UTF-8, iso-8859-1
     */
    public static String URL_PARAM_ENCODING = "UTF-8";

    /**
     * **********************************************************************************
     */
    public static void show() {
        System.out.println(
                "HOWTOSAY"
                + "\n    CORPUS_NAME: " + CORPUS_NAME
                + "\n    LIST_OF_LANG: " + LIST_OF_LANG
                + "\n    CACHE: " + CACHE
                + "\n    URL_PARAM_ENCODING: " + URL_PARAM_ENCODING   
);
    }
}

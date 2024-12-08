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

package org.olanto.mycat.lineseg;

/**
 * Une classe pour déclarer les types énumérés.
 * 
 */
public class SegmentationEnum {


    public static enum FileMode {

        /**oui*/
        BY_EXT,
        /**non*/
        BY_FOLDER
    };

    /** conserve des propriétés de collection */
    public static enum CollectionMode {

        /**oui*/
        YES,
        /**non*/
        NO
    };

    /** la compression est activée */
    public static enum Compression {

        /**oui*/
        YES,
        /**non*/
        NO
    };

    /** type de mapping pour les fichiers  */
    public static enum MappingMode {

        /** pas de mapping */
        NOMAP,
        /** mapping total  */
        FULL,
        /** mapping partiel  */
        CACHE
    };

    /** état d'un verrouillage  */
    public static enum StateMark {

        /** verrouiller */
        LOCK,
        /** déverrouiller */
        UNLOCK
    };

    /** type d'utilisation de l'index d'un mot  */
    public static enum UsageMark {

        /** de base (sans les positions) */
        BASIC,
        /** complet (avec les positions) */
        FULL,
        /** pas utilisé*/
        UNUSED
    };

    /** type de stockage de l'index */
    public static enum PassMode {

        /** en une passe, on indexe et on stocke simultanément */
        ONE,
        /** en une passe, on indexe et on stocke successivement */
        TWO
    };
}

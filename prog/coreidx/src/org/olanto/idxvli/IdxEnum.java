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

package org.olanto.idxvli;

/**
 * Une classe pour déclarer les types énumérés.
 * 
 */
public class IdxEnum {

    /** Mode d'indexation. */
    public static enum RankingMode {

        /**pas de ranking */
        NO,
        /**classique inverse document frequency * term document frequency */
        IDFxTDF,
        /**classique BM25 adapted */
        BM25,
        /**classique BM25 adapted */
        BM25TWICE
    };

    /** Mode d'indexation. */
    public static enum IdxMode {

        /**nouvelle indexation */
        NEW,
        /**On ajoute des documents (ou éventuellement on remplace des documents*/
        INCREMENTAL, /**On ajoute des documents, remplace et élimine les documents absents */
        DIFFERENTIAL,
        /**interrogation de l'index, pas de mise à jour */
        QUERY
    };

    /** Mode Mix. */
    public static enum ContinueMode {

        /** en lecture et en écriture simultané */
        MIX,
        /** en lecture ou en écriture */
        ALT
    };

    /** Mode d'ouverture des objets */
    public static enum readWriteMode {

        /**lecture seulement */
        r,
        /**lecture/écriture */
        rw
    };

    /** stratégie d'implémentation */
    public static enum implementationMode {

        /**privilégie la rapidité et les noms des fichiers sont des numéros (pour le content manager) */
        DIRECT,
        /**privilégie les volumes de grande taille */
        FAST, /**privilégie les volumes de grande taille */
        BIG,
        /**privilégie les volumes de grande taille avec un accés disque au string */
        XL,
        /**privilégie les volumes de trés grande taille */
        XXL
    };

    /** conserve des propriétés de langage */
    public static enum LanguageMode {

        /**oui*/
        YES,
        /**non*/
        NO
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
    /** rename option  */
    public static enum RenameOption {

        /** convert to lowercase*/
        LOWER,
        /** convert to uppercase*/
        UPPER,
        /** no change*/
        NOCHANGE
    };

    /** type de stockage de l'index */
    public static enum PassMode {

        /** en une passe, on indexe et on stocke simultanément */
        ONE,
        /** en une passe, on indexe et on stocke successivement */
        TWO
    };
}

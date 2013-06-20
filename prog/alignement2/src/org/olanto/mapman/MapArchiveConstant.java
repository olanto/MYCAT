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
package org.olanto.mapman;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;
import org.olanto.idxvli.IdxEnum.Compression;
import org.olanto.idxvli.IdxEnum.IdxMode;
import org.olanto.idxvli.IdxEnum.implementationMode;
import org.olanto.mapman.mapper.TokenNative;
import static org.olanto.util.Messages.*;

/**
 * Une classe pour déclarer des constantes du conteneur de MAP.
 *
 */
public class MapArchiveConstant {//extends org.olanto.idxvli.IdxConstant {

    /*
     * LOGGER DEFINITION
     */
    /**
     * logger commun
     */
    public static Logger COMLOG;
    /**
     * nom du fichier pour les logs
     */
    public static String COMLOG_FILE = "C:/JG/VLI_RW/data/common.log";
    /**
     * log est en mode append
     */
    public static boolean COMLOG_APPEND = true;
    /**
     * log format XML
     */
    public static boolean COMLOG_FORMAT_XML = false;
    /**
     * fichier associ� au logger
     */
    private static FileHandler handler_COMLOG;
    /**
     * logger detail
     */
    public static Logger DETLOG;
    /**
     * nom du fichier pour les logs
     */
    public static String DETLOG_FILE = "C:/JG/VLI_RW/data/detail.log";
    /**
     * log est en mode append
     */
    public static boolean DETLOG_APPEND = false;
    /**
     * log format XML
     */
    public static boolean DETLOG_FORMAT_XML = false;
    /**
     * fichier associ� au logger
     */
    private static FileHandler handler_DETLOG;
    /**
     * valeur rapport�e si on ne trouve pas
     */
    /**
     * **********************************************************************************
     */
    public static final int NOT_FOUND = -1;
    /**
     * un m�ga
     */
    public static final long MEGA = 1024 * 1024;
    /**
     * un kilo
     */
    public static final int KILO = 1024;
    /**
     * pour avoir plus de renseignements sur IdxIO
     */
    public static boolean VERBOSE_IO = false;
    /**
     * **********************************************************************************
     */
    /**
     * path de la racine des fichiers communs
     */
    public static String COMMON_ROOT = "C:/JG/VLI_RW/data/test";
    /**
     * racine des nom des fichiers
     */
    public static String currentf = "rootidx";
    /*
     * MAP ************************************************************************************
     */
    /**
     * nbr max de pairs de langues en puissance de deux
     */
    public static int LANGPAIR_MAXBIT = 5;
    /**
     * nbr max de pairs de langues
     */
    public static int LANGPAIR_MAX = (int) Math.pow(2, LANGPAIR_MAXBIT);  // recalcule
    /**
     * path de la racine des fichiers du document manager
     */
    public static String MAP_ROOT = COMMON_ROOT;
    /**
     * extention des noms du document manager
     */
    public static String MAP_NAME = "MAP";
    public static String[] LANGID = new String[LANGPAIR_MAX];
    /**
     * nbr maximum de documents indexables
     */
    public static int DOC_MAX = 1024;
    /**
     * nbr de bit de maxdoc
     */
    public static int DOC_MAXBIT = 10;
    /**
     * langues des maps
     */
    public static String LIST_OF_MAP_LANG = "XX YY";
     /**
     * Skip a line between paragraphs
     */
    public static boolean SKIP_LINE = false;
  /**
     * get txt from zip_cache (IDX_ZIP_CACHE must be set to true
     */
    public static boolean GET_TXT_FROM_ZIP_CACHE = false;
   /**
     * compression des object storage
     */
    public static Compression MAP_COMPRESSION = Compression.YES;
    /**
     * **********************************************************************************
     */
    /**
     * implementation des objects store. l'impl�mentation est directement li�e �
     * la taille du dictionnaire
     */
    /**
     * implementation du manager d'objets FAST ou BIG
     */
    public static implementationMode OBJ_IMPLEMENTATION = implementationMode.FAST;
    /**
     * taille des objets consid�r�s comme petits en byte. Doit �tre un mutiple
     * de 4, pour g�rer des int
     */
    public static int OBJ_SMALL_SIZE = 16 * 4;
    /**
     * nbre d'object storage actif = 2^OBJ_PW2
     */
    public static int OBJ_PW2 = 2;  ///0=>1,1=>2,2=>4,3=>8,4=>16
    /**
     * nbre d'object storage actif
     */
    public static int OBJ_NB = (int) Math.pow(2, OBJ_PW2);  ///0=>1,1=>2,2=>4,
    /**
     * maximum des msg dans la queue
     */
    public static int OBJ_STORE_MAX_QUEUE = 8 * KILO;
    /**
     * maximum des objects storeage
     */
    public static int OBJ_STORE_MAX_THREAD = 16;
    /**
     * défini si le mode est asynchrone
     */
    public static boolean OBJ_STORE_ASYNC = false;
    /**
     * root des fichiers des object storage
     */
    public static String[] OBJ_ROOT = new String[OBJ_STORE_MAX_THREAD];
    /**
     * **********************************************************************************
     */
    /**
     * mode d'utilisation du serveur de map
     */
    public static IdxMode MODE_IDX;
    /**
     * **********************************************************************************
     */
    /**
     * nom du fichier pour les mots non-indexables
     */
    public static String IDX_DONTINDEXTHIS = "C:/JG/VLI_RW/data/dontindexthiswords.txt";
    /**
     * nom du fichier pour les mots non-indexables
     */
    public static ParseSetOfWords SOF = null;
    /**
     * longueur minimum d'un mot � indexer
     */
    public static int WORD_MINLENGTH = 3;
    /**
     * longueur maximum d'un mot � indexer (tronquer)
     */
    public static int WORD_MAXLENGTH = 40;
    /**
     * définition de la notion de terme
     */
    public static ParseTokenDefinition WORD_DEFINITION = new TokenNative();

    /**
     * **********************************************************************************
     */
    public static void show() {
        COMLOG.info(
                "MAP"
                + "\nLANGUAGES"
                + "\n    LANGPAIR_MAXBIT: " + LANGPAIR_MAXBIT
                + "\n    LANGPAIR_MAX: " + LANGPAIR_MAX
                + "\n    LANGUAGE IN MAP: " + LIST_OF_MAP_LANG
                + "\nPARSING"
                + "\n    IDX_DONTINDEXTHIS: " + IDX_DONTINDEXTHIS
                + "\n    WORD_MINLENGTH: " + WORD_MINLENGTH
                + "\n    WORD_MAXLENGTH: " + WORD_MAXLENGTH
                + "\n    WORD_DEFINITION: " + WORD_DEFINITION.toString()
                + "\nSTRATEGIES"
                + "\n    MAP_MAX: " + DOC_MAX
                + "\n    OBJSTO_IMPLEMENTATION: " + OBJ_IMPLEMENTATION
                + "\n    OBJ_PW2: " + OBJ_PW2
                + "\n    OBJ_NB: " + OBJ_NB
                + "\n    OBJ_SMALL_SIZE: " + OBJ_SMALL_SIZE
                + "\n    OBJ_STORE_ASYNC: " + OBJ_STORE_ASYNC
                + "\n    OBJ_STORE_MAX_QUEUE: " + OBJ_STORE_MAX_QUEUE
                + "\n    OBJ_COMPRESSION: " + MAP_COMPRESSION);
    }

    /**
     * ouvre le fichier de log
     */
    public static void openLogger() {
        // initialisation du logger
        try {
            // logfile est de type append ou replace
            handler_COMLOG = new FileHandler(COMLOG_FILE, COMLOG_APPEND);
            if (COMLOG_FORMAT_XML) {
                handler_COMLOG.setFormatter(new XMLFormatter());
            } else {
                handler_COMLOG.setFormatter(new SimpleFormatter());
            }

            // Add to the desired logger
            COMLOG = Logger.getLogger("common log for VLI");
            COMLOG.addHandler(handler_COMLOG);
        } catch (Exception e) {
            error("append during open log file", e);
        }
        COMLOG.info("OPEN Log File ---------------------------------------------------------");
        try {
            handler_DETLOG = new FileHandler(DETLOG_FILE, DETLOG_APPEND);
            if (DETLOG_FORMAT_XML) {
                handler_DETLOG.setFormatter(new XMLFormatter());
            } else {
                handler_DETLOG.setFormatter(new SimpleFormatter());
            }
            DETLOG = Logger.getLogger("detail log for VLI");
            DETLOG.addHandler(handler_DETLOG);
        } catch (Exception e) {
            error("append during open log file", e);
        }
        DETLOG.info("OPEN Log File ---------------------------------------------------------");

    }

    public static void closeLogger() {
        COMLOG.info("CLOSE Common Log File ---------------------------------------------------------");
        DETLOG.info("CLOSE Detail Log File ---------------------------------------------------------");
    }

    /**
     * modification des racines des directoires des objects storage
     *
     * @param root nom du directoire
     * @param ObjStoId indice de l'object storage
     */
    public static void SetObjectStoreRoot(String root, int ObjStoId) {
        if (ObjStoId >= 0 && ObjStoId < OBJ_STORE_MAX_THREAD) {
            OBJ_ROOT[ObjStoId] = root;
        } else {
            error("error in SetObjectStoreRoot out of bound");
        }
    }
}

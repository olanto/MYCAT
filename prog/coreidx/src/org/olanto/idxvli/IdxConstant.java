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
package org.olanto.idxvli;

import static org.olanto.util.Messages.*;
import static org.olanto.idxvli.IdxEnum.*;
import java.util.logging.*;
import org.olanto.idxvli.server.OriginalService;
import org.olanto.idxvli.server.OriginalService_Default;
import org.olanto.idxvli.server.PreProcessingService;
import org.olanto.idxvli.server.PreProcessingService_Default;

/**
 * Une classe pour déclarer des constants.
 *
 *
 */
public class IdxConstant {

    public static final String VERSION = "3.2.3";
    
    public static String REGEX_EXACT_BEFORE_TOKEN = "([^\\p{L}\\p{N}]|^)";
    
    public static String REGEX_EXACT_AFTER_TOKEN = "([^\\p{L}\\p{N}]|$)";
    
    public static boolean FULL_LOAD = true; // // évite de charger les doc et les idx for classification
    
    public static boolean CHECK_CONSISTENT = false;
    // constante pour être compatible avec la version du classifieur
    /**
     * nom du fichier pour les documents non-indexables
     */
    public static String IDX_DONTINDEXTHISDOC = "C:/JG/gigaversion/data/dontindexthisdocuments.txt";
    // -------------------------------------------------------------------   
    // constante pour être compatible avec la version du classifieur
    /**
     * nom du fichier pour les documents non-indexables
     */
    public static String IDX_XML_ORGANISATION_TEMPLATE = "C:/MYCAT/config/organisation_template.xml";
    // -------------------------------------------------------------------   

    /* LOGGER DEFINITION */
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
    /* correction ortografic ! */
    /**
     * initialise une suggestion orthographique pour les requetes
     */
    public static boolean ORTOGRAFIC = true;
    /**
     * dictionnaire de correction
     */
    public static String DICT_FILE = "C:/JG/VLI_RW/dict/en.dic";
    /**
     * dictionnaire de correction
     */
    public static String ORG_FILE = "C:/JG/VLI_RW/dict/organisation.dic";
    /**
     * r�gles phon�tiques
     */
    public static String PHONET_FILE = "C:/JG/VLI_RW/dict/phonet.en";
    /**
     * longueur mininaldu mot pour une sugestion
     */
    public static int MIN_CHAR_SUGGEST = 4;
    /**
     * nbr maximum de sugestions pour un mot
     */
    public static int MAX_SUGGESTION = 6;
    /**
     * facteur de d�croissance entre deux suggestions
     */
    public static int DECREASE_NEXT_SUGGESTION = 2;
    /**
     * en cas de z�ro r�ponse essaye de sugg�r� une alternative (m�me si peu de
     * r�ponse)
     */
    public static boolean SUGGEST_MANDATORY_IF_ZERO = true;
    /**
     * la suggestion doit amener un facteur mininum de r�ponses suppl�mentaire
     */
    public static int MIN_FACTOR_SUGGESTION = 5;
    /* MODE DEFINITION */
    /**
     * mode d'utilisation de l'index
     */
    public static IdxMode MODE_IDX;
    /**
     * mode de continuit�
     */
    public static ContinueMode MODE_CONTINUE = ContinueMode.MIX;
    /**
     * mode de ranking des requ�te
     */
    public static RankingMode MODE_RANKING = RankingMode.IDFxTDF;
    /**
     * valeur rapport�e si on ne trouve pas
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
     * maximum de distance entre deux mots pour l'op�rateur NEAR, si elle n'est
     * pas sp�cifi�e, elle est calcul�e en mots index�s
     */
    public static int NEAR_DISTANCE = 15;
    public static int MAX_CITATION = 100;
    public static int MAX_RESPONSE = 1000;
    public static int MAX_QUERY_IN_CACHE = 1000;  //taille en byte approx MAX_QUERY_IN_CACHE*WINDOW_SIZE*2*1000 ?
   public static boolean CACHE_QUERY = true;  // active cacheQuery
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
    /**
     * nom du fichier pour les mots non-indexables
     */
    public static String IDX_DONTINDEXTHIS = "C:/JG/VLI_RW/data/dontindexthiswords.txt";
    /**
     * nom du fichier racine pour la d�tection des langues
     */
    public static String LANGUAGE_TRAINING = "C:/JG/VLI_RW/TrainingDTK/";
    /**
     * nom du fichier racine pour la d�tection des langues
     */
    public static String COLLECTION_DOMAIN = "C:/JG/VLI_RW/data/urlcollection.txt";
    /**
     * Name of the temporary folder
     */
    public static String TEMP_FOLDER = "C:/MYCAT/TEMP";
    /**
     * racine pour le corpus � convertir
     */
    public static String ROOT_CORPUS_SOURCE = "";
    /**
     * racine pour le corpus txt celui qui est indexe
     */
    public static String ROOT_CORPUS_TXT = "";
    /**
     * langues du corpus
     */
    public static String ROOT_CORPUS_LANG = "XX YY";
    /*INDEX OPTION *************************************************************************************/
    /**
     * modification du nom du fichier
     */
    public static String SEPARATOR = "¦";
   /**
     * modification du nom du fichier
     */
    public static boolean FILE_RENAME = false;
    /**
     * modification du nom des collections
     */
    public static RenameOption FILE_COLLECTION_CASE = RenameOption.NOCHANGE;
   /**
     * modification du nom du fichier
     */
    public static RenameOption FILE_NAME_CASE = RenameOption.NOCHANGE;
     /**
     * modification du nom du fichier
     */
    public static RenameOption FILE_EXTENTION_CASE = RenameOption.NOCHANGE;
        public static String GLOSS_NAME = "Glossaries";
    /**
     * adding a parameter to modify the order_by_name
     */
        public static boolean GLOSS_FIRST_IN_LIST = true;
    /**
     * sauvegarde des positions
     */
    public static boolean IDX_SAVE_POSITION = true;
    /**
     * cr�e des doc bag => classification & knn
     */
    public static boolean IDX_WITHDOCBAG = false;
    /**
     * encodage du fichier mflf
     */
    public static String IDX_MFLF_ENCODING = "UTF-8";
    /**
     * cr�e des info suppl�mentaire sur les documents
     */
    public static boolean IDX_MORE_INFO = false;
    /**
     * crée un cache ZIP pour les documents
     */
    public static boolean IDX_ZIP_CACHE = false;
    /**
     * pour accélerer le load et save
     */
    public static boolean IDX_ZIP_CACHE_FASTLOAD = false;
    /**
     * si vrai alors une indexation conceputelle sinon native dans la langue
     */
    public static boolean IDX_CONCEPTUAL = true;
    /**
     * marqueur de document actif
     */
    public static boolean IDX_MARKER = true;
    /* pour le classifieur *************************************************************************************/
    /**
     * faire les doc bag (passe 2)
     */
    public static boolean DO_DOCBAG = true;
    /**
     * enregistrer les documents
     */
    public static boolean DO_DOCRECORD = true;
    /**
     * ne cr�e pas d'index dans l'objectstore count les mots seulement marche
     * avec les options en deux passes et la compression !!!
     */
    public static boolean NO_IDX_ONLY_COUNT = false;
    /**
     * ne cr�e pas d'index dans l'objectstore count les mots seulement marche
     * avec les options en une passe et la compression !!!
     */
    public static boolean MODIFY_IDX = true;
    /**
     * Twopass indexation pour fixer le vocabulaire (acc�l�re les classifieurs)
     */
    public static boolean WORD_TWOPASS = false;
    /**
     * nbr mininum de documents poss�dant ce terme (pour le garder)
     */
    public static int WORD_MINOCCKEEP = 24;  // pour une indexation en deux passes
    /**
     * nbr maximum de documents poss�dant ce terme (en pour mille)
     */
    public static int WORD_MAXOCCDOCKEEP = 50;  // pour une indexation en deux passes
    /**
     * liste pour fixer le vocabulaire (acc�l�re les classifieurs) annule minocc
     * et maxocc
     */
    public static boolean WORD_LIST = false;
    /* DOCUMENT *************************************************************************************/
    /**
     * encodage des fichiers
     */
    public static String DOC_ENCODING = "ISO-8859-1";  //"UTF-8";
    /**
     * implementation du document manager FAST ou BIG
     */
    public static implementationMode DOC_IMPLEMENTATION = implementationMode.FAST;
    /**
     * conserve les propi�tes de langage du document
     */
    public static LanguageMode DOC_LANGUAGE = LanguageMode.NO;   // pour le content manager =No
    /**
     * conserve les propi�tes de collection du document
     */
    public static CollectionMode DOC_COLLECTION = CollectionMode.NO; // pour le content manager =No
    /**
     * path de la racine des fichiers du document manager
     */
    public static String DOC_ROOT = COMMON_ROOT;
    /**
     * extention des noms du document manager
     */
    public static String DOC_NAME = "DOC";
    /**
     * extention des noms du document manager
     */
    public static String ZIP_NAME = "ZIP";
    /**
     * taille maximum des noms de documents
     */
    public static int DOC_SIZE_NAME = 128;
    /**
     * taille maximum d'un document
     */
    public static final int DOC_MAXOCCLENGTH = 5000000;
    /**
     * nbr maximum de documents indexables
     */
    public static int DOC_MAX = 1024;
    /**
     * nbr de bit de maxdoc
     */
    public static int DOC_MAXBIT = 10;
    /**
     * nombre de propri�t�s maximum
     */
    public static int DOC_PROPERTIES_MAXBIT = 12;  // 2^8
    /**
     * longueur maximum d'une propri�t�
     */
    public static int DOC_PROPERTIES_MAX_LENGHT = 256;
    /**
     * facteur de compression esp�r�, utilis� dans les impl�mentations BIG, !
     * allocation fixe
     */
    public static  int HOPE_COMPRESSION = 64;
    /**
     * maximum en cache, utilis� dans les impl�mentations BIG, ! allocation fixe
     */
    public static final int MAX_IN_ZIP_CACHE = 64;
    /**
     * **********************************************************************************
     */
    /**
     * implementation du word manager FAST ou BIG
     */
    public static implementationMode WORD_IMPLEMENTATION = implementationMode.FAST;
    /**
     * path de la racine des fichiers du document manager
     */
    public static String WORD_ROOT = COMMON_ROOT;
    /**
     * extention des noms du document manager
     */
    public static String WORD_NAME = "WORD";
    /**
     * taille du cache pour les mots consult�s (actuellement la m�me valeur est
     * utilis� pour les documents!)
     */
    public static int WORD_CACHE_COUNT = 1 * (int) MEGA;
    /**
     * nbr maximum de mots indexables
     */
    public static int WORD_MAX = 1024;
    /**
     * nbr de bit de maxword
     */
    public static int WORD_MAXBIT = 10;
    /**
     * longueur minimum d'un mot � indexer
     */
    public static int WORD_MINLENGTH = 3;
    /**
     * longueur maximum d'un mot � indexer (tronquer)
     */
    public static int WORD_MAXLENGTH = 20;
    /**
     * nbr maximum de mots � indexer par document
     */
    public static int WORD_NFIRSTOFDOC = 10000000;
    /**
     * activate stemmer
     */
    public static boolean WORD_USE_STEMMER = false;
    /**
     * activate stemmer at document level
     */
    public static boolean STEM_DOC = true;
    /**
     * stemming language
     */
    public static String WORD_STEMMING_LANG = "nostemming";
    /**
     * cache of stemming language
     */
    public static int STEM_CACHE_COUNT = 256 * KILO;
    /**
     * cache of stemming language
     */
    public static boolean STEM_KEEP_LIST = false;
    /**
     * stemming language for mfl file
     */
    public static String ACTUAL_LANGUAGE = "";
    /**
     * définition de la notion de terme
     */
    public static TokenDefinition WORD_DEFINITION = new TokenDefault();
    /**
     * **********************************************************************************
     */
    /**
     * autorise l'expansion des termes avec les wildchar pour les mots
     */
    public static boolean WORD_EXPANSION = true;
    /**
     * recharge la liste si on modifie l'index (sinon la liste est établie au
     * chargement seulement
     */
    public static boolean WORD_EXPANSION_RELOAD = true;
    /**
     * recharge la liste si on modifie l'index (sinon la liste est établie au
     * chargement seulement
     */
    public static int WORD_MAX_EXPANSION = 100;
    /**
     * autorise l'expansion des termes avec les wildchar pour les noms de
     * documents
     */
    public static boolean DOCNAME_EXPANSION = true;
    /**
     * recharge la liste si on modifie l'index (sinon la liste est établie au
     * chargement seulement
     */
    public static boolean DOCNAME_EXPANSION_RELOAD = true;
    /**
     * recharge la liste si on modifie l'index (sinon la liste est établie au
     * chargement seulement
     */
    public static int DOCNAME_MAX_EXPANSION = 1000;
    /**
     * maximum de nom collecter avant tri
     */
    public static int DOCNAME_MAX_BROWSE = 5000;
    /**
     * longueur max des check pour les reférences trouvées
     */
    public static int REALREF_MAX_CHECK = 12;
     /**
     * longueur max des check pour les reférences trouvées
     */
    public static int REF_MAX_THREAD = 4;
   /**
     * search expression are insensitive by default
     */
    public static boolean DOCNAME_BROWSE_INSENSITIVE = true;
    /**
     * only to test new feature not for production
     */
    public static boolean PRODUCTION_MODE = true;
    /**
     * **********************************************************************************
     */
    /**
     * ajoute les statisques dans myQuote
     */
    public static boolean SKIP_LINE_QUOTE_DECTECTOR = false;
    public static boolean MYQUOTE_STAT = true;
    public static String OPEN_REF_BEG = "[R";
    public static String OPEN_REF_END = "]";
    public static String CLOSE_REF_BEG = "[E";
    public static String CLOSE_REF_END = "]";
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
    public static implementationMode OBJ_IMPLEMENTATION = WORD_IMPLEMENTATION;
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
     * d�fini si le mode est asynchrone
     */
    public static boolean OBJ_STORE_ASYNC = true;
    /**
     * root des fichiers des object storage
     */
    public static String[] OBJ_ROOT = new String[OBJ_STORE_MAX_THREAD];
    /**
     * compression des object storage
     */
    public static Compression OBJ_COMPRESSION = Compression.YES;
    /**
     * **********************************************************************************
     */
    /**
     * implementation des caches d'indexation
     */
    public static implementationMode CACHE_IMPLEMENTATION_INDEXING = implementationMode.BIG;
    /**
     * implementation des caches d'interrogation
     */
    public static implementationMode CACHE_IMPLEMENTATION_READ = implementationMode.BIG;
    /**
     * **********************************************************************************
     */
    // cache d'indexation
    /**
     * taille du cache pour les indexation (donc mode=INCREMENTAL, DIFFERENTIAL)
     */
    public static long INDEXING_CACHE_SIZE = 256 * MEGA;
    /**
     * part en % a essayer de conserver dans le cache lors d'un nettoyage [0 �
     * 99].
     */
    public static int KEEP_IN_CACHE = 0;
    /**
     * taille du cache pour les indexation
     */
    public static int IDX_CACHE_COUNT = 128 * KILO;
    /**
     * taille maximum de mots diff�rents pour un seul document
     */
    public static int IDX_RESERVE = 64 * KILO;
    /**
     * taille initiale d'un vecteur d'indexation
     */
    public static final int AVG_INDEXDOC = 1; // initial size  of idx vector
    /**
     * taille initiale d'un vecteur de positions
     */
    public static final int AVG_INDEXPOS = 1; // initial size  of position vector
    /**
     * **********************************************************************************
     */
    // cache de query
    /**
     * taille du cache pour les query (donc mode=QUERY)
     */
    public static long QUERY_CACHE_SIZE = 128 * MEGA;
    /**
     * taille du cache pour les indexation
     */
    public static int QUERY_CACHE_COUNT = 128 * KILO;

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
    /**
     * **********************************************************************************
     */
    /**
     * to forge an url for original
     */
    public static String ORIGINAL_HOST = "localhost";
    public static String ORIGINAL_PORT = "5555";
    public static String ORIGINAL_FULL_URL = "HTTP";
    /**
     * définition de la notion d'url originale
     */
    public static String ORIGINAL_CODE = "org.olanto.idxvli.server.OriginalService_Default";
    public static OriginalService ORIGINAL_DEFINITION = new OriginalService_Default();
    /**
     * **********************************************************************************
     */
    /**
     * définition de la notion d'url originale
     */
    public static String PRE_PROCESSING_CODE = "org.olanto.idxvli.server.PreProcessingService_Default";
    public static PreProcessingService PRE_PROCESSING = new PreProcessingService_Default();
    public static int PRE_PROCESSING_INTERVAL = 3600;
    /**
     * Path to the file containing all the SERVER messages
     */
    public static String SERVER_MESSAGE_PATH = "C:/MYCAT/config/messages/interface/initserver";
    public static String SERVER_MESSAGE_LANG = "";
    public static ConstStringManager MSG;

    /**
     * affiche les param�tres. (� compl�ter pour tout voir)
     */
    public static void show() {
        COMLOG.info(
                "VERSION " + VERSION
                + "\nHOSTS"
                + "\n    EXPERIMENT_NOTFORPROD: " + CHECK_CONSISTENT
                + "\n    CHECK_CONSISTENT: " + CHECK_CONSISTENT
                + "\n    ORIGINAL_FULL_URL: " + ORIGINAL_FULL_URL
                + "\n    ORIGINAL_HOST: " + ORIGINAL_HOST
                + "\n    PRODUCTION_MODE: " + PRODUCTION_MODE
                + "\nMESSAGES"
                + "\n    SERVER_MESSAGE_PATH: " + SERVER_MESSAGE_PATH
                + "\n    SERVER_MESSAGE_LANG: " + SERVER_MESSAGE_LANG
                + "\nCONFIGURATION"
                + "\n    MODE_IDX: " + MODE_IDX
                + "\n    MODE_CONTINUE: " + MODE_CONTINUE
                + "\n    MODE_RANKING: " + MODE_RANKING
                + "\nFLAG"
                + "\n    IDX_WITHDOCBAG: " + IDX_WITHDOCBAG
                + "\n    IDX_SAVE_POSITION: " + IDX_SAVE_POSITION
                + "\n    IDX_MORE_INFO: " + IDX_MORE_INFO
                + "\n    IDX_CONCEPTUAL: " + IDX_CONCEPTUAL
                + "\n    IDX_ZIP_CACHE: " + IDX_CONCEPTUAL
               + "\n     FULL_LOAD: " + FULL_LOAD
                + "\nSTRATEGIES"
                + "\n    DOC_IMPLEMENTATION: " + DOC_IMPLEMENTATION
                + "\n         DOC_MAX: " + DOC_MAX
                + "\n         IDX_DONTINDEXTHIS: " + IDX_DONTINDEXTHIS
                + "\n         DOC_ENCODING: " + DOC_ENCODING
                + "\n         DOC_SIZE_NAME: " + DOC_SIZE_NAME
                + "\n         DOC_MAXOCCLENGTH: " + DOC_MAXOCCLENGTH
                + "\n         SEPARATOR: " + SEPARATOR
                + "\n       GLOSS_NAME: " + GLOSS_NAME
                + "\n       GLOSS_FIRST_IN_LIST: " + GLOSS_FIRST_IN_LIST
                + "\n       RENAMING"
                + "\n         FILE_RENAME: " + FILE_RENAME
                + "\n         FILE_COLLECTION_CASE: " + FILE_COLLECTION_CASE
                + "\n         FILE_NAME_CASE: " + FILE_NAME_CASE
                + "\n         FILE_EXTENTION_CASE: " + FILE_EXTENTION_CASE
                + "\n       PROPERTIES"
                + "\n         DOC_PROPERTIES_MAXBIT: " + DOC_PROPERTIES_MAXBIT
                + "\n         DOC_PROPERTIES_MAX_LENGHT: " + DOC_PROPERTIES_MAX_LENGHT
                + "\n         HOPE_COMPRESSION: " + HOPE_COMPRESSION
                + "\n         MAX_IN_ZIP_CACHE: " + MAX_IN_ZIP_CACHE
                + "\n    WORD_IMPLEMENTATION: " + WORD_IMPLEMENTATION
                + "\n         WORD_MAX: " + WORD_MAX
                + "\n         IDX_MFLF_ENCODING :" + IDX_MFLF_ENCODING
                + "\n         WORD_MINLENGTH: " + WORD_MINLENGTH
                + "\n         WORD_MAXLENGTH: " + WORD_MAXLENGTH
                + "\n         WORD_NFIRSTOFDOC: " + WORD_NFIRSTOFDOC
                + "\n     STEMMER"
                + "\n         WORD_USE_STEMMER: " + WORD_USE_STEMMER
                + "\n         STEM_DOC: " + STEM_DOC
                + "\n         WORD_STEMMING_LANG: " + WORD_STEMMING_LANG
                + "\n         STEM_CACHE_COUNT: " + STEM_CACHE_COUNT
                + "\n         STEM_KEEP_LIST: " + STEM_KEEP_LIST
                + "\n         ACTUAL_LANGUAGE: " + ACTUAL_LANGUAGE
                + "\n     QUOTEREF"
                + "\n         REALREF_MAX_CHECK: " + REALREF_MAX_CHECK
                + "\n         REF_MAX_THREAD: " + REF_MAX_THREAD
                + "\n     WILDCHAR"
                + "\n         WORD_EXPANSION: " + WORD_EXPANSION
                + "\n         WORD_EXPANSION_RELOAD: " + WORD_EXPANSION_RELOAD
                + "\n         WORD_MAX_EXPANSION: " + WORD_MAX_EXPANSION
                + "\n         DOCNAME_EXPANSION: " + DOCNAME_EXPANSION
                + "\n         DOCNAME_EXPANSION_RELOAD: " + DOCNAME_EXPANSION_RELOAD
                + "\n         DOCNAME_MAX_EXPANSION: " + DOCNAME_MAX_EXPANSION
                + "\n         DOCNAME_BROWSE_INSENSITIVE: " + DOCNAME_BROWSE_INSENSITIVE
                + "\n    OBJSTO_IMPLEMENTATION: " + OBJ_IMPLEMENTATION
                + "\n         OBJ_PW2: " + OBJ_PW2
                + "\n         OBJ_NB: " + OBJ_NB
                + "\n         OBJ_SMALL_SIZE: " + OBJ_SMALL_SIZE
                + "\n         OBJ_STORE_ASYNC: " + OBJ_STORE_ASYNC
                + "\n         OBJ_STORE_MAX_QUEUE: " + OBJ_STORE_MAX_QUEUE
                + "\n         OBJ_COMPRESSION: " + OBJ_COMPRESSION
                + "\nCACHE WRITE"
                + "\n         CACHE_IMPLEMENTATION_INDEXING: " + CACHE_IMPLEMENTATION_INDEXING
                + "\n         INDEXING_CACHE_SIZE: " + INDEXING_CACHE_SIZE
                + "\n         KEEP_IN_CACHE: " + KEEP_IN_CACHE
                + "\n         IDX_CACHE_COUNT: " + IDX_CACHE_COUNT
                + "\n         IDX_RESERVE: " + IDX_RESERVE
                + "\nCACHE READ"
                + "\n         CACHE_IMPLEMENTATION_READ: " + CACHE_IMPLEMENTATION_READ
                + "\n         QUERY_CACHE_SIZE: " + QUERY_CACHE_SIZE
                + "\n         QUERY_CACHE_COUNT: " + QUERY_CACHE_COUNT
                + "\nQUERY OPTION"
                + "\n    NEAR_DISTANCE: " + NEAR_DISTANCE
                + "\n    MAX_CITATION: " + MAX_CITATION
                + "\n    MAX_RESPONSE: " + MAX_RESPONSE);
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
}

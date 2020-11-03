/**
 ***LIC-BEG***
 * Copyright 2020 Olanto Foundation
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ***LIC-END***
 */
package knndbpedia;

import org.olanto.idxvli.*;
import static org.olanto.idxvli.IdxEnum.*;
import static org.olanto.idxvli.IdxConstant.*;

/**
 * Une classe pour initialiser les constantes. Une classe pour initialiser les
 * constantes. Cette classe doit être implémentée pour chaque application
 */
public class ConfigurationForKNN implements IdxInit {

    /**
     * crée l'attache de cette classe.
     */
    public ConfigurationForKNN() {
    }

    /**
     * initialisation permanante des constantes. Ces constantes choisies
     * définitivement pour toute la durée de la vie de l'index.
     */
    public void InitPermanent() {
        DOC_MAXBIT = 20;
        WORD_MAXBIT = 20;
        DOC_MAX = (int) Math.pow(2, DOC_MAXBIT);  // recalcule
        WORD_MAX = (int) Math.pow(2, WORD_MAXBIT); // recalcule

        WORD_IMPLEMENTATION = implementationMode.XL;
        DOC_IMPLEMENTATION = implementationMode.XL;
        OBJ_IMPLEMENTATION = implementationMode.XL;

        /**
         * nbre d'object storage actif = 2^OBJ_PW2
         */
        OBJ_PW2 = 0;  ///0=>1,1=>2,2=>4,3=>8,4=>16
        OBJ_NB = (int) Math.pow(2, OBJ_PW2);  ///0=>1,1=>2,2=>4,

        //PASS_MODE=PassMode.ONE;
        IDX_DONTINDEXTHIS = "C:\\SIMPLE_CLASS\\config\\dontindexthiswords_EN.txt";
        
        IDX_ZIP_CACHE = true;
        IDX_ZIP_CACHE_FASTLOAD = true;
        
        IDX_WITHDOCBAG = false; //
        //IDX_INDEX_MFL = false;
        IDX_MORE_INFO = false;

       IDX_SAVE_POSITION = false;  // pour TFxIDF_ONE
    //    IDX_SAVE_POSITION = true;  // pour TFxIDF
        MODE_RANKING = RankingMode.IDFxTDF;
        MAX_RESPONSE = 200;

        
        
        /**
         * taille maximum des noms de documents
         */
        DOC_SIZE_NAME = 20;

        WORD_MINOCCKEEP = 4;  // pour une indexation en deux passes
        WORD_MAXOCCDOCKEEP = 100;  // pour une indexation en deux passes
        WORD_NFIRSTOFDOC = 1000;

        IDX_MFLF_ENCODING = "UTF-8";
        //IDX_MFLF_ENCODING = "ISO-8859-1";
        WORD_MINLENGTH = 3;
        WORD_MAXLENGTH = 40;
        WORD_DEFINITION = new TokenCatNative();

        WORD_USE_STEMMER = false;
        STEM_DOC = false;
        WORD_STEMMING_LANG = "french"; // only for initialisation
        ACTUAL_LANGUAGE = "_FR";

        /* désactive les options qui ne servent pas à la classification */

        ORTOGRAFIC = false;
        IDX_MARKER = false;
        WORD_EXPANSION = false;
        DOCNAME_EXPANSION = false;
    }

    /**
     * initialisation des constantes de configuration (modifiable). Ces
     * constantes choisies définitivement pour toute la durée de la vie du
     * processus.
     */
    public void InitConfiguration() {

        // les directoire
        COMLOG_FILE = "C:/PATDB/dbpedia-knn/common.log";
        DETLOG_FILE = "c:/PATDB/dbpedia-knn/detail.log";

        String root = "c:/PATDB/dbpedia-knn";
        String root0 = "c:/PATDB/dbpedia-knn/sto0";
        IdxConstant.COMMON_ROOT = root;
        IdxConstant.DOC_ROOT = root;
        IdxConstant.WORD_ROOT = root;
        SetObjectStoreRoot(root0, 0);
        // paramètre de fonctionnement
        CACHE_IMPLEMENTATION_INDEXING = implementationMode.FAST;
        KEEP_IN_CACHE = 90;
        INDEXING_CACHE_SIZE = 2000 * MEGA;
        IDX_CACHE_COUNT = 32 * (int) MEGA;
        IDX_RESERVE = WORD_NFIRSTOFDOC + 4 * KILO;

        IdxConstant.CACHE_IMPLEMENTATION_READ = implementationMode.FAST;
        IdxConstant.QUERY_CACHE_SIZE = 1000 * MEGA;
        IdxConstant.QUERY_CACHE_COUNT = 8 * (int) MEGA;
    }

}

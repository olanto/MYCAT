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

package org.olanto.conman;

import java.util.*;
import org.olanto.idxvli.util.*;
import org.olanto.idxvli.doc.*;
import static org.olanto.util.Messages.*;
import static org.olanto.conman.ContentConstant.*;
import org.olanto.idxvli.*;
import static org.olanto.idxvli.IdxEnum.*;
import java.util.regex.*;

/**
 * Une classe pour construire un gestionnaire de contenu.
 *
 * 
 */
public class ContentStructure {

    /** dictionnaire de documents (document--indice) (indice--document)*/
    public DocumentManager docstable;
    /** la structure comprend un module de statistique*/
    public ContentStatistic Statistic;
    /** la structure comprend un module de gestion des IO*/
    public ContentIO IO;
    /** la structure comprend un module de gestion des IO*/
    public ContentManager CM;
    /** indice du dernier document indexé */
    public int lastdoc = 0;
    /** flag indiquant qu'il n'y pas de document invalide (permet de sauter les filtres */
    public static boolean ZERO_INVALID_DOC = false;
    /** sliper de texte */
    Pattern p = Pattern.compile(SEPARATOR);

    /** crée une structure d'indexation
     */
    public ContentStructure() {
    }

    /** crée une structure d'indexation . Permet de décomposer l'initalisation.
     * @param _mode (QUERY,INCREMENTAL,DIFFERENTIAL,)
     */
    public ContentStructure(String _mode) {
        MODE_IDX = IdxMode.valueOf(_mode);
    }

    /**
     * création d'une structure d'indexation. Permet de décomposer l'initalisation.
     * @param _mode (NEW_INDEXATION)
     * @param name  (pas utilisé)
     *
     */
    public ContentStructure(String _mode, String name) {
        if (!_mode.equals("NEW")) {
            error_fatal("must be in mode NEW_INDEXATION");
        }
        MODE_IDX = IdxMode.valueOf(_mode);
    }

    /** crée une structure d'indexation et initialise la.
     * @param _mode (NEW,QUERY,INCREMENTAL,DIFFERENTIAL,)
     * @param client la configuration client
     */
    public ContentStructure(String _mode, ContentInit client) {
        MODE_IDX = IdxMode.valueOf(_mode);
        createComponent(client);
        loadContentManager();
    }

    /* service IO */

    /**
     * update lastdoc var
     */
    public final void updateLastdoc() {
        lastdoc = docstable.getCount();
    }

    /**
     * load content manager
     */
    public final void loadContentManager() {
        IO.loadContentManager();
    }

    /** sauvegarder les données encore dans les caches et ferme l'un index
     */
    public final void close() {
        IO.saveContentManager();
    }

    /** récupère le nom de la racine de la structure d'indexation
     * @return dossier racine
     */
    public final String getIdxRootName() {
        return COMMON_ROOT;
    }

    /** récupère le nom de l'index actuellement actif
     * @return sac nom de fichier
     */
    public final String getIdxName() {
        return ContentConstant.currentf;
    }

    /** sauve le contenu
     * @param n terme
     * @return 
     */
    public byte[] loadContent(int n) {
        return IO.loadContent(n);
    }

    /** ajoute un contenu String.
     * @param docName identifiant
     * @param content contenu
     * @param lang
     * @param collection
     */
    synchronized public void addContent(String docName, String content, String lang, String collection) {
        try {
            byte[] b = content.getBytes(CONTENT_ENCODING);
            long fdate = System.currentTimeMillis();
            CM.addContent(docName, fdate, b, lang, "STRING", collection);

        } catch (Exception e) {
            error("addContent", e);
        }
    }

    /** ajoute un URL
     * @param url identifiant
     */
    synchronized public void addURL(String url) {
        CM.addURL(url);
    }

    /** sauver un contenu
     * @param docID identifiant
     * @param content contenu
     */
    synchronized public void saveContent(int docID, byte[] content) {
        IO.saveContent(docID, ContentManager.compress(content), content.length);
    }

    /** sauver un contenu
     * @param docID identifiant
     * @param content contenu
     * @param type
     */
    synchronized public void saveContent(int docID, String content, String type) {
        try {
            byte[] b = content.getBytes(CONTENT_ENCODING);
            IO.saveContent(docID, ContentManager.compress(b), b.length);
            docstable.setPropertie(docID, "TYPE." + type);
        } catch (Exception e) {
            error("saveContent string", e);
        }
    }

    /** sauver un contenu
     * @param docID identifiant
     * @param content contenu
     * @param type
     */
    synchronized public void saveContent(int docID, byte[] content, String type) {
        try {
            IO.saveContent(docID, ContentManager.compress(content), content.length);
            docstable.setPropertie(docID, "TYPE." + type);
        } catch (Exception e) {
            error("saveContent binary", e);
        }
    }

    /** récuper un contenu String.
     * @param docName identifiant
     * @return  contenu
     */
    synchronized public String getStringContent(String docName) {
        return CM.getStringContent(docName);
    }

    /** récuper un contenu Byte.
     * @param docName identifiant
     * @return  contenu
     */
    synchronized public byte[] getByteContent(String docName) {
        return CM.getByteContent(docName);
    }

    /** récuper un contenu String.
     * @param docID identifiant
     * @return  contenu
     */
    synchronized public String getStringContent(int docID) {
        return CM.getStringContent(docID);
    }

    /** récuper un contenu Byte.
     * @param docID identifiant
     * @return  contenu
     */
    synchronized public byte[] getByteContent(int docID) {
        return CM.getByteContent(docID);
    }

    /** récupère un contenu type String sur un intervalle donné.
     * @param docID identifiant
     * @param from debut
     * @param to fin
     * @return 
     */
    public String getStringContent(int docID, int from, int to) {
        return CM.getStringContent(docID, from, to);
    }

    /** récupère le contenu d'un répertoire.
     * @param pathName répertoire
     */
    synchronized public void getFromDirectory(String pathName) {
        CM.getFromDirectory(pathName, "NODEF", "NODEF", "ISO-8859-1");
    }

    /** récupère le contenu d'un répertoire.
     * @param pathName répertoire
     * @param language langage de la collection
     * @param collection nom de la collection
     * @param txt_encoding encodage des textes
     */
    synchronized public void getFromDirectory(String pathName, String language, String collection, String txt_encoding) {
        CM.getFromDirectory(pathName, language, collection, txt_encoding);
    }

    /** longeur d'un document.
     * @param d document
     * @return nbr de termes indexés
     */
    public int getLengthOfD(int d) {
        return docstable.getSize(d);
    }

    /** distance de kolmogorov entre deux documents.
     * @param d1 document
     * @param d2 document
     * @param bzip2
     * @return distance
     */
    public double distOfKolmogorov(int d1, int d2, boolean bzip2) {
        return CM.distOfKolmogorov(d1, d2, bzip2);
    }

    /**
     *
     * @param kd1
     * @param kd2
     * @param Sd1
     * @param Sd2
     * @param bzip2
     * @return
     */
    public double distOfKolmogorov(double kd1, double kd2, String Sd1, String Sd2, boolean bzip2) {
        return CM.distOfKolmogorov(kd1, kd2, Sd1, Sd2, bzip2);
    }

    /**
     *
     * @param Sd1
     * @param bzip2
     * @return
     */
    public double kdlength(String Sd1, boolean bzip2) {
        return CM.kdlength(Sd1, bzip2);
    }

    /** nom du fichier du document i
     * @param d ième document
     * @return nom de fichier
     */
    public String getFileNameForDocument(int d) {
        return docstable.get(d);
    }

    /** numéro du document ayant le libellé s
     * @param s nom du fichier
     * @return numéro du document (-1 = pas trouvé)
     */
    public int getIntForDocument(String s) {
        return docstable.get(s);
    }

    /** tout les documents satisfaisants une propriété
     * @param properties nom de la propriété
     * @return set de bits
     */
    public SetOfBits satisfyThisProperty(String properties) {
        return docstable.satisfyThisProperty(properties);
    }

    /** définir une  propriété pour un document
     * @param docID identifiant
     * @param properties nom de la propriété
     */
    public void setDocumentPropertie(int docID, String properties) {
        docstable.setPropertie(docID, properties);

    }

    /** éliminer une  propriété pour un document
     * @param docID identifiant
     * @param properties nom de la propriété
     */
    public void clearDocumentPropertie(int docID, String properties) {
        docstable.clearPropertie(docID, properties);

    }

    /**
     *  récupère le dictionnaire de propriétés
     * @return liste des propriétés actives
     */
    public List<String> getDictionnary() {
        return docstable.getDictionnary();
    }

    /**
     *  récupère le dictionnaire de propriétés ayant un certain préfix (COLECT., LANG.)
     * @param prefix préfixe des propriétés
     * @return liste des propriétés actives
     */
    public List<String> getDictionnary(String prefix) {
        return docstable.getDictionnary(prefix);
    }

    /** test si un document est indexable.
     * @param doc identifiant
     * @return si le document est indexable
     */
    public boolean isIndexable(int doc) {
        return docstable.getPropertie(doc, "TYPE.INDEXABLE") && !docstable.getPropertie(doc, "STATE.INDEXED");
    }

    /** détermine que le document est indexé.
     * @param doc identifiant
     */
    public void setIndexed(int doc) {
        docstable.setPropertie(doc, "STATE.INDEXED");
    }

    /** détermine que le document n'est pas indexé.
     * @param doc identifiant
     */
    public void clearIndexed(int doc) {
        docstable.clearPropertie(doc, "STATE.INDEXED");
    }

    /**
     *
     * @param docName
     * @param refName
     * @param title
     * @param cleantxt
     */
    public synchronized void setRefDoc(String docName, String refName, String title, String cleantxt) {
        try {

            String refdoc = refName + SEPARATOR + title + SEPARATOR + cleantxt;
            byte[] content = refdoc.getBytes(CONTENT_ENCODING);
            int idcontent = docstable.put(docName); // enregistre le numéro de la référence
            updateLastdoc();
            docstable.setPropertie(idcontent, "TYPE.REFDOC");
            docstable.setPropertie(idcontent, "TYPE.INDEXABLE");
            docstable.setPropertie(idcontent, "TYPE.STRING");
            IO.saveContent(idcontent, ContentManager.compress(content), content.length);
        } catch (Exception e) {
            error("setRefDoc", e);
        }
    }

//**** application ***** refname,title,context is not ok for this document 169007
//**** application ***** refname,title,context is not ok for this document 169007
//**** application ***** refname,title,context is not ok for this document 169027
//**** application ***** refname,title,context is not ok for this document 169027
//**** application ***** refname,title,context is not ok for this document 169036
//**** application ***** refname,title,context is not ok for this document 169036
//**** application ***** refname,title,context is not ok for this document 169048
//**** application ***** refname,title,context is not ok for this document 169048
//**** application ***** refname,title,context is not ok for this document 169062
//**** application ***** refname,title,context is not ok for this document 169062
//**** application ***** refname,title,context is not ok for this document 169099
//**** application ***** refname,title,context is not ok for this document 169099
//**** application ***** refname,title,context is not ok for this document 169217
//**** application ***** refname,title,context is not ok for this document 169217
//**** application ***** refname,title,context is not ok for this document 169310
//**** application ***** refname,title,context is not ok for this document 169310

    /**
     *
     * @param docName
     * @return
     */
    public String[] getRefDoc(String docName) {
        String[] res = new String[3];
        int id = this.getIntForDocument(docName);
        if (id == -1) {
            error("no title for this document(noref)" + docName);
            res[0] = "ref error";
            res[1] = "title error";
            res[2] = "context error";
            return res;
        }
        res = p.split(CM.getStringContent(id));
        if (res.length != 3) {
            error("refname,title,context is not ok for this document " + docName + " / " + res.length);
            res = new String[3];
            res[0] = "ref error";
            res[1] = "title error";
            res[2] = "context error";
            return res;
        }
        return res;
    }

    /**
     *
     * @param docName
     * @return
     */
    public String getRefName(String docName) {
        return getRefDoc(docName)[0];
    }

    /**
     *
     * @param docName
     * @return
     */
    public String getTitle(String docName) {
        return getRefDoc(docName)[1];
    }

    /**
     *
     * @param docName
     * @return
     */
    public String getCleanText(String docName) {
        return getRefDoc(docName)[2];
    }

    /**
     *
     * @param docName
     * @param from
     * @param to
     * @return
     */
    public String getCleanText(String docName, int from, int to) {
        boolean verbose = false;
        String allcontent = getCleanText(docName);
        if (verbose) {
            msg("getCleanText allcontent:" + 0 + "-" + allcontent.length());
        }
        String res = "no clue! for " + docName;
        if (from < allcontent.length()) {
            res = allcontent.substring(Math.max(0, from), Math.min(to, allcontent.length()));
        }
        return res;
    }

    /**
     * crée un composant d'indexation avec une racine pour les fichiers d'index
     * @param client
     */
    public void createComponent(ContentInit client) {
        // initialise les constantes et la configuration
        client.InitPermanent();
        client.InitConfiguration();
        IdxConstant.openLogger();
        IdxConstant.show();

        ContentConstant.show();
        // create reference
        Statistic = new ContentStatistic(this);
        IO = new ContentIO(this);
        CM = new ContentManager(this);

        if (MODE_IDX == IdxMode.NEW || MODE_IDX == IdxMode.INCREMENTAL || MODE_IDX == IdxMode.DIFFERENTIAL) {
            InitIndexer();
        }
        if (MODE_IDX == IdxMode.QUERY) {
            InitQuery();
        }
    }

    void InitQuery() {
        msg("init mode:" + MODE_IDX.name());
    }

    void InitIndexer() {
        msg("docdmax:" + DOC_MAX);
        msg("init mode:" + MODE_IDX.name());

    }
}

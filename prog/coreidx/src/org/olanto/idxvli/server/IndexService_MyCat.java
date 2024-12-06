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
package org.olanto.idxvli.server;

import java.rmi.*;
import java.util.HashMap;
import org.olanto.idxvli.*;
import org.olanto.idxvli.doc.*;
import org.olanto.idxvli.knn.*;
import org.olanto.idxvli.ref.UploadedFile;
import org.olanto.idxvli.util.SetOfBits;

/* -server -Xmx1400m -Djava.rmi.server.codebase="file:///c:/JG/VLI_RW/dist/VLI_RW.jar"  -Djava.security.policy="c:/JG/VLI_RW/rmi.policy" */

/* default permissions granted to all domains
 grant codeBase "file://c:/JG/VLI_RW/*" {
 permission java.security.AllPermission;
 };
 grant { 
 // JG modifier pour le rmi
 permission java.net.SocketPermission "*:1099-", "accept, connect, listen, resolve";
 permission java.security.AllPermission;
 };
 */
/**
 * Facade pour les services des index manager en modeRMI (ces services peuvent
 * être augmentés en publiant des méthodes de IdxStructure)
 *
 *
 */
public interface IndexService_MyCat extends Remote {

    /**
     * Pour demander des informations sur le serveur (pour vérifier la
     * connexion)
     *
     * @return nom du serveur ...
     * @throws java.rmi.RemoteException execption
     */
    public String getInformation() throws RemoteException;

    /**
     * Pour demander la construction de nouvelles structures pour l'indexeur
     * (dans le cas d'une réinitialisation complète)
     *
     * @param client configuration de la structure
     * @throws java.rmi.RemoteException execption
     */
    public void startANewOne(IdxInit client) throws RemoteException;

    /**
     * Pour demander l'ouverture d'un indexeur existant
     *
     * @param client configuration de la structure
     * @param mode (QUERY,INCREMENTAL,DIFFERENTIAL)
     * @param OpenCM
     * @throws java.rmi.RemoteException execption
     */
    public void getAndInit(IdxInit client, String mode, boolean OpenCM) throws RemoteException;

    /**
     * Pour demander la sauvegarde des modifications (le système repart avec les
     * paramètres initiaux)
     *
     * @throws java.rmi.RemoteException execption
     */
    public void saveAndRestart() throws RemoteException;

    /**
     * pour forcer l'écriture des caches
     *
     * @throws java.rmi.RemoteException execption
     */
    public void flush() throws RemoteException;

    /**
     * pour rendre visible les nouvelles indexations
     *
     * @throws java.rmi.RemoteException execption
     */
    public void showFullIndex() throws RemoteException;

    /**
     * Pour obtenir les statisques (nb doc, %compression, ...) de l'indexeur
     *
     * @return résultat des stat
     * @throws java.rmi.RemoteException execption
     */
    public String getStatistic() throws RemoteException;

    /**
     * pour stopper le serveur
     *
     * @throws java.rmi.RemoteException execption
     */
    public void quit() throws RemoteException;

    /**
     * pour stopper le process
     *
     * @throws java.rmi.RemoteException execption
     */
    public void stop() throws RemoteException;

    /**
     * pour indexer un répertoire
     *
     * @param path chemin
     * @throws java.rmi.RemoteException execption
     */
    public void indexdir(String path) throws RemoteException;

    /**
     * pour indexer un contenu
     *
     * @param path nom du document
     * @param content contenu à indexer
     * @throws java.rmi.RemoteException execption
     */
    public void indexThisContent(String path, String content) throws RemoteException;

    /**
     * pour indexer un contenu
     *
     * @param path nom du document
     * @param content contenu à indexer
     * @param lang langue du document
     * @throws java.rmi.RemoteException execption
     */
    public void indexThisContent(String path, String content, String lang) throws RemoteException;

    /**
     * pour indexer un contenu
     *
     * @param path nom du document
     * @param content contenu à indexer
     * @param lang langue du document
     * @param collection étiquettes des collections
     * @throws java.rmi.RemoteException execption
     */
    public void indexThisContent(String path, String content, String lang, String[] collection) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @return résultat
     * @throws java.rmi.RemoteException execption
     */
    public QLResult evalQL(String request) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @return résultat étendu avec le ranking
     * @throws java.rmi.RemoteException execption
     */
    public QLResultAndRank evalQLMore(String request) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request file pattern
     * @param langS
     * @param start depuis
     * @param size taille du résultat
     * @param collections
     * @param order type de tri
     * @param onlyOnFileName
     * @return résultat étendu avec snippet
     * @throws java.rmi.RemoteException execption
     */
    public QLResultNice browseNice(String request, String langS, int start, int size, String[] collections, String order, boolean onlyOnFileName) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @param start depuis
     * @param size taille du résultat
     * @return résultat étendu avec snippet
     * @throws java.rmi.RemoteException execption
     */
    public QLResultNice evalQLNice(String request, int start, int size) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @param start depuis
     * @param size taille du résultat
     * @param order critère d'ordre
     * @param exact
     * @param orderbyocc
     * @return résultat étendu avec snippet
     * @throws java.rmi.RemoteException execption
     */
    public QLResultNice evalQLNice(String request, int start, int size, String order, boolean exact, boolean orderbyocc) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request1 requête 1
     * @param request2 requête 2
     * @param start depuis
     * @param size taille du résultat
     * @param order critère d'ordre
     * @param chardist
     * @param orderbyocc
     * @return résultat étendu avec snippet
     * @throws java.rmi.RemoteException execption
     */
    public QLResultNice evalQLNice(String request1, String request2, int start, int size, String order, int chardist, boolean orderbyocc) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @param properties propriétés à vérifier
     * @param start depuis
     * @param size jusqua
     * @return résultat étendu avec snippet
     * @throws java.rmi.RemoteException execption
     */
    public QLResultNice evalQLNice(String request, String properties, int start, int size) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @param properties propriétés à vérifier
     * @param profile profil à vérifier
     * @param start depuis
     * @param size jusqua
     * @return résultat étendu avec snippet
     * @throws java.rmi.RemoteException execption
     */
    public QLResultNice evalQLNice(String request, String properties, String profile, int start, int size) throws RemoteException;

    /**
     * évaluation d'une requête
     *
     * @param request requête
     * @param lang propriétés à vérifier
     * @return résultat
     * @throws java.rmi.RemoteException execption
     */
    public QLResult evalQL(String lang, String request) throws RemoteException;

    /**
     * pour chercher les noms des documents
     *
     * @param docList liste d'identifiant
     * @return noms
     * @throws java.rmi.RemoteException execption
     */
    public String[] getDocName(int[] docList) throws RemoteException;

    /**
     * pour chercher les noms d'un document
     *
     * @param docid identifiant
     * @return nom du document
     * @throws java.rmi.RemoteException execption
     */
    public String getDocName(int docid) throws RemoteException;

    /**
     * établir la validité d'un document
     *
     * @param doc
     * @return validité
     * @throws java.rmi.RemoteException execption
     */
    public boolean docIsValid(int doc) throws RemoteException;

    /**
     * cherche le contenu d'un document
     *
     * @param docId
     * @return contenu
     * @throws java.rmi.RemoteException execption
     */
    public String getDoc(int docId) throws RemoteException;

    /**
     * cherche l'id d'un document
     *
     * @param docName nom
     * @return id
     * @throws java.rmi.RemoteException execption
     */
    public int getDocId(String docName) throws RemoteException;

    /**
     * Chercher les N premiers voisins du document d, sans formattage.
     *
     * @param doc document
     * @param N nombre de voisins
     * @return réponse
     * @throws java.rmi.RemoteException execption
     */
    public KNNResult KNNForDoc(int doc, int N) throws RemoteException;

    /**
     * Cherche l'encodage d'un document
     *
     * @return encodage
     * @throws java.rmi.RemoteException execption
     */
    public String getDOC_ENCODING() throws RemoteException;

    /**
     * export le vocabulaire de l'index
     *
     * @param min borne inférieure
     * @param max borne supérieure
     * @throws java.rmi.RemoteException execption
     */
    public void exportEntry(long min, long max) throws RemoteException;

    /**
     * retourne l'expansion d'un terme comportant des wildchar
     *
     * @param term
     * @return la liste
     * @throws java.rmi.RemoteException execption
     */
    public String[] ExpandTerm(String term) throws RemoteException;

    /**
     * récupère le dictionnaire de propriétés
     *
     * @return liste des propriétés actives
     * @throws java.rmi.RemoteException execption
     */
    public PropertiesList getDictionnary() throws RemoteException;

    /**
     * récupère le dictionnaire de propriétés ayant un certain préfix (COLECT.,
     * LANG.)
     *
     * @param prefix préfixe des propriétés
     * @return liste des propriétés actives
     * @throws java.rmi.RemoteException execption
     */
    public PropertiesList getDictionnary(String prefix) throws RemoteException;

    /**
     * détermine la langue (actuellement Markov)
     *
     * @param txt texte
     * @return langue
     * @throws java.rmi.RemoteException execption
     */
    public String getLanguage(String txt) throws RemoteException;

    /**
     * détermine les propriétés d'un document
     *
     * @param txt texte
     * @return propriétés
     * @throws java.rmi.RemoteException execption
     */
    public String[] getCollection(String txt) throws RemoteException;

    /**
     * ***********************************************************( pour myCAT
     */
    /**
     * détermine le chemin du corpus à convertir
     *
     * @return path
     * @throws java.rmi.RemoteException execption
     */
    public String getROOT_CORPUS_SOURCE() throws RemoteException;

    /**
     * détermine le chemin du corpus à indexer
     *
     * @return path
     * @throws java.rmi.RemoteException execption
     */
    public String getROOT_CORPUS_TXT() throws RemoteException;

    /**
     * détermine la taille du corpus
     *
     * @return dernier numéro de document enregistré
     * @throws java.rmi.RemoteException execption
     */
    public int getSize() throws RemoteException;

    /**
     * pour récupérer l'ensemble des documents satisfaisant une propriété (lang,
     * collection, state, etc)
     *
     * @param properties nom de la propiété
     * @return vector de bits
     * @throws java.rmi.RemoteException execption
     */
    public SetOfBits satisfyThisProperty(String properties) throws RemoteException;

    /**
     * pour éliminer l'ensemble des documents satisfaisant une propriété (lang,
     * collection, state, etc)
     *
     * @param properties nom de la propiété
     * @throws java.rmi.RemoteException execption
     */
    public void clearThisProperty(String properties) throws RemoteException;

    /**
     * pour associer une propriété à un document
     *
     * @param docID id
     * @param properties nom de la propiété
     * @throws java.rmi.RemoteException execption
     */
    public void setDocumentPropertie(int docID, String properties) throws RemoteException;

    /**
     * pour retrouver une propriété associée  à un document
     *
     * @param docID id
     * @param properties nom de la propiété
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public boolean getDocumentPropertie(int docID, String properties) throws RemoteException;
    
    
    /**
     * pour enlever une propriété à un document
     *
     * @param docID id
     * @param properties nom de la propiété
     * @throws java.rmi.RemoteException execption
     */
    public void clearDocumentPropertie(int docID, String properties) throws RemoteException;

    /**
     * pour récupérer les stops words
     *
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String[] getStopWords() throws RemoteException;

    /**
     * pour récupérer les langues des corpus
     *
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String[] getCorpusLanguages() throws RemoteException;

    /**
     * pour récupérer le document original
     *
     * @param docName
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String getOriginalUrl(String docName) throws RemoteException;

    /**
     * pour récupérer le path du document original
     *
     * @param docName
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String getOriginalPath(String docName) throws RemoteException;

    /**
     * pour récupérer le contenu d'un fichier
     *
     * @param d
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public byte[] getByte(String d) throws RemoteException;

    /**
     * pour récupérer le contenu du document original
     *
     * @param docName
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String getOriginalZipName(String docName) throws RemoteException;

    /**
     * pour récupérer le path du document original
     *
     * @param args
     * @throws java.rmi.RemoteException execption
     */
    public void executePreprocessing(String[] args) throws RemoteException;

    /**
     * pour récupérer les propriétés du client
     *
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public HashMap<String, String> getClientProperties() throws RemoteException;

    /**
     * pour chercher les références d'un fichier uploaded
     *
     * @param upfile
     * @param limit
     * @param source
     * @param target
     * @param selectedCollection
     * @param fast
     * @param removefirst
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public REFResultNice getReferences(UploadedFile upfile, int limit, String source, String target, String[] selectedCollection,
            boolean removefirst, boolean fast) throws RemoteException;

    /**
     * pour chercher les références d'un fichier uploaded
     *
     * @param upfile
     * @param limit
     * @param target
     * @param source
     * @param selectedCollection
     * @param removefirst
     * @param fast
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String getHtmlReferences(UploadedFile upfile, int limit, String source, String target, String[] selectedCollection,
            boolean removefirst, boolean fast) throws RemoteException;

    /**
     * pour chercher les références d'un fichier uploaded
     *
     * @param upfile
     * @param limit
     * @param RefType
     * @param removefirst
     * @param source
     * @param selectedCollection
     * @param fast
     * @param fromFile
     * @param target
     * @param DocSrc
     * @param DocTgt
     * @return 
     * @throws java.rmi.RemoteException execption
     */
    public String getXMLReferences(UploadedFile upfile, int limit, String source, String target, String[] selectedCollection,
            boolean removefirst, boolean fast, boolean fromFile, String DocSrc, String DocTgt, String RefType) throws RemoteException;

    /**
     *
     * @param RefType
     * @param DocSrc1
     * @param DocSrc2
     * @param DocTgt
     * @param RepTag1
     * @param RepTag2
     * @param Color2
     * @return
     * @throws RemoteException
     */
    public String mergeXMLReferences(String RefType, String DocSrc1, String DocSrc2, String DocTgt, String RepTag1, String RepTag2, String Color2) throws RemoteException;

    /**
     *
     * @param FileName
     * @param Content
     * @return
     * @throws RemoteException
     */
    public String createTemp(String FileName, String Content) throws RemoteException;

    /**
     *
     * @param FileName
     * @return
     * @throws RemoteException
     */
    public byte[] getTemp(String FileName) throws RemoteException;
}

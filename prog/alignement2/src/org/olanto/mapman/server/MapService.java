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

package org.olanto.mapman.server;

import java.rmi.*;
import org.olanto.mapman.MapArchiveInit;

/**
 *  Facade pour les services des contents manager en modeRMI (ces services peuvent
 * être augmentés en publiant des méthodes de MapArchiveStructure)
 * 
 */
public interface MapService extends Remote {

    /**
     * Pour demander des informations sur le serveur (pour vérifier la connexion)
     * @return nom du serveur ...
     * @throws java.rmi.RemoteException
     */
    public String getInformation() throws RemoteException;

    /**
     * Pour demander la construction de nouvelles structures pour le mappeur
     * (dans le cas d'une réinitialisation compl�te)
     * @param client configuration de la structure
     * @throws java.rmi.RemoteException
     */
    public void startANewOne(MapArchiveInit client) throws RemoteException;

    /**
     * Pour demander l'ouverture d'un conteneur existant
     * @param client configuration de la structure
     * @param mode (QUERY,INCREMENTAL,DIFFERENTIAL)
     * @throws java.rmi.RemoteException
     */
    public void getAndInit(MapArchiveInit client, String mode) throws RemoteException;

    /**
     * Pour demander la sauvegarde des modifications (le système repart avec les paramètres initiaux)
     * @throws java.rmi.RemoteException
     */
    public void saveAndRestart() throws RemoteException;

    /**
     * Pour obtenir les statisques (nb doc, %compression, ...) du mappeur
     * @return r�sultat des stat
     * @throws java.rmi.RemoteException
     */
    public String getStatistic() throws RemoteException;

    /**
     * pour stopper le serveur
     * @throws java.rmi.RemoteException
     */
    public void quit() throws RemoteException;

       /**
     * pour stopper le process
     * @throws java.rmi.RemoteException
     */
    public void stop() throws RemoteException;

    /** ajoute une map.
     * @param map map
     * @param docid la référence du document
     * @param lang lanque (pivot/cette langue)
     */
    public void addMap(IntMap map, int docid, String langfrom, String langto) throws RemoteException;

    /** récupère une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    public IntMap getMap(int docid, String langfrom, String langto) throws RemoteException;

    /** récupère une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    public boolean existMap(int docid, String langfrom, String langto) throws RemoteException;
   
    /** skip line ?.
     * 
     */
    public boolean isSkipLine() throws RemoteException;
}

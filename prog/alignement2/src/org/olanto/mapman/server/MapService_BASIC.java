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
import java.rmi.server.*;
import static org.olanto.util.Messages.*;
import java.util.concurrent.locks.*;
import org.olanto.mapman.MapArchiveInit;
import org.olanto.mapman.MapArchiveStructure;
import static org.olanto.mapman.MapArchiveConstant.*;

/**
 *  service de Map d'alignement.
 *
 * 
 * <p>
 * *  <pre>
 *  concurrence:
 *   - // pour les lecteurs
 *   - Ã©crivain en exclusion avec tous
 *  doit Ãªtre le point d'accÃ¨s pour toutes les structures utilisÃ©es !
 *  </pre>
 *   */
public class MapService_BASIC extends UnicastRemoteObject implements MapService {

    private MapArchiveStructure id;
    private MapArchiveInit clientForRestart;
    private String modeForRestart;

    public MapService_BASIC() throws RemoteException {
        super();
    }

    public String getInformation() throws RemoteException {
        return "this service is alive ... :MapService_BASIC";
    }
    /** opÃ©ration sur documentName verrous ------------------------------------------*/
    private final ReentrantReadWriteLock serverRW = new ReentrantReadWriteLock();
    private final Lock serverR = serverRW.readLock();
    private final Lock serverW = serverRW.writeLock();
    private final Lock serverCLEAN = serverRW.writeLock();

    public void startANewOne(MapArchiveInit client) throws RemoteException {
        serverW.lock();
        try {
            if (id != null) {
                error("already init: -> bad usage");
            } else {
                id = new MapArchiveStructure("NEW", client);
                id.Statistic.global();
                id.close();

                msg("wait a little and start map server ");

            }
        } finally {
            serverW.unlock();
        }

    }

    public void getAndInit(MapArchiveInit client, String mode) throws RemoteException {
        serverW.lock();
        modeForRestart = mode;
        clientForRestart = client;
        try {
            if (id != null) {
                error("already init: getAndInit");
            } else {
                msg("current Map manager is opening ...");
                id = new MapArchiveStructure(mode, client);
                id.Statistic.global();
                msg("current Map manager is opened");
            }
        } finally {
            serverW.unlock();
        }
    }

    public void saveAndRestart() throws RemoteException {
        serverW.lock();
        try {
            if (id == null) {
                error("server is not opened");
            } else {
                msg("-- restart Map manager");
                msg("current Map manager is closing ...");
                id.Statistic.global();
                id.close();
                msg("current Map manager is closed");
                msg("current Map manager is opening ...");
                id = new MapArchiveStructure(modeForRestart, clientForRestart);
                id.Statistic.global();
                msg("current Map manager is opened");
            }
        } finally {
            serverW.unlock();
        }
    }

    public void quit() throws RemoteException {
        if (id == null) {
            error("already close: quit");
        } else {
            serverW.lock();
            try {
                msg("current content manager is closing ...");
                id.Statistic.global();
                id.close();
                msg("current content manager is closed");
                msg("server must be stopped  & restarted ...");
                id = null;
            } finally {
                //serverW.unlock(); ne rend pas le verrou, accepte seulement le stop
            }
        }
    }

    public void stop() throws RemoteException {
            msg("kill the process & all other service running in the same process ...");
            try {
                System.exit(0);
             } finally {
               // serverW.unlock(); // seulement pour la symétrie ...
         }
    }
   
    
    public synchronized String getStatistic() throws RemoteException {
        if (id != null) {
            return id.Statistic.getGlobal();
        }
        return "content is not opened";
    }

    /** ajoute une map.
     * @param map map
     * @param docid la rÃ©fÃ©rence du document
     * @param lang lanque (pivot/cette langue)
     */
    public void addMap(IntMap map, int docid, String langfrom, String langto) throws RemoteException {
        serverW.lock();
        try {
            id.addMap(map, docid, langfrom, langto);
        } finally {
            serverW.unlock();
        }

    }

    /** rÃ©cupÃ¨re une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    public IntMap getMap(int docid, String langfrom, String langto) throws RemoteException {
        serverR.lock();
        try {
            return id.getMap(docid, langfrom, langto);
        } finally {
            serverR.unlock();
        }
    }

    /** rÃ©cupÃ¨re une map.
     * @param map map
     * @param lang lanque (pivot/cette langue)
     */
    public boolean existMap(int docid, String langfrom, String langto) throws RemoteException {
        serverR.lock();
        try {
            return id.existMap(docid, langfrom, langto);
        } finally {
            serverR.unlock();
        }

    }
     /** skip line ?.
     * 
     */
    public boolean isSkipLine() throws RemoteException{
        return SKIP_LINE;
        
    }

    
}

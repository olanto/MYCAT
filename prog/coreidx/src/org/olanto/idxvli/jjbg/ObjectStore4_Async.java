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

package org.olanto.idxvli.jjbg;

import static org.olanto.util.Messages.*;
import java.util.concurrent.*;
import static org.olanto.idxvli.IdxEnum.*;
import static org.olanto.idxvli.IdxConstant.*;
import org.olanto.idxvli.IdxIO;

/**
 * Une classe stocker des vecteurs de bytes sur disque.
 * 
 *
 *
 *
 * cette version implémente une mise à jour asynchrone, il est supposé que seul la
 * séquence create, put, .... put, close est valide
 *
 * la désynchronisation est assurée par une queue et un thread qui consome la queue,
 * si la queue est pleine, le producteur est mis en attente (voir put and take)
 *
 * donc les méthodes get, printstatistique, ... ne donne pas de résultat
 *
 * v4.0
 *  modification realSize et StoredSize  //21-11-2005
 *  <pre>
 *  concurrence:
 *   -objectstore fait le controle de la concurrence
 *  </pre>
 */
public class ObjectStore4_Async implements ObjectStorage4 {

    private String path;

    // une classe pour structurer les commmandes dans la queue
    class CommandQueue {
        // id=-1 == fin

        int id;
        int[] value;
        int size; // taille réel du int 
        int realsize; // taille réel du byte sans compression 
        byte[] rawValue;

        CommandQueue(int[] value, int id, int size) {
            this.id = id;
            this.value = value;
            this.size = size;
        }

        CommandQueue(byte[] rawValue, int id, int realsize) {
            this.id = id;
            this.rawValue = rawValue;
            this.realsize = realsize;
        }
    }

    // classe chargée de la consommation de la queue
    class Consummer extends Thread {

        ArrayBlockingQueue<CommandQueue> Q;
        ObjectStorage4 objsto;

        Consummer(ArrayBlockingQueue<CommandQueue> Q, ObjectStorage4 objsto) {
            this.Q = Q;
            this.objsto = objsto;
        }

        public void run() {
            int countCmd = 0;
            while (true) {
                try {
                    CommandQueue cmd = (CommandQueue) Q.take();  // attend si vide
                    if (cmd.id == -1) {
                        msg("receive a closing command");
                        objsto.printStatistic();
                        objsto.close();
                        break;
                    }
                    countCmd++;
                    if (countCmd % 10000 == 0) {
                        msg(path + " N0:" + countCmd + " size of Q" + Q.size());
                        //objsto.printStatistic();
                    }
                    if (cmd.value != null) {
                        objsto.append(cmd.value, cmd.id, cmd.size);
                    } // vecteur de int
                    else if (cmd.rawValue != null) {
                        objsto.append(cmd.rawValue, cmd.id, cmd.realsize);
                    } // vecteur de byte
                    else {
                        error_fatal("problem in asynchrone consummer: bad command:" + cmd.toString());
                    }
                } catch (Exception e) {
                    error("problem in asynchrone consummer:", e);
                    //System.exit(1);
                }
            }
            COMLOG.info("end thread ...");
            IdxIO.signalAClosingObjSto();
        }
    }
    ObjectStorage4 objsto;  // l'objet à mettre à jour
    ArrayBlockingQueue<CommandQueue> Q;   // le mécanisme de désynchronisation

    /**
     * an objectstore with async behaviour
     */
    public ObjectStore4_Async() {
    }

    /** créer une nouvelle instance de ObjectStore à partir des données existantes*/
    private ObjectStore4_Async(implementationMode implementation,
            String _pathName, readWriteMode _RW) {  // recharge un gestionnaire
        path = _pathName;
        objsto = (new ObjectStore4()).open(implementation, _pathName, _RW);
        initThread();
    }

    /** créer une nouvelle instance de ObjectStore */
    private ObjectStore4_Async(implementationMode implementation,
            String _pathName, int _maxSize, int _minBigSize) {  //
        path = _pathName;
        createObjectStore(implementation, _pathName, _maxSize, _minBigSize);
    }

    /**  crée un ObjectStorage de taille 2^maxSize à l'endroit indiqué par le path
     * @param implementation
     * @param path
     * @param maxSize
     * @param minBigSize
     * @return valeur */
    public final ObjectStorage4 create(implementationMode implementation,
            String path, int maxSize, int minBigSize) {
        return (new ObjectStore4_Async(implementation, path, maxSize, minBigSize));
    }

    public final ObjectStorage4 open(implementationMode implementation,
            String path, readWriteMode _RW) {
        return (new ObjectStore4_Async(implementation, path, _RW));
    }

    /**
     *
     * @param implementation
     * @param _pathName
     * @param _maxSize
     * @param _size_0
     */
    public final void createObjectStore(
            implementationMode implementation,
            String _pathName,
            int _maxSize,
            int _size_0) {
        objsto = (new ObjectStore4()).create(implementation, _pathName, _maxSize, _size_0);
    }

    private final void initThread() { // n'utiliser que la première fois, à la création
        Q = new ArrayBlockingQueue<CommandQueue>(OBJ_STORE_MAX_QUEUE);
        Thread c = (Thread) new Consummer(Q, objsto);
        c.start();

    }

    public final void close() {
        try {
            CommandQueue cmd = new CommandQueue((byte[]) null, -1, 0);  // -1 est la commande d'arrét
            System.out.println("Ask for closing Object store");
            Q.put(cmd);
        } catch (Exception e) {
            error("problem during Asynchrone closing ...", e);
            //System.exit(1);
        }
    }

    public final int append(int[] b, int user, int to) {
        try {
            CommandQueue cmd = new CommandQueue(b, user, to);
            Q.put(cmd);
        } catch (Exception e) {
            error("problem during Aappend ...", e);
            //System.exit(1);
        }
        return user;
    }

    public final void printStatistic() {
        objsto.printStatistic();
    }

    // pas implémenté, car en mode écriture seulement
    public final void printNiceId(int user) {
        //error("printNiceId not implemented");
        objsto.printNiceId(user);
    }

    public final void releaseId(int user) {
        error("releaseId not implemented");
    }

    public final int[] readInt(int user) {
        return objsto.readInt(user);
    }

    /**  retourne la taille de l'obje
     * @param user n
     * @return t
     */
    public final int storedSize(int user) {
        return objsto.storedSize(user);
    }

    /**  retourne la taille réel de l'objet sans compression (uniquement en mode 1 passe
     * @param user
     * @return )*/
    public int realSize(int user) {
        return objsto.realSize(user);
    }

    public final int append(byte[] b, int user, int realsize) {
        try {
            CommandQueue cmd = new CommandQueue(b, user, realsize);
            Q.put(cmd);
        } catch (Exception e) {
            error("problem during append ...", e);
            //System.exit(1);
        }
        return user;
    }

    public final void resetStatistic() {
        error("resetStatistic not implemented");
    }

    public final long getSpace() { // calcule en byte l'espace occupé
        error("getSpace not implemented");
        return -1;
    }
}

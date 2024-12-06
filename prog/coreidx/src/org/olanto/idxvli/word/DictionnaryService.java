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

package org.olanto.idxvli.word;

import java.rmi.*;
import static org.olanto.idxvli.IdxEnum.*;

/**
 * 
 * 
 *
 * @author xtern
 */
public interface DictionnaryService extends Remote {

    /**
     *
     * @return
     * @throws RemoteException
     */
    public String getInformation() throws RemoteException;

    /**
     *
     * @param implementation
     * @param path
     * @param file
     * @param maxSize
     * @param maxLengthSizeOfName
     * @throws RemoteException
     */
    public void create(implementationMode implementation,
            String path, String file, int maxSize, int maxLengthSizeOfName) throws RemoteException;

    /**
     *
     * @param implementation
     * @param RW
     * @param path
     * @param file
     * @throws RemoteException
     */
    public void open(implementationMode implementation,
            readWriteMode RW, String path, String file) throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    public void close() throws RemoteException;

    /**
     *
     * @param word
     * @return
     * @throws RemoteException
     */
    public int put(String word) throws RemoteException;

    /**
     *
     * @param word
     * @return
     * @throws RemoteException
     */
    public int get(String word) throws RemoteException;

    /**
     *
     * @param i
     * @return
     * @throws RemoteException
     */
    public String get(int i) throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    public int getCount() throws RemoteException;

    /**
     *
     * @throws RemoteException
     */
    public void printStatistic() throws RemoteException;
}

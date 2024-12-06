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

package org.olanto.server.test;

import org.olanto.idxvli.*;
import org.olanto.util.Timer;

/** Test de l'indexeur, cr�ation d'un nouvel index
 * 
  *<p>
 *
 */
public class CreateIndex {

    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        id = new IdxStructure("NEW", new ConfigurationNative());


        //id.indexdir("C:/D");// indexation du dossier sp�cifi�
        id.indexdir("C:/DOC");// indexation du dossier sp�cifi�
        //id.indexdir("C:/jdk1.5/docs");// indexation du dossier sp�cifi�
        //id.indexdir("E:/WIPO/EN");// indexation du dossier sp�cifi�
        id.flushIndexDoc();  //  vide les buffers       
        id.Statistic.global();
        id.close();
        t1.stop();

    }
}

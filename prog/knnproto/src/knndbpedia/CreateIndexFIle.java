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

package knndbpedia;

import org.olanto.idxvli.*;
import org.olanto.util.Timer;

/**
 * *
 * 
 *
 *
 * Test de l'indexeur, création d'un nouvel index
 */
public class CreateIndexFIle {

    private static IdxStructure id;
    private static Timer t1 = new Timer("global time");

    /**
     * application de test
     * @param args sans
     */
    public static void main(String[] args) {
        create();

    }

    private static void create() {
        id = new IdxStructure(
                "NEW", // mode
                "dont forget to clean directories before");
        // création de la racine de l'indexation
        id.createComponent(new ConfigurationForKNN());
        // charge l'index (si il n'existe pas alors il est vide)
        id.loadIndexDoc();





        id.indexdir("C:/MYCAT/prog/knnproto/data");
 


        id.flushIndexDoc();

        id.Statistic.global();

        id.close();

        t1.stop();

        System.out.println("======================================================================");
    }
}

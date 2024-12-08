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

package org.olanto.idxvli.word.test;

/* générique test pour les différentes implémenations remplacer le nom de l'implémentation .... */
import java.rmi.*;
import org.olanto.idxvli.word.*;
import static org.olanto.util.Messages.*;

/**
 * 
 * 
 *
 *
 * @author xtern
 */
public class TestStringRepository_Close {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {


        try {

            System.out.println("connect to serveur");

            Remote r = Naming.lookup("rmi://jg4/TERMDICT");

            System.out.println("access to serveur");

            if (r instanceof DictionnaryService) {
                DictionnaryService is = ((DictionnaryService) r);
                String s = is.getInformation();
                System.out.println("chaîne renvoyée = " + s);
                msg("close");
                is.close();
                msg("is closed now");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

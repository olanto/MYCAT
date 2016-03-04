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

package org.olanto.mycat.tmx.generic;

import java.rmi.*;
import org.olanto.idxvli.server.*;
import static org.olanto.util.Messages.*;

/**
 * arrêter les services
 */
public class StopAgent {

    public static void main(String[] args) {


        try {

            System.out.println("connect to server TMX (How2Say)");
            Remote r = Naming.lookup("rmi://localhost/TMX");
            System.out.println("access to server");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                String s = is.getInformation();
                System.out.println("serve says = " + s);
                System.out.println("send a STOP to" + s);
                is.quit();
                msg("TMX ending ...");
                is.stop();
                msg("TMX force stop ...");
            }
        } catch (Exception e) {
           // e.printStackTrace();
           msg("Warning !: server TMX is not there");
        }


        // waiting
        //Object Monitor
        final Object monitor = new Object();
        new Thread() {
            //threads run method

            public void run() {
                try {
                    //synchronize on object monitor
                    synchronized (monitor) {
                        int waiting = 2;
                        System.out.println("Waiting for " + waiting + " seconds ...");
                        //Wait on object monitor for 10000 milliseconds
                        monitor.wait(waiting * 1000);
                        System.out.println("Wait End");
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }.start(); //starting the thread
    }
}

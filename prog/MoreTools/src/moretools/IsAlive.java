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
package moretools;

import java.rmi.Naming;
import java.rmi.Remote;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.olanto.idxvli.server.IndexService_MyCat;

/**
 * arrêter les services
 */
public class IsAlive {

    static int waiting = 60;

    public static void main(String[] args) {

        // waiting
        //Object Monitor
        final Object monitor = new Object();
        new Thread() {
            //threads run method

            public void run() {
                while (true) {
                    try {
                        //synchronize on object monitor
                        synchronized (monitor) {

                            System.out.print("Test at "+(new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss")).format(new Date()));
                            //Wait on object monitor for n*1000 milliseconds
                            testAlive();
                            monitor.wait(waiting * 1000);
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }.start(); //starting the thread
    }

    public static void testAlive() {
        try {

            //System.out.print("connect to server VLI,");
            Remote r = Naming.lookup("rmi://localhost/VLI");
            //System.out.println("access to server");
            if (r instanceof IndexService_MyCat) {
                IndexService_MyCat is = ((IndexService_MyCat) r);
                String s = is.getInformation();
                System.out.println(" serve says = " + s);
              }
        } catch (Exception e) {
            // e.printStackTrace();
             System.out.println(" Warning !: server VLI is not there");
        }
//        try {
//            System.out.println("connect to server MAP");
//            Remote r = Naming.lookup("rmi://localhost/MAP");
//            System.out.println("access to server");
//            if (r instanceof MapService) {
//                MapService is = ((MapService) r);
//                String s = is.getInformation();
//                System.out.println("serve says = " + s);
//                System.out.println("send a STOP to" + s);
//                is.quit();
//                msg("MAP ending ...");
//                msg("all process will be stopped ...");
//                is.stop();  // doit être la derniere chose à faire
//            }
//        } catch (Exception e) {
//            // e.printStackTrace();
//            msg("Warning ! : server MAP is not there or stopped");
//        }


     }
}

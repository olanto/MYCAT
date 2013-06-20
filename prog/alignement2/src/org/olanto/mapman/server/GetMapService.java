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
import static org.olanto.util.Messages.*;

/**
 * implémentation des services de map
 * 
 *
 */
public class GetMapService {

    public static MapService getServiceMAP(String serviceName) {

        MapService is;

        try {
            System.out.println("try to connect server " + serviceName);
            Remote r = Naming.lookup(serviceName);
            System.out.println("access to serveur");
            if (r instanceof MapService) {
                is = ((MapService) r);
                String s = is.getInformation();
                System.out.println("Test ... return = " + s);
                msg(serviceName + " service open ...");
                return is;
            } else {
                msg(serviceName + " exist but not a right type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MapService runServiceMAP(MapArchiveInit client, String serviceName) {

        MapService mapobj;
        try {
            System.out.println("initialisation de ContentService_BASIC ...");

            mapobj = new MapService_BASIC();
            mapobj.getAndInit(client, "INCREMENTAL");

            System.out.println("Enregistrement du serveur");

            //      String name="rmi://"+java.net.InetAddress.getLocalHost()+"/IDX";
            //String name = "rmi://localhost/CM";
            System.out.println("name:" + serviceName);
            Naming.rebind(serviceName, mapobj);
            System.out.println("Server Map manager started");
            return mapobj;
        } catch (Exception e) {
            error("Server Map manager", e);
        }
        return null;

    }
}

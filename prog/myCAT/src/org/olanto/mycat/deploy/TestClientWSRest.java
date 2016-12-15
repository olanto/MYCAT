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

package org.olanto.mycat.deploy;

import org.olanto.conman.server.GetContentService;
import org.olanto.idxvli.ref.UploadedFile;
import org.olanto.idxvli.ref.UtilsFiles;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.*;
import org.olanto.idxvli.server.*;
import static org.olanto.util.Messages.*;
import org.olanto.util.Timer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.senseos.SenseOS;

/** test une le service de quotation
 *
 */
public class TestClientWSRest {

    static IndexService_MyCat is;

    public static void main(String[] args) throws FileNotFoundException {



        is = GetContentService.getServiceMYCAT("rmi://localhost/VLI");

        testref();
        
    }

    public static void testref() throws FileNotFoundException {

        try {
            // test ref
            //REFResultNice ref = is.getReferences(up, 3, "EN", "FR", collect,true,true);
            String ref = is.getXMLReferences(null, 3, "EN", "FR", null,
            false, false, true,
                    "C:\\MYCAT\\doc2process\\A_RES_53_144_EN.pdf",
                    "C:\\MYCAT\\doc2process\\A_RES_53_144_EN.xml",
                    "TESTREF") ;
    
            System.out.println(ref);
            

        } catch (RemoteException ex) {
            Logger.getLogger(TestClientWSRest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

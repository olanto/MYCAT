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
package org.olanto.mycat.tmx.support;

import org.olanto.idxvli.ref.UploadedFile;
import org.olanto.idxvli.ref.UtilsFiles;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.olanto.idxvli.server.*;
import org.olanto.util.Timer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.olanto.idxvli.IdxStructure;
import org.olanto.senseos.SenseOS;

/**
 * test une le service de quotation
 *
 */
public class TestQueryWithoutServer {

    static IdxStructure id;

    public static void main(String[] args) throws FileNotFoundException {

        id = new IdxStructure("QUERY", new ConfigurationIndexingGetFromFile(SenseOS.getMYCAT_HOME("MYCAT_TMX") + "/config/IDX_fix.xml"));

        System.out.println(id.getFileNameForDocument(0));
        System.out.println(id.getFileNameForDocument(1));
        System.out.println(id.getFileNameForDocument(2));
      System.out.println(id.getFileNameForDocument(3));


    }

  
}

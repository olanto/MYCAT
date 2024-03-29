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

package org.olanto.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/** opération sur les documents à convertir
 */
public class Document extends File {

    private static final Logger _logger = Logger.getLogger(Document.class);
    private String badfilesPath = null;

    public Document(String pathname) {
        super(pathname);
    }

    /**
     * Get extention of the file
     * @return
     */
    public String getExtention() {
        if (getName().lastIndexOf('.') >= 0) {
            return getName().substring(getName().lastIndexOf('.') + 1);
        } else {
            return "";
        }
    }

    @Override
   public Document[] listFiles() {
        try {
            File[] list = super.listFiles();
            Document[] toReturn = new Document[list.length];
            int cpt = 0;
            for (File f : list) {
                toReturn[cpt++] = new Document(f.getAbsolutePath());
            }
            return toReturn;
        } catch (Exception e) {
            System.out.println("Could not read this folder: " + this.getName());
            _logger.warn("Could not read this folder: " + this.getName());
            return null;
        }
    }

    public void setBadfilesPath(String badfilesPath) {
        this.badfilesPath = badfilesPath;
    }

    public boolean hasBadfile() {
        boolean toReturn = false;
        File badfile = null;
        if (this.badfilesPath != null) {
            badfile = new File(this.badfilesPath + File.separator + this.getName());
            toReturn = (badfile.exists() && badfile.lastModified() == this.lastModified());
        }
        return toReturn;
    }

    public void copyToBadfile() {
        File badfile = null;
        if (this.badfilesPath == null) {
            _logger.warn("Could not create badfile, because field badfilesPath is 'null'.");
            return;
        } else {
            badfile = new File(this.badfilesPath + File.separator + this.getName());
            badfile.getParentFile().mkdirs();
        }
//        FileWriter wtr = null;
        FileOutputStream out=null;
        try {
//            BufferedReader rdr = new BufferedReader(new FileReader(this));
//            wtr = new FileWriter(badfile);
//
//            char[] cbuf = new char[1024 * 1024];
//            int len = rdr.read(cbuf);
//            while (len != -1) {
//                wtr.write(cbuf, 0, len);
//                len = rdr.read(cbuf);
//            }
//            rdr.close();
            
             FileInputStream in = new FileInputStream(this);
            out = new FileOutputStream(badfile);
            byte[] b = new byte[1024*32];
            int count = 0;
            while ((count = in.read(b)) != -1) {
                out.write(b, 0, count);
            }
            in.close();
        
            
        } catch (IOException ex) {
            _logger.error(ex.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                _logger.error(ex.getMessage());
            }
            if (badfile.exists()) {
                badfile.setLastModified(this.lastModified());
            }
        }
    }

 
    public Document replaceExtention(String ext) {
        return new Document(getParent() + separatorChar + getName().substring(0, getName().lastIndexOf('.') + 1) + ext);
    }

    public void removeBadfile() {
        File badfile = null;
        if (this.badfilesPath != null) {
            badfile = new File(this.badfilesPath + File.separator + this.getName());
            if (badfile.exists()) {
                badfile.deleteOnExit();
            }
        }
    }

    public void copyTo(Document newTarget) {
        //File badfile = null;
        if (newTarget == null) {
            _logger.warn("Could not copy, because target file is 'null'.");
            return;
        } else {
            newTarget.getParentFile().mkdirs();
        }
        FileWriter wtr = null;
        try {
            BufferedReader rdr = new BufferedReader(new FileReader(this));
            wtr = new FileWriter(newTarget);

            char[] cbuf = new char[1024 * 1024];
            int len = rdr.read(cbuf);
            while (len != -1) {
                wtr.write(cbuf, 0, len);
                len = rdr.read(cbuf);
            }
            rdr.close();
        } catch (IOException ex) {
            _logger.error(ex.getMessage());
        } finally {
            try {
                wtr.close();
            } catch (IOException ex) {
                _logger.error(ex.getMessage());
            }
            if (newTarget.exists()) {
                newTarget.setLastModified(this.lastModified());
            }
        }
    }

    Object getDocumentElement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

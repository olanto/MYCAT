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

import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.log4j.Logger;

/** implémentation du service IConverter.
 *
 * 
 */
public abstract class AbstractConverterFactory implements IConverter {

    private static final Logger _logger = Logger.getLogger(AbstractConverterFactory.class);
    protected boolean success = false;
    protected String outputFormat = Constants.TXT;
    protected Document source, target;

    public void init(final Document source, final Document target) {
        this.success = false;
        this.source = source;
        this.target = target;

        if (this.target != null) {
            this.target.getParentFile().mkdirs();
        }
    }

    public abstract void startConvertion();

    public boolean isConverted() {
        return this.success;
    }

    /**
     *  lire les bytes de ce fichier
     * retourne le vecteur si ok sinon null
     * @param fileName fichier
     * @return valeurs
     */
    protected static final byte[] readBytes(String fileName) {
        RandomAccessFile pdfFile = null;
        try {
            pdfFile = new RandomAccessFile(fileName, "r");
            long n = pdfFile.length();
            _logger.debug("readBytes " + n + " from: " + fileName);
            byte[] byteidx = new byte[(int) n];
            pdfFile.seek(0); // position la tête sur le début du block

            pdfFile.read(byteidx); // lire le vecteur
            pdfFile.close();

            return byteidx;
        } catch (IOException e) {
            _logger.error(e.getMessage(), e);
            if (pdfFile != null) {
                try {
                    pdfFile.close();
                } catch (IOException ex) {
                    _logger.error(ex.getMessage(), ex);
                }
            }
            return null;  // en erreur

        }
    }

    String getName() {
        if (this.source != null && this.target != null) {
            return this.source.getName() + " to " + this.target.getName();
        } else {
            return "No name";
        }
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setModificationDate() {
        if (source.exists() && target.exists()) {
            target.setLastModified(source.lastModified());
        }
    }
}

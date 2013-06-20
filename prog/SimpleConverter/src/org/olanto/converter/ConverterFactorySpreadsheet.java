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

import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.OfficeException;

/** pour les Spreadsheet.
 */
public class ConverterFactorySpreadsheet extends ConverterFactoryOffice {

    private static final Logger _logger = Logger.getLogger(ConverterFactorySpreadsheet.class);

    public static ConverterFactorySpreadsheet getInstance() {
        return new ConverterFactorySpreadsheet();
    }

    @Override
    public void init(Document docSource, Document docTarget) {
        super.init(docSource, docTarget);
//        if (outputFormat.equals(Constants.TXT))
//        {
//            target = new Document(target.getParent()+Document.separatorChar+getName().substring(0, getName().lastIndexOf('.')+1)+Constants.CSV);
//            _logger.debug("target: "+target);
//        }
    }

    @Override
    public void startConvertion() {

        OfficeDocumentConverter converter = new OfficeDocumentConverter(ConverterFactoryOffice.getOfficeManager());
        _logger.debug("Start converting " + source.getName());
        long startTime = System.currentTimeMillis();
        try {
            converter.convert(source, target);
            setModificationDate();
            target.copyTo(target.replaceExtention(Constants.TXT));
            target.deleteOnExit();
            success = true;
        } catch (OfficeException e) {
            close();
            _logger.error(e.getMessage());
        }
        _logger.debug("convertion time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}

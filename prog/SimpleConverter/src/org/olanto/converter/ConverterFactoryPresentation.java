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

/**
 *
 * @author Administrator
 */
public class ConverterFactoryPresentation extends ConverterFactoryOffice {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryPresentation.class);
    private Document targetPDF = null;

    public static ConverterFactoryPresentation getInstance() {
        return new ConverterFactoryPresentation();
    }

    @Override
    public void init(Document docSource, Document docTarget) {
        super.init(docSource, docTarget);
        if (outputFormat.equals(Constants.TXT)) {
            targetPDF = new Document(docTarget.getParent() + Document.separatorChar + docTarget.getName().substring(0, docTarget.getName().lastIndexOf('.') + 1) + Constants.PDF);
            _logger.debug("target: " + targetPDF);
        }
    }

    @Override
    public void startConvertion() {
        _logger.debug("Start converting " + source.getName());

        OfficeDocumentConverter converter = new OfficeDocumentConverter(ConverterFactoryOffice.getOfficeManager());

        long startTime = System.currentTimeMillis();
        try {
            converter.convert(source, targetPDF);
            //success = true;
        } catch (OfficeException e) {
            //close();
            _logger.error(e.getMessage());
        }
        close();
        if (targetPDF.exists()) {
            AbstractConverterFactory converterFactory = ConverterFactoryPDF.getInstance();
            if (converterFactory != null) {
                converterFactory.init(targetPDF, target);
                converterFactory.setOutputFormat(outputFormat);
                converterFactory.startConvertion();
                success = converterFactory.isConverted();
            }
            converterFactory = null;
        }
        targetPDF.delete();
        _logger.debug("convertion time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}

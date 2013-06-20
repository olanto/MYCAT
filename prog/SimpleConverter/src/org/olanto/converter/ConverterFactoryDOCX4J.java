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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

/**
 *
 * pour les docx
 */
public class ConverterFactoryDOCX4J extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryDOCX4J.class);

    public static AbstractConverterFactory getInstance() {
        return new ConverterFactoryDOCX4J();
    }

    @Override
    public void startConvertion() {
        _logger.debug("Start converting " + source.getName());
        long startTime = System.currentTimeMillis();
        Writer out = null;
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(source);
            if (outputFormat.equalsIgnoreCase(Constants.TXT)) {
                MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
                org.docx4j.wml.Document wmlDocumentEl = (org.docx4j.wml.Document) documentPart.getJaxbElement();
                out = new OutputStreamWriter(new FileOutputStream(target));
                org.docx4j.TextUtils.extractText(wmlDocumentEl, out);
                success = true;
            } else if (outputFormat.equalsIgnoreCase(Constants.HTML)) {
                AbstractHtmlExporter exporter = new HtmlExporterNG2();
                StreamResult result = new StreamResult(new FileOutputStream(target));
                exporter.html(wordMLPackage, result, target.getName() + "_files");
                success = true;
            }

        } catch (Docx4JException e) {
            _logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            _logger.error(e.getMessage(), e);
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                _logger.error(ex.getMessage(), ex);
            }
        }


        _logger.debug("convertion time: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}

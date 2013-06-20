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

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFText2HTML;
import org.apache.pdfbox.util.PDFTextStripper;

/** pour les PDF.
 *
 * 
 */
public class ConverterFactoryPDF extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryPDF.class);
    private String password = "password";

    public static AbstractConverterFactory getInstance() {
        _logger.debug("Build new : ConverterFactoryPDF");
        return new ConverterFactoryPDF();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public void startConvertion() {
        _logger.debug("Start converting " + source.getName());

        boolean sort = true;
        String fixEncoding = "UTF-8";
        Charset encoding = Charset.forName(fixEncoding);
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;

        Writer output = null;
        Writer outputFile = null;
        PDDocument document = null;
        try {
            document = PDDocument.load(source);

//            if (document.isEncrypted()) {
//                _logger.warn("Try to extract text from encrypted document :" + source.getAbsolutePath());
//                document.decrypt(password);
//            }

            PDFTextStripper stripper = null;
            if (outputFormat.equalsIgnoreCase(Constants.HTML)
                    || outputFormat.equalsIgnoreCase(Constants.HTM)) {
                stripper = new PDFText2HTML(fixEncoding);
            } else if (outputFormat.equalsIgnoreCase(Constants.TXT)) {
                stripper = new PDFTextStripper(fixEncoding);
            } else {
                _logger.warn("Could not convert PDF file to " + outputFormat);
            }

            if (stripper != null) {
                _logger.debug("open stripper : " + encoding.name());
                stripper.setSortByPosition(sort);
                stripper.setStartPage(startPage);
                stripper.setEndPage(endPage);

                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                output = new OutputStreamWriter(buf, encoding);
                output.write(stripper.getText(document));
                output.flush();

                outputFile = new OutputStreamWriter(new FileOutputStream(target), encoding);
                outputFile.write(buf.toString(encoding.name()));
                buf = null;
                success = true;
                _logger.debug("(writed) file : " + target.getAbsolutePath());
            } else {
                _logger.debug("Destination format not yet implemented");
            }

//        } catch (CryptographyException ex) {
//            _logger.error("(Error while decripting) file : " + source.getAbsolutePath(), ex);
//        } catch (InvalidPasswordException ex) {
//            _logger.error("(Bad password) file : " + source.getAbsolutePath(), ex);
        } catch (IOException ex) {
            _logger.error("(rejected cannot be opened) file : " + source.getAbsolutePath(), ex);
        } catch (Exception ex) {
            _logger.error("(rejected) file : " + source.getAbsolutePath(), ex);
        } finally {
            try {
                _logger.debug("(closing) file : " + target.getAbsolutePath());
                if (outputFile != null) {
                    outputFile.close();
                }
                if (output != null) {
                    output.close();
                }
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
                _logger.error("Could not close document", e);
            }
        }
    }

    /*   @Deprecated
    public void startConvertion2() {
    _logger.debug("startConvertion");
    byte[] pdf = readBytes(source.getAbsolutePath());
    if (pdf != null) {
    boolean sort = true;
    
    String encoding = "UTF-8";
    int startPage = 1;
    int endPage = Integer.MAX_VALUE;
    
    Writer output = null;
    Writer outputFile = null;
    PDDocument document = null;
    try {
    document = PDDocument.load(new ByteArrayInputStream(pdf));
    
    if (document.isEncrypted()) {
    _logger.warn("Try to extract text from encrypted document :" + source.getAbsolutePath());
    document.decrypt(password);
    }
    
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    output = new OutputStreamWriter(buf, encoding);
    
    PDFTextStripper stripper = null;
    if (outputFormat.equalsIgnoreCase(Constants.HTML)
    || outputFormat.equalsIgnoreCase(Constants.HTM)) {
    stripper = new PDFText2HTML(fixEncoding);
    } else if (outputFormat.equalsIgnoreCase(Constants.TXT)) {
    stripper = new PDFTextStripper();
    _logger.debug("open stripper");
    } else {
    _logger.warn("Could not convert PDF file to " + outputFormat);
    }
    
    stripper.setSortByPosition(sort);
    stripper.setStartPage(startPage);
    stripper.setEndPage(endPage);
    stripper.writeText(document, output);
    
    String s = new String(buf.toString(encoding));
    
    if (!isText(s.substring(0, Math.min(2000, s.length())))) {
    _logger.warn("(rejected) file: " + source.getAbsolutePath() + " = " + s.substring(0, Math.min(2000, s.length())));
    } else {
    _logger.debug("(accepted) file : " + source.getAbsolutePath());
    outputFile = new FileWriter(target);
    outputFile.write(s);
    outputFile.flush();
    //outputFile.close();
    success = true;
    _logger.debug("(writed) file : " + target.getAbsolutePath());
    }
    
    } catch (CryptographyException ex) {
    _logger.error("(Error while decripting) file : " + source.getAbsolutePath(), ex);
    } catch (InvalidPasswordException ex) {
    _logger.error("(Bad password) file : " + source.getAbsolutePath(), ex);
    } catch (IOException ex) {
    _logger.error("(rejected cannot be opened) file : " + source.getAbsolutePath(), ex);
    } finally {
    try {
    _logger.debug("(closing) file : " + target.getAbsolutePath());
    if (outputFile != null) {
    outputFile.close();
    }
    if (output != null) {
    output.close();
    }
    if (document != null) {
    document.close();
    }
    } catch (IOException e) {
    _logger.error("Could not close document", e);
    }
    }
    } else {
    _logger.debug("Source file is not a PDF file");
    }
    }
    
    public static boolean isText(String s) {
    int count = 0;
    for (int i = 0; i < s.length(); i++) {
    if (s.charAt(i) == ' ' || s.charAt(i) == '\n' || s.charAt(i) == '\t') {
    count++;
    }
    }
    if (count * 100 / (s.length() + 1) > 6) {  // +1 pour éviter les divisions par zéro
    
    return true;
    } // au moins x % de blancs 8 semble assez raisonable
    else {
    return false;
    }
    }
     *
     */
}

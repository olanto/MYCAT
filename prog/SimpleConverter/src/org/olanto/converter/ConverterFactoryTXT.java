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
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;

/**
 * pour le TXT.
 */
public class ConverterFactoryTXT extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryTXT.class);

    public static AbstractConverterFactory getInstance() {
        _logger.debug("Build new : ConverterFactoryTXT");
        return new ConverterFactoryTXT();
    }
//     private String outputEncoding = System.getProperty("file.encoding");
    private String outputEncoding = "UTF-8";

    public void setEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    @Override
    public void startConvertion() {
        _logger.debug("Start converting " + source.getName());

        // BufferedInputStream streamData = null;
        BufferedReader in = null;
        BufferedWriter writer = null;
        try {
            // CharsetDetector detector = new CharsetDetector();
            // streamData = new BufferedInputStream(new FileInputStream(source));
            // detector.setText(streamData);
            // CharsetMatch match = detector.detect();
            // _logger.info("detected encoding: " +match.getName()+" confidence:"+match.getConfidence()+" for:"+ source.getName());
            // in = new BufferedReader(new InputStreamReader(new FileInputStream(source), match.getName()));
            // in = new BufferedReader(new InputStreamReader(new FileInputStream(source)));

            writer = new BufferedWriter(new FileWriterWithEncoding(target, outputEncoding));
            // writer = new BufferedWriter(new FileWriter(target));

            StringBuffer txt = new StringBuffer("");
            // try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(source), "UTF-8");
            in = new BufferedReader(isr);
            String w = in.readLine();
            while (w != null) {
                txt.append(w);
                txt.append("\n");
                w = in.readLine();
            }

            writer.write(txt.toString());
            writer.flush();
            writer.close();
            in.close();
            success = true;
        } catch (Exception e) {
            _logger.error("Writer error: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}

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

import java.io.BufferedWriter;
import java.io.IOException;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.htmlparser.beans.StringBean;

/** pour le html
 */
public class ConverterFactoryHTM extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryHTM.class);

    public static AbstractConverterFactory getInstance() {
        _logger.debug("Build new : ConverterFactoryHTM");
        return new ConverterFactoryHTM();
    }

    @Override
    public void startConvertion() {
        BufferedWriter writer = null;
        try {
            _logger.debug("Start converting " + source.getName());

            System.out.println("Start converting " + source.getAbsolutePath());

            StringBean sb = new StringBean();
            sb.setLinks(false);
            sb.setReplaceNonBreakingSpaces(true);
            sb.setCollapse(true);

            // sb.setURL("http://www.apache.org/");
            sb.setURL(source.toURI().toString()); // the HTTP is performed here
            writer = new BufferedWriter(new FileWriterWithEncoding(target, "UTF-8"));
            String s = sb.getStrings();
            writer.write(s);
            writer.flush();
            writer.close();
            success = true;
        } catch (IOException ex) {
            _logger.error("Writer error: " + ex.getMessage());
            // System.out.println("Writer error: "+ex.getMessage());

        }

    }
}

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
package org.olanto.converter;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.artofsolving.jodconverter.process.LinuxProcessManager;
import org.artofsolving.jodconverter.process.ProcessManager;
import org.artofsolving.jodconverter.process.ProcessQuery;
import org.artofsolving.jodconverter.process.PureJavaProcessManager;
import org.artofsolving.jodconverter.process.SigarProcessManager;
import org.artofsolving.jodconverter.util.PlatformUtils;

/**
 *
 * pour les documents Office
 */
public abstract class ConverterFactoryOffice extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryOffice.class);
    private static OfficeManager officeManager = null;
    private static int retries = 0;

    public static OfficeManager getOfficeManager() {
        if (officeManager == null) {
            try {
                officeManager = new DefaultOfficeManagerConfiguration().setTaskExecutionTimeout(30000L).buildOfficeManager();
                if (officeManager != null) {
                    officeManager.start();
                }
            } catch (NullPointerException e) {
                _logger.error(e.getMessage());
                close();
            } catch (IllegalStateException e) {
                _logger.error(e.getMessage());
                close();
            } catch (OfficeException e) {
                _logger.error(e.getMessage(), e);
                close();
            }
            if (officeManager == null && retries < 3) {
                retries++;
                _logger.warn("Retry to start OpenOffice = " + retries);
                getOfficeManager();
            } else {
                retries = 0;
            }
        }
        return officeManager;

    }

    public static void close() {
        try {
            if (officeManager != null) {
                officeManager.stop();
            }
        } catch (OfficeException e) {
            _logger.error(e.getMessage());
            ProcessManager process = new PureJavaProcessManager();
            if (PlatformUtils.isLinux()) {
                process = new LinuxProcessManager();
                System.out.println("Linux Platform.");
            } else {
                process = new SigarProcessManager();
                System.out.println("Non-Linux Platform.");
            }
            ProcessQuery query = new ProcessQuery("soffice.*", "socket,host=127.0.0.1,port=2002");
            try {
                Long pid = process.findPid(query);
                _logger.info("Trying to kill soffice process: " + pid);
                process.kill(null, pid);

            } catch (IOException e1) {
                _logger.error(e1.getMessage());
            }
        } finally {
            officeManager = null;
        }
    }


    @Override
    public abstract void startConvertion();
}

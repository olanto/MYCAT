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

import org.apache.log4j.Logger;

/**
 * choix de la méthode de conversion adapté à l'extension
 */
public class ConverterControler {

    private final static Logger _logger = Logger.getLogger(ConverterControler.class);
    private boolean success = false;
    private AbstractConverterFactory converterFactory;
    // default value
    private String outputFormat = Constants.TXT;
    // default value
    private int iMaxRetry = 2;
    private boolean usePlugin = false;
    
    public static ConverterControler getInstance() {
        _logger.debug("Build new Converter");
        return new ConverterControler();
    }

    public void init(final Document source, final Document target)
            throws UnsupportedExtentionException {
        init(source, target, 0);
    }

    public void init(final Document source, final Document target, int numberOfRetry)
            throws UnsupportedExtentionException {
        _logger.debug("Init converter with file extention: " + source.getExtention());
        Document newTarget = new Document(target.getAbsolutePath());

        if (ConfigUtil.hasPluginForExtension(source.getExtention())) {
            if (numberOfRetry < iMaxRetry) {
                usePlugin = true;
                converterFactory = PluginConverterFactory.getInstance();
            } else if (ConfigUtil.applyBuiltin(source.getExtention())) {
                usePlugin = false;
                numberOfRetry=0;
            }
        }
        if (!usePlugin) {
            if (source.getExtention().equalsIgnoreCase(Constants.PDF)) {
                converterFactory = ConverterFactoryPDF.getInstance();
            } else if (source.getExtention().equalsIgnoreCase(Constants.HTML)
                    || source.getExtention().equalsIgnoreCase(Constants.HTM)) {
                converterFactory = ConverterFactoryHTM.getInstance();
            } else if (source.getExtention().equalsIgnoreCase(Constants.DOCX) && numberOfRetry == 0
                    || source.getExtention().equalsIgnoreCase(Constants.DOC)
                    || source.getExtention().equalsIgnoreCase(Constants.RTF)
                    //|| source.getExtention().equalsIgnoreCase(Constants.TXT)
                    //            || source.getExtention().equalsIgnoreCase(Constants.HTML)
                    //            || source.getExtention().equalsIgnoreCase(Constants.HTM)
                    || source.getExtention().equalsIgnoreCase(Constants.ODT)
                    || source.getExtention().equalsIgnoreCase(Constants.WPD)
                    || source.getExtention().equalsIgnoreCase(Constants.WPF)) {
                converterFactory = ConverterFactoryDOC.getInstance();
            } else if (source.getExtention().equalsIgnoreCase(Constants.TXT)) {
                converterFactory = ConverterFactoryTXT.getInstance();
            } else if (source.getExtention().equalsIgnoreCase(Constants.DOCX) && numberOfRetry < iMaxRetry) {
                converterFactory = ConverterFactoryDOCX4J.getInstance();
            } else if (source.getExtention().equalsIgnoreCase(Constants.XLS)
                    || source.getExtention().equalsIgnoreCase(Constants.ODS)) {
                converterFactory = ConverterFactorySpreadsheet.getInstance();
                if (this.outputFormat.equalsIgnoreCase(Constants.TXT)) {
                    newTarget = target.replaceExtention(Constants.CSV);
                }
            } else if (source.getExtention().equalsIgnoreCase(Constants.PPT)
                    || source.getExtention().equalsIgnoreCase(Constants.ODP)) {
                converterFactory = ConverterFactoryPresentation.getInstance();
            } else {
                converterFactory = null;
            }
        }

        if (converterFactory != null) {
            converterFactory.init(source, newTarget);
            _logger.debug("converterFactory: " + converterFactory.getName());
        } else {
            throw new UnsupportedExtentionException("Code for this extention is not yet implemented: " + source.getExtention());
        }
    }

    public void run() {
        if (converterFactory != null) {
            converterFactory.setOutputFormat(outputFormat);
            converterFactory.startConvertion();
            success = converterFactory.isConverted();
            if (success) {
                converterFactory.setModificationDate();
                _logger.info("Convertion finished (OK): " + converterFactory.getName());
            } else {
                _logger.info("Convertion finished (failed): " + converterFactory.getName());
            }
        }
    }

    public boolean isConverted() {
        return this.success;
    }

    void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    void setMaxRetry(int iMaxRetry) {
        this.iMaxRetry = iMaxRetry;
    }
    
    public Boolean usePlugin(){
        return this.usePlugin;
    }
}

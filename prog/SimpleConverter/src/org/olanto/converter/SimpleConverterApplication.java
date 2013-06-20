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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 * construit un convertisseur
 *
 */
public class SimpleConverterApplication {

    private final static Logger _logger = Logger.getLogger(SimpleConverterApplication.class);
    private String outputFormat = Constants.TXT;
    private int iMaxRetry = 2;
    private static long globalNumberOfFiles = 1L;
    private static long globalStartTime;
    private static boolean KEEP_EXTENSION = true;

    public static SimpleConverterApplication getInstance() {
        globalStartTime = System.currentTimeMillis();
        return new SimpleConverterApplication();
    }
    private long localNumberOfFiles = 1L;
    private long statisticBadfiles = 0;
    private long statisticConvertedFiles = 0;
    private long statisticNotSupportedFiles = 0;
    private Collection<String> statisticNotSupportedExtentionList = new ArrayList<String>();

    /**
     *
     * @param source source folder or file
     * @param toCovertPath directory
     */
    public void convertObject(final Document source, final Document target, final String badfilesPath) {

        if (source.isFile()) {
            source.setBadfilesPath(badfilesPath);
            convertDocument(source, target);
        } else if (source.isDirectory() && target.isDirectory()) {
            System.out.println("new folder: " + source.getName());
            if (!source.canRead()) {
                System.out.println("Could not read this folder: " + source.getName());
            } else {  // peut le lire
                Document[] list = source.listFiles();
                if (list != null) {
                    for (Document f : list) {
                        Document newTarget = new Document(target.getAbsolutePath() + File.separator + f.getName());
                        String newBadfilesPath = badfilesPath.toString();
                        if (f.isDirectory()) {
                            newBadfilesPath = badfilesPath + File.separator + f.getName();
                            newTarget.mkdirs();
                        } else if (f.isFile()) {
                            String newTargetPath;
                            if (KEEP_EXTENSION) {
                                newTargetPath = newTarget.getAbsolutePath() + '.' + outputFormat;
                                //System.out.println("target:" + newTargetPath);
                            } else {
                                newTargetPath = newTarget.getAbsolutePath().substring(0, newTarget.getAbsolutePath().lastIndexOf('.') + 1) + outputFormat;
                            }
                            newTarget = new Document(newTargetPath);

                        }
                        convertObject(f, newTarget, newBadfilesPath);
                    }
                }
            }
        } else {
            _logger.warn("Could not convert from source: " + source.getAbsolutePath());
            _logger.warn("Could not convert to   target: " + target.getAbsolutePath());
        }
    }

    /**
     *
     * @param source
     * @param target
     */
    private void convertDocument(final Document source, final Document target) {

        _logger.info("**************** " + (globalNumberOfFiles++) + " ******************");

        if ((globalNumberOfFiles % 1000) == 0) {
            float deltaTime = (System.currentTimeMillis() - globalStartTime);
            if (deltaTime > 1000) {

                _logger.info("Global number of files: " + globalNumberOfFiles
                        + ", convert " + (globalNumberOfFiles - localNumberOfFiles)
                        + " in time="
                        + deltaTime / 1000f
                        + "s ("
                        + (deltaTime / (globalNumberOfFiles - localNumberOfFiles))
                        + "ms/file)");
                globalStartTime = System.currentTimeMillis();
                localNumberOfFiles = globalNumberOfFiles;
            }
        }
        // Do not convert if source file has badfile
        if (source.hasBadfile()) {
            _logger.info("(stopped because BADFILE found) file: " + source.getName());
            ConverterReport.badFiles.info(source.getAbsolutePath());
            statisticBadfiles++;
            return;
        }

        // Do not convert if target file already exist
        if (target.exists() && source.lastModified() == target.lastModified()) {
            _logger.info("(stopped because already converted) file: " + source.getName());
            ConverterReport.convertedFiles.info(source.getAbsolutePath());
            statisticConvertedFiles++;
            return;
        }

        // Start convertion
        long startTime = System.currentTimeMillis();

        _logger.debug("converting file: " + source.getName());
        ConverterControler converterControler = ConverterControler.getInstance();
        converterControler.setMaxRetry(iMaxRetry);
        try {
            converterControler.init(source, target);
            converterControler.setOutputFormat(outputFormat);
            converterControler.run();

            if (converterControler.isConverted()) {
                ConverterReport.convertedFiles.info(source.getAbsolutePath());
                statisticConvertedFiles++;
                source.removeBadfile();
            } else {
                int retryCount = 1;
                while (retryCount <= iMaxRetry) {
                    _logger.warn("(retry convertion) file: " + source.getName());
                    converterControler.init(source, target, retryCount++);
                    converterControler.run();
                    if (retryCount==iMaxRetry && !converterControler.isConverted() && (!converterControler.usePlugin() 
                            || (converterControler.usePlugin() && !ConfigUtil.applyBuiltin(source.getExtention())))) {
                        _logger.info("(considered as BADFILE) file: " + source.getName());
                        ConverterReport.badFiles.info(source.getAbsolutePath());
                        statisticBadfiles++;
                        source.copyToBadfile();
                        break;
                    } else if (converterControler.isConverted()) {
                        ConverterReport.convertedFiles.info(source.getAbsolutePath());
                        statisticConvertedFiles++;
                        source.removeBadfile();
                        break;
                    }
                }


            }
        } catch (UnsupportedExtentionException e) {
            ConverterReport.notSupportedExtention.info(source.getAbsolutePath());
            statisticNotSupportedFiles++;
            _logger.warn(e.getMessage());
            if (!statisticNotSupportedExtentionList.contains(source.getExtention())) {
                statisticNotSupportedExtentionList.add(source.getExtention());
            }
        }
        _logger.info("convertion time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setMaxRetry(int iMaxRetry) {
        this.iMaxRetry = iMaxRetry;
    }

    public long getNotSupportedCount() {
        return statisticNotSupportedFiles;
    }

    public String getNotSupportedExtentionList() {
        String toReturn = "";
        for (String str : statisticNotSupportedExtentionList) {
            toReturn += str + " ";
        }
        return toReturn.trim();
    }

    public long getConvertedCount() {
        return statisticConvertedFiles;
    }

    public long getBadfilesCount() {
        return statisticBadfiles;
    }

    public long getGlobalFilesCount() {
        return globalNumberOfFiles;
    }
}

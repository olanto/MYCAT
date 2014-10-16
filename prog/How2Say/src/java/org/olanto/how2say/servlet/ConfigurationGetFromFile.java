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
package org.olanto.how2say.servlet;

import org.olanto.senseos.SenseOS;
import java.io.FileInputStream;
import java.util.Properties;
import static org.olanto.util.Messages.*;
import static org.olanto.how2say.servlet.How2SayConstant.*;

/**
 * Une classe pour initialiser les constantes.
 */
public class ConfigurationGetFromFile implements How2SayInit {

    String fileName = "to be initialised";
    Properties prop;

    /** cr�e l'attache de cette classe.
     */
    public ConfigurationGetFromFile() {
    }

    /**
     * charge la configuration depuis un fichier de properties
     * @param fileName nom du fichier
     */
    public ConfigurationGetFromFile(String fileName) {
        this.fileName = fileName;
        FileInputStream f = null;
        try {
            f = new FileInputStream(fileName);
        } catch (Exception e) {
            error_fatal("cannot find properties file:" + fileName);
        }
        try {
            prop = new Properties();
            prop.loadFromXML(f);
        } catch (Exception e) {
            error("errors in properties file:" + fileName, e);
            System.exit(0);
        }
        msg("properties from: " + fileName);
        prop.list(System.out);
    }

    /** initialisation permanante des constantes.
     * Ces constantes choisies d�finitivement pour toute la dur�e de la vie de l'index.
     */
    public void InitPermanent() {

 
        LIST_OF_LANG = prop.getProperty("LIST_OF_LANG", "XX YY");
      CORPUS_NAME = prop.getProperty("CORPUS_NAME", "Corpus ?");

 

    }

    /** initialisation des constantes de configuration (modifiable).
     * Ces constantes choisies d�finitivement pour toute la dur�e de la vie du processus.
     */
    public void InitConfiguration() {
        // les directoire
//        FOLDER_ORIGINAL = prop.getProperty("FOLDER_ORIGINAL", SenseOS.getMYCAT_HOME() + "/corpus/docs");
//        FOLDER_CONVERTED = prop.getProperty("FOLDER_CONVERTED", SenseOS.getMYCAT_HOME() + "/corpus/source");
//        FOLDER_CONVERTED_GLOSSARIES = prop.getProperty("FOLDER_CONVERTED_GLOSSARIES", SenseOS.getMYCAT_HOME() + "/corpus/source/Glossaries");
//        FOLDER_SEGMENTED = prop.getProperty("FOLDER_SEGMENTED", SenseOS.getMYCAT_HOME() + "/corpus/txt");
//        FOLDER_EXTRA_SOURCE = prop.getProperty("FOLDER_EXTRA_SOURCE", SenseOS.getMYCAT_HOME() + "/corpus/source/BY_EXT");


    }
}

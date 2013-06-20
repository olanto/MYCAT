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
package org.olanto.senseos;

import java.util.Map;
import static org.olanto.util.Messages.*;

/** 
 * Gestion de l'OS et des dépendances d'installation
 */
public class SenseOS {

    private static final String WINDOWS_FAMILIES = "WINDOWS_FAMILIES";
    private static final String UNIX_FAMILIES = "UNIX_FAMILIES";
    private static final String DEF_WINDOWS_HOME = "C:/";
    private static final String DEF_UNIX_HOME = "/home/olanto/";
    private static String OS_TYPE = null;
    private static String MYCAT_HOME = null;
    private static String MYPREP_HOME = null;
    private static  Map<String,String> env;
 
    
    /**
     * permet d'effectuer le test de cette classe
     * @param args pas utilisés
     */
    public static void main(String args[]) {
        
       msg("OS_TYPE:"+getOS_TYPE());
       msg("MYCAT_HOME:"+getMYCAT_HOME());
       msg("NUMBER_OF_PROCESSORS:"+getENV("NUMBER_OF_PROCESSORS"));
    }

    /**
     * @return the OS_TYPE windows ou unix
     */
    public static String getOS_TYPE() {
        if (OS_TYPE == null) {//
            String runningOS = System.getProperty("os.name");
            msg("running OS:" + runningOS);
            if (runningOS.startsWith("Window")) {
                OS_TYPE = WINDOWS_FAMILIES;
            } else {  // pour le moment tous les autres sont des Unix !!!
                OS_TYPE = UNIX_FAMILIES;
            }
            env=System.getenv();
        }
        return OS_TYPE;
    }

    /** permet de forcer le type de OS
     * @param aOS_TYPE the OS_TYPE to set
     */
    public static void setOS_TYPE(String aOS_TYPE) {
        OS_TYPE = aOS_TYPE;
    }

    /** retourne le home de myCAT
     * @return AT_HOMEthe MYC
     */
    public static String getMYCAT_HOME() {
          if (MYCAT_HOME == null) {// par encore initialisée
              if (env==null) { // init env
                  getOS_TYPE();
              }
             String res= env.get("MYCAT_HOME");
             if (res==null){ // pas défini
                 if(getOS_TYPE().equals(WINDOWS_FAMILIES))
                     MYCAT_HOME=DEF_WINDOWS_HOME+"MYCAT";
                 else
                     MYCAT_HOME=DEF_UNIX_HOME+"MYCAT";
             }
             else MYCAT_HOME=res;
          }    
        return MYCAT_HOME;
    }
 
       /** retourne le home de myCAT
     * @return AT_HOMEthe MYC
     */
    public static String getMYPREP_HOME() {
          if (MYPREP_HOME == null) {// par encore initialisée
              if (env==null) { // init env
                  getOS_TYPE();
              }
             String res= env.get("MYPREP_HOME");
             if (res==null){ // pas défini
                 if(getOS_TYPE().equals(WINDOWS_FAMILIES))
                     MYPREP_HOME=DEF_WINDOWS_HOME+"MYPREP";
                 else
                     MYPREP_HOME=DEF_UNIX_HOME+"MYPREP";
             }
             else MYPREP_HOME=res;
          }    
        return MYPREP_HOME;
    }
   
    /** retourne la valeur d'une variable de l'environnement
     * @return AT_HOMEthe MYC
     */
    public static String getENV(String envName) {
        return env.get(envName);
    }

   /** permet de forcer le home de myCAT
     * @param aMYCAT_HOME the aMYCAT_HOME to set
     */
    public static void setMYCAT_HOME(String aMYCAT_HOME) {
        MYCAT_HOME = aMYCAT_HOME;
    }
    
    
}

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

package org.olanto.idxvli.doc;

import java.util.*;
import org.olanto.idxvli.util.*;
import static org.olanto.util.Messages.*;
import static org.olanto.idxvli.IdxEnum.*;
import static org.olanto.idxvli.IdxConstant.*;

/**
 * Permet de gérer des propriétés associés à des objets.
 * <p>
 * Par exemple, les propriétés de langue ou de collection associées à des documents
 *
 *
 *  <pre>
 *  concurrence 
 *  get est // pour les lecteurs (pas de pbr?)
 *  les autres doivent être protégé par un écrivain (externe)
 *  </pre>
 * 
 * - modification JG : bug on implémentation autre que FAST pour le dictionnaire de noms
 */
public class Properties1 implements PropertiesManager {

    private StringRepository propertieName;
    private BitArrayVector propertiesRef;
    private readWriteMode RW = readWriteMode.rw;

    /** créer une nouvelle instance de repository pour effectuer les create, open*/
    public Properties1() {
    }

    /**  crée une word table (la taille et la longueur) à l'endroit indiqué par le path
     * @param _ManagerImplementation
     * @param _idxName */
    public final PropertiesManager create(implementationMode _ManagerImplementation,
            String _path, String _idxName, int _maxPropertie, int _lengthString, int _maxDoc) {
        return (new Properties1(_ManagerImplementation,
                _path, _idxName, _maxPropertie, _lengthString, _maxDoc));
    }

    /**  ouvre un gestionnaire de mots  à l'endroit indiqué par le _path
     * @param _ManagerImplementation
     * @param _idxName */
    public final PropertiesManager open(implementationMode _ManagerImplementation,
            String _path, String _idxName, readWriteMode _RW) {
        return (new Properties1(_ManagerImplementation, _path, _idxName, _RW));
    }

    /**  ferme un gestionnaire de mots  (et sauve les modifications*/
    public final void close() {
        propertieName.close();
        propertiesRef.close();
        msg("--- Properties Manager is closed now ");
    }

    /** créer une nouvelle instance de PropertiesManager à partir des données existantes*/
    private Properties1(implementationMode _ManagerImplementation,
            String _pathName, String _idxName, readWriteMode _RW) {  // recharge un gestionnaire
        RW = _RW;
        switch (_ManagerImplementation) {
            case DIRECT:
            case FAST:
                propertieName = (new StringTable_HomeHash_InMemory()).open(_pathName, _idxName + "_name");
                propertiesRef = (new BitArrayVector_ZIP_WithCache()).open(_ManagerImplementation, _pathName, _idxName + "_bit", RW);
                break;
            case BIG:
                propertieName = (new StringTable_HomeHash_InMemory()).open(_pathName, _idxName + "_name");
                // attention le facteur HOPE_COMPRESSION doit être bien adapté
                propertiesRef = (new BitArrayVector_ZIP_WithCache()).open(_ManagerImplementation, _pathName, _idxName + "_bit", RW);
                break;
            case XL:
            case XXL:
               propertieName = (new StringTable_HomeHash_InMemory()).open(_pathName, _idxName + "_name");
               // attention le facteur HOPE_COMPRESSION doit être bien adapté
                propertiesRef = (new BitArrayVector_ZIP_WithCache()).open(_ManagerImplementation, _pathName, _idxName + "_bit", RW);
                break;
        }
    }

    /** créer une nouvelle instance de Document Manager*/
    private Properties1(implementationMode _ManagerImplementation, String _pathName, String _idxName, int _maxPropertie, int _lengthString, int _maxDoc) {
        switch (_ManagerImplementation) {
            case DIRECT:
            case FAST:
                propertieName = (new StringTable_HomeHash_InMemory()).create(_pathName, _idxName + "_name", _maxPropertie + 1, -1);  // on double pour les collisions
                propertiesRef = (new BitArrayVector_ZIP_WithCache()).create(_ManagerImplementation, _pathName, _idxName + "_bit", _maxPropertie, (int) Math.pow(2, _maxDoc));
                break;
            case BIG:
                 propertieName = (new StringTable_HomeHash_InMemory()).create(_pathName, _idxName + "_name", _maxPropertie + 1, -1);  // on double pour les collisions
               // attention le facteur HOPE_COMPRESSION doit être bien adapté
                propertiesRef = (new BitArrayVector_ZIP_WithCache()).create(_ManagerImplementation, _pathName, _idxName + "_bit", _maxPropertie, (int) Math.pow(2, _maxDoc));
                break;
            case XL:
            case XXL:
                 propertieName = (new StringTable_HomeHash_InMemory()).create(_pathName, _idxName + "_name", _maxPropertie + 1, -1);  // on double pour les collisions
               // attention le facteur HOPE_COMPRESSION doit être bien adapté
                propertiesRef = (new BitArrayVector_ZIP_WithCache()).create(_ManagerImplementation, _pathName, _idxName + "_bit", _maxPropertie, (int) Math.pow(2, _maxDoc));
                break;
        }
    }

    public List<String> getDictionnary() {
        Vector<String> res = new Vector<String>();
        int nb = propertieName.getCount();
        for (int i = 0; i < nb; i++) {
            res.add(propertieName.get(i));
        }
        return res;
    }

    public List<String> getDictionnary(String prefix) {
        Vector<String> res = new Vector<String>();
        int nb = propertieName.getCount();
     //   msg("=======================getDictionnary:"+nb);
        for (int i = 0; i < nb; i++) {
            String property = propertieName.get(i);
            //     msg("======================= "+property);
          if (property.startsWith(prefix)) {
                 res.add(property);
            }
        }
        return res;
    }

    /**  ajoute une propriété pour un document */
    public final void put(String properties, int doc) {
        int idprop = propertieName.get(properties); // cherche l'identifiant de la propriété
        if (idprop == propertieName.EMPTY) {
            idprop = propertieName.put(properties);
        } // ajoute l'identifiant de la propriété
        //msg(properties+","+idprop+","+doc);
        propertiesRef.set(idprop, doc, true);
    }

    /**  élimine une propriété pour un document */
    public final void clear(String properties, int doc) {
        int idprop = propertieName.get(properties); // cherche l'identifiant de la propriété
        if (idprop == propertieName.EMPTY) {
            idprop = propertieName.put(properties);
        } // ajoute l'identifiant de la propriété
        //msg(properties+","+idprop+","+doc);
        propertiesRef.set(idprop, doc, false);
    }

    /**  ajoute une propriété pour tous les documents */
    public final void put(String properties, SetOfBits corpus) {
        //msg("=========================== properties  put:"+properties);
        int idprop = propertieName.get(properties); // cherche l'identifiant de la propriété
        if (idprop == propertieName.EMPTY) {
            idprop = propertieName.put(properties);
        } // ajoute l'identifiant de la propriété
        //msg(properties+","+idprop);
        propertiesRef.set(idprop, corpus);
    }

    /**  vérifie si le document possède la propriété  */
    public final boolean get(String properties, int doc) {
        int idprop = propertieName.get(properties); // cherche l'identifiant de la propriété
        if (idprop == NOT_FOUND) {
            return false;
        }
        return propertiesRef.get(idprop, doc);
    }

    /**  cherche toutes les valeurs d'une propriétés*/
    public final SetOfBits get(String properties) {
        int idprop = propertieName.get(properties); // cherche l'identifiant de la propriété
        //msg("debug properties:"+properties+" / "+idprop);
        if (idprop == NOT_FOUND) {
            return null;
        }
        return propertiesRef.get(idprop);
    }

    /**  retourne le nbr de propriétés actuellement gérées dans le gestionnaire */
    public final int getCount() {
        return propertieName.getCount();
    }

    /**  imprime des statistiques */
    public final void printStatistic() {
        msg(getStatistic());
    }

    /**  imprime des statistiques
     * @return valeur */
    public final String getStatistic() {
        return propertieName.getStatistic()
                + "\n" + propertiesRef.getStatistic();
    }
}

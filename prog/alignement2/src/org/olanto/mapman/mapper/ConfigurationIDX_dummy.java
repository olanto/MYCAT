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

package org.olanto.mapman.mapper;

import org.olanto.idxvli.IdxInit;
import static org.olanto.mapman.MapArchiveConstant.*;

/**
 * Une classe pour initialiser les constantes POUR LE PARSING UNIQUEMENT.
 * 
 * Une classe pour initialiser les constantes. Cette classe doit Ãªtre implÃ©mentÃ©e pour chaque application
 */
public class ConfigurationIDX_dummy implements IdxInit {

    /** crÃ©e l'attache de cette classe.
     */
    public ConfigurationIDX_dummy() {
    }

    /** initialisation permanante des constantes.
     * Ces constantes choisies dï¿½finitivement pour toute la durï¿½e de la vie de l'index.
     */
    public void InitPermanent() {

        WORD_MINLENGTH = 3;
        WORD_MAXLENGTH = 40;
        WORD_DEFINITION = new TokenNative();


    }

    /** initialisation des constantes de configuration (modifiable).
     * Ces constantes choisies dï¿½finitivement pour toute la durï¿½e de la vie du processus.
     */
    public void InitConfiguration() {
    }
}

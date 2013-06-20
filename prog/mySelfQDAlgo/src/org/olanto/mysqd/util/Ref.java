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
package org.olanto.mysqd.util;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/** pour traiter une référence inverse
 */
public class Ref {
    public String docref;
    public String ngram ;
    public int len;
    public int nbocc;
    
    public Ref(String docref, String ngram, int len, int nbocc){
        this.docref=docref;
       this.ngram=ngram;
       this.len=len;
       this.nbocc=nbocc;
    }

}
/** pour implémenter le tri sur % mots
 */
class RefComparator implements Comparator{
    
   public int compare(Object r1, Object r2){  
       
        long r1pct=((Ref)r1).len+10000*((Ref)r1).nbocc;  // priorité à la longueur
        long r2pct=((Ref)r2).len+10000*((Ref)r2).nbocc;
       
        if(r1pct > r2pct)
            return -1;  // pour un ordre décroissant
        else if(r1pct < r2pct)
            return 1;
        else
            return 0;    
    }
    
}

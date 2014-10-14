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
package org.olanto.mycat.tmx.common;


 public class ItemsCorrelation implements Comparable<ItemsCorrelation> { 
    public String msg,termta;
    public String[][] examples;
   public int n1,n2,n12;
   public float cor;
    public ItemsCorrelation(String _termta, int _n1,int _n2,int _n12, float _cor, String _s,String[][] _examples ) { 
        cor = _cor;
        termta = _termta;
        examples= _examples;
        msg = _s;
        n1=_n1;
        n2=_n2;
        n12=_n12;
        
    }
 
     public int compareTo(ItemsCorrelation two ) {
        // I migth compare them using the int first 
        // and if they're the same, use the string... 
       // float diff = this.cor - two.cor;
        if( this.cor == two.cor ) return 0;
        if( this.cor <= two.cor ) { // they have different int
            return 1;
        }

         return -1;
   }

}
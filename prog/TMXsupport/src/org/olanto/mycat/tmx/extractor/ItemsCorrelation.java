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
package org.olanto.mycat.tmx.extractor;

import java.util.*;

/*
class ItemsCorrelation { 
    public static void main( String ... args ) { 
        // create a bunch and sort them 
        List<CustomObject> list = Arrays.asList(
            new CustomObject(3, "Blah"),
            new CustomObject(30, "Bar"),
            new CustomObject(1, "Zzz"),
            new CustomObject(1, "Aaa")
        );
        System.out.println( "before: "+ list );
        Collections.sort( list );
        System.out.println( "after : "+ list );
    }
}
*/
 public class ItemsCorrelation implements Comparable<ItemsCorrelation> { 
    String msg;
    float cor;
    public ItemsCorrelation( float _cor, String _s ) { 
        cor = _cor;
        msg = _s;
    }
 
     public int compareTo(ItemsCorrelation two ) {
        // I migth compare them using the int first 
        // and if they're the same, use the string... 
        float diff = this.cor - two.cor;
        if( diff < 0 ) { // they have different int
            return 1;
        }

         return -1;
   }

}
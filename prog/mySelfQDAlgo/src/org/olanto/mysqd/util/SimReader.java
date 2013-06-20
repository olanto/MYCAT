/**
 * ********
 * Copyright © 2010-2012 Jacques Guyot
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
package org.olanto.mysqd.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static org.olanto.util.Messages.*;

/**
 *
 * @author simple
 */
public class SimReader {

    /**
     * simule un Reader
     *
     */
 
    String s;
    int pos=0;
    
    public SimReader(String s){
        this.s=s;
    }
    
   
    
    public  final int read() {  
        if (pos<s.length()){
            int c= s.charAt(pos);
            pos++;
            return c;
        }
        return -1;  //EOF
    }
  }

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

package org.olanto.mfl.txt2mfl;

import java.io.*;
import java.lang.reflect.*;

/** génération de mfl.
 * 
 */

public class TestTXT2MFL{
   
   static int idSeq=0;

   public static void main(String[] args)   {

     String FN="C:/JG/uit/doc";
     ExportMFL.init("C:/JG/uit/uit");
     indexdir(FN);
     ExportMFL.close();

   }

  public static void indexdir(String path){
     File f=new File(path);
     if (f.isFile()){
       if (path.endsWith(".txt")) indexdoc(path);
     }
     else {
       String[] lf=f.list();
       int ilf=Array.getLength(lf);
       for (int i=0; i<ilf; i++)
          indexdir(path+"/"+lf[i]);
     }
   }

  public static void indexdoc(String f)   {
     try {
          System.out.println("file:"+f+" from idseq:"+idSeq);
           LoadTXT LF=new LoadTXT(f);

     String txt=LF.getNext();
     
     while (!txt.equals("")){

        //System.out.println(idSeq);
        ExportMFL.addFile(f.substring(14,f.length()),txt);
        idSeq++;   
        txt=LF.getNext();
       }
    } //try
	catch (Exception e) {System.err.println("IO error");
	                     System.out.println(e.toString());}
  }


}



	


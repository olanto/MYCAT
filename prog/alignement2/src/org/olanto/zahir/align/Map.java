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

package org.olanto.zahir.align;

import static org.olanto.util.Messages.*;

/**
 * Classe stockant la carte des positions entre deux traductions.
 * 
 */
public class Map {

    /* les maps  en sentence*/
    public int[] fromMap;
    public int[] toMap;
    public float[] fromSimil;
    public float[] toSimil;
    public int[] fromShift, fromCharLength, fromNbWords, fromIncrement;
    public int[] fromtoCharLength, fromtoNbWords, fromMapping;
    public int fromCountOne;
    public boolean[] fromCertainMap;
    public boolean[] fromFullMap;
    public int[] start;
    public int[]  end;
    public String[]  mapRules;
    public int[] toShift, toCharLength, toNbWords, toIncrement;
    public int[] tofromCharLength, tofromNbWords, toMapping;
    public int toCountOne;
    public int fullcount;

    public Map(int fromSize, int toSize) {
        fromMap = new int[fromSize];
        toMap = new int[toSize];
        fromSimil = new float[fromSize];
        toSimil = new float[toSize];
        fromShift = new int[fromSize];
        fromCharLength = new int[fromSize];
        fromNbWords = new int[fromSize];
        fromIncrement = new int[fromSize];
        fromtoCharLength = new int[fromSize];
        fromtoNbWords = new int[fromSize];
        fromMapping = new int[fromSize];
        fromCertainMap = new boolean[fromSize];

        toShift = new int[toSize];
        toCharLength = new int[toSize];
        toNbWords = new int[toSize];
        toIncrement = new int[toSize];
        tofromCharLength = new int[toSize];
        tofromNbWords = new int[toSize];
        toMapping = new int[toSize];
    }

    public final void addFromPos(int src, int target, float score, int srccharlength, int srcnbwords, int targcharlength, int targnbwords) {
        fromMap[src] = target;
        fromSimil[src] = score;
        fromCharLength[src] = srccharlength;
        fromNbWords[src] = srcnbwords;
        fromtoCharLength[src] = targcharlength;
        fromtoNbWords[src] = targnbwords;

    }

    public final void addToPos(int src, int target, float score, int srccharlength, int srcnbwords, int targcharlength, int targnbwords) {
        toMap[src] = target;
        toSimil[src] = score;
        toCharLength[src] = srccharlength;
        toNbWords[src] = srcnbwords;
        tofromCharLength[src] = targcharlength;
        tofromNbWords[src] = targnbwords;
    }

    
     public final void computeFullMap() {
         fromFullMap= fromCertainMap.clone();
         start= new int[fromFullMap.length];
         end= new int[fromFullMap.length];
         mapRules= new String[fromFullMap.length];
         for (int i = 0; i < fromFullMap.length; i++) { // mark certain mapping
            if (fromCertainMap[i]){
                start[i]=1;
                end[i]=1;
                mapRules[i]="A";
            }
         }
         
//0 --> 1 ;-1 ;2 ;false ;false ;0 ;0 ;null ;8 ;1 ;5 ;1 ; sim:0.51732284 ;0
 if (!fromCertainMap[0]){
               fromFullMap[0]=true; start[0]=1;end[0]=1;mapRules[0]="INIT";
               fromMap[0]=0;
               fromIncrement[0]=1;
               fromShift[0]=0;
               fromCertainMap[0]=true;
            }        
 
//26865 --> 27019 ;-154 ;3 ;false ;false ;0 ;0 ;null ;31 ;3 ;34 ;4 ; sim:0.19079158 ;-152

 if (!fromCertainMap[fromFullMap.length-1]){
               fromFullMap[fromFullMap.length-1]=true; start[fromFullMap.length-1]=1;end[fromFullMap.length-1]=1;mapRules[fromFullMap.length-1]="LAST";
               fromIncrement[fromFullMap.length-1]=1;
               fromMap[fromFullMap.length-1]=toMap.length-1;
               fromShift[fromFullMap.length-1]=(fromFullMap.length-1)-fromMap[fromFullMap.length-1];
               fromCertainMap[fromFullMap.length-1]=true;
            }
//11 --> 11 ;0 ;1 ;true ;true ;1 ;1 ;90 ;8 ;100 ;10 ; sim:0.90025395 ;0
//12 --> 12 ;0 ;1 ;false ;false ;0 ;0 ;10 ;1 ;10 ;1 ; sim:1.8540483 ;0
//13 --> 13 ;0 ;1 ;false ;false ;0 ;0 ;16 ;2 ;23 ;3 ; sim:1.1008976 ;0
//14 --> 14 ;0 ;1 ;true ;true ;1 ;1 ;225 ;19 ;238 ;25 ; sim:1.0355163 ;0

          for (int i = 1; i < fromFullMap.length-2; i++) { // mark 2 false no step
            if (fromCertainMap[i-1]&&!fromCertainMap[i]&&!fromCertainMap[i+1]&&fromCertainMap[i+2]&&
                   fromIncrement[i-1]==1&&fromIncrement[i]==1&&fromIncrement[i+1]==1&&fromIncrement[i+2]==1
                    ){
               fromFullMap[i]=true; start[i]=1;end[i]=1;mapRules[i]="F=";
               fromFullMap[i+1]=true; start[i+1]=1;end[i+1]=1;mapRules[i+1]="F=";
               
            }
         }
   
           
        
         
          
          
//86 --> 86 ;0 ;1 ;true ;true ;1 ;1 ;A ;10 ;1 ;10 ;1 ; sim:1.8540483 ;0
//87 --> 86 ;1 ;0 ;false ;false ;0 ;0 ;null ;15 ;2 ;10 ;1 ; sim:0.5 ;0
//88 --> 88 ;0 ;2 ;false ;false ;0 ;0 ;null ;227 ;18 ;269 ;26 ; sim:0.8589326 ;0
//89 --> 89 ;0 ;1 ;true ;true ;1 ;1 ;A ;110 ;9 ;111 ;12 ; sim:1.0183588 ;0
         
   for (int i = 1; i < fromFullMap.length-2; i++) { // mark 2 false no step
            if (fromCertainMap[i-1]&&!fromCertainMap[i]&&!fromCertainMap[i+1]&&fromCertainMap[i+2]&&
                   fromIncrement[i-1]==1&&fromIncrement[i+2]==1&&
                   fromShift[i-1]==fromShift[i+2]
                    
                    ){
               fromFullMap[i]=true; start[i]=1;end[i]=1;mapRules[i]="F!";fromMap[i]=i-fromShift[i-1];
               fromFullMap[i+1]=true; start[i+1]=1;end[i+1]=1;mapRules[i+1]="F!";fromMap[i+1]=i+1-fromShift[i-1];
               
            }       
   }
  
//595 --> 595 ;0 ;1 ;true ;true ;1 ;1 ;A ;271 ;27 ;271 ;27 ; sim:2.015802 ;0
//596 --> 596 ;0 ;1 ;true ;true ;1 ;1 ;A ;26 ;3 ;22 ;3 ; sim:1.3256879 ;0
//597 --> 598 ;-1 ;2 ;false ;false ;0 ;0 ;null ;75 ;9 ;75 ;9 ; sim:1.9659495 ;0
//598 --> 599 ;-1 ;1 ;true ;true ;1 ;1 ;A ;60 ;5 ;60 ;5 ; sim:1.8806815 ;-1
//599 --> 600 ;-1 ;1 ;true ;true ;1 ;1 ;A ;74 ;7 ;74 ;7 ; sim:1.8645309 ;-1
 
    for (int i = 1; i < fromFullMap.length-1; i++) { // mark 1 false  step
            if (fromCertainMap[i-1]&&!fromCertainMap[i]&&fromCertainMap[i+1]&&
                   fromIncrement[i-1]==1&&fromIncrement[i+1]==1&&
                   fromShift[i-1]-fromShift[i+1]==1
                    
                    ){
               fromFullMap[i]=true; start[i]=1;end[i]=2;mapRules[i]="S12";fromMap[i]=i-fromShift[i-1];
              
            }       
   }
  
//107 --> 109 ;-2 ;1 ;true ;true ;1 ;1 ;A ;124 ;11 ;234 ;22 ; sim:0.75491136 ;-2
//108 --> 109 ;-1 ;0 ;false ;false ;0 ;0 ;null ;110 ;7 ;234 ;22 ; sim:0.6398244 ;-2
//109 --> 110 ;-1 ;1 ;false ;false ;0 ;0 ;null ;246 ;18 ;204 ;18 ; sim:0.94326985 ;-1
//110 --> 111 ;-1 ;1 ;true ;true ;1 ;1 ;A ;86 ;5 ;76 ;7 ; sim:0.7707921 ;-1
  
     for (int i = 1; i < fromFullMap.length-2; i++) { // mark 2 false  step
            if (fromCertainMap[i-1]&&!fromCertainMap[i]&&!fromCertainMap[i+1]&&fromCertainMap[i+2]&&
                   fromIncrement[i-1]==1&&fromIncrement[i+2]==1&&
                   fromShift[i-1]-fromShift[i+2]==-1
                    
                    ){
               fromFullMap[i]=true; start[i]=2;end[i]=1;mapRules[i]="S21";fromMap[i]=i-fromShift[i-1];
               fromFullMap[i+1]=true; start[i+1]=0;end[i+1]=0;mapRules[i+1]="S21";fromMap[i+1]=-1;
               
            }       
   }
    
//11 --> 11 ;0 ;1 ;true ;true ;1 ;1 ;A ;78 ;6 ;79 ;7 ; sim:0.66356176 ;0
//12 --> 13 ;-1 ;2 ;false ;false ;0 ;0 ;null ;69 ;7 ;54 ;4 ; sim:0.40460518 ;0
//13 --> 13 ;0 ;0 ;false ;false ;0 ;0 ;null ;31 ;3 ;54 ;4 ; sim:0.3335819 ;0
//14 --> 14 ;0 ;1 ;false ;false ;0 ;0 ;null ;21 ;2 ;21 ;2 ; sim:0.80736387 ;0
//15 --> 15 ;0 ;1 ;true ;true ;1 ;1 ;A ;97 ;6 ;98 ;9 ; sim:0.58246106 ;0
 
          for (int i = 0; i < fromMap.length-4; i++) {
              if (fromFullMap[i]&&!fromFullMap[i+1]){  // start false
                  for (int j = i+2; j < fromMap.length; j++) {
                     if (fromFullMap[j]&&!fromFullMap[j-1]){ // end of false
                         if (fromShift[i]==fromShift[j]){
                             System.out.println("take a chance from:"+(i+1)+" to "+(j-1));
                             for (int k = i+1; k < j; k++) {
                                 fromFullMap[k]=true; start[k]=1;end[k]=1;mapRules[k]="TAC";fromMap[k]=k-fromShift[i];
                             }
                         } else{
                             System.out.println("n-m :"+(i+1)+" to "+(j-1));
                                fromFullMap[i+1]=true; start[i+1]=j-i-1;end[i+1]=j-i-1+(fromShift[i]-fromShift[j]);mapRules[i+1]="BLK";fromMap[i+1]=i+1-fromShift[i];
                              for (int k = i+2; k < j; k++) {
                                 fromFullMap[k]=true;mapRules[k]="BLK";fromMap[k]=-1;
                             }
                       }
                      i=j-1; // restart before
                      break;
                     }  
                  }                
              }
              
          }
   
     
      // count
      fullcount=0;
        for (int i = 0; i < fromMap.length; i++) {
            if (fromFullMap[i])fullcount++;
        }
     }
     
    public final void compute() {

        //FROM
        for (int i = 0; i < fromMap.length; i++) {// shift entre les deux phrases
            fromShift[i] = i - fromMap[i];
        }
        fromIncrement[0] = fromMap[0] + 1; // incrï¿½ment dans la cible pour chaque ligne (attendu =1)
        for (int i = 1; i < fromMap.length; i++) {
            fromIncrement[i] = fromMap[i] - fromMap[i - 1]; // shift entre les deux phrases
        }
        fromCountOne = 0;
        for (int i = 0; i < fromMap.length; i++) {// shift entre les deux phrases
            if (fromIncrement[i] == 1) {
                fromCountOne++;
            }
        }
        //TO
        for (int i = 0; i < toMap.length; i++) {// shift entre les deux phrases
            toShift[i] = i - toMap[i];
        }
        toIncrement[0] = toMap[0] + 1; // incrï¿½ment dans la cible pour chaque ligne (attendu =1)
        for (int i = 1; i < toMap.length; i++) {
            toIncrement[i] = toMap[i] - toMap[i - 1]; // shift entre les deux phrases
        }
        toCountOne = 0;
        for (int i = 0; i < toMap.length; i++) {// shift entre les deux phrases
            if (toIncrement[i] == 1) {
                toCountOne++;
            }
        }
        // compute mapping
        int lastmapping = 0;
        for (int i = 0; i < fromMap.length; i++) {// approxime le mapping
            if (fromIncrement[i] == 1) {
                fromMapping[i] = fromShift[i];
                lastmapping = fromShift[i];
            } else {
                fromMapping[i] = lastmapping;
            }
        }
        lastmapping = 0;
        for (int i = 0; i < toMap.length; i++) {// approxime le mapping
            if (toIncrement[i] == 1) {
                toMapping[i] = toShift[i];
                lastmapping = toShift[i];
            } else {
                toMapping[i] = lastmapping;
            }
        }
        // compute sure mapping
        for (int i = 0; i < fromMap.length; i++) {// approxime le mapping
            if (fromIncrement[i] == 1) {
                if (toIncrement[fromMap[i]] == 1) {
                    if (i == toMap[fromMap[i]]) {
                        fromCertainMap[i] = true;
                    }
                }
            }
        }

           // compute sure mapping
      
        
    }
    
    

    public void dump() {
        msg("map from -> to "+fullcount+"/"+fromMap.length);
        for (int i = 0; i < fromMap.length; i++) {
            msg(i + " --> " + fromMap[i] + " ;" + fromShift[i]
                    + " ;" + fromIncrement[i]
                    + " ;" + fromCertainMap[i]
                    + " ;" + fromFullMap[i]
                    + " ;" + start[i]
                    + " ;" + end[i]
                    + " ;" + mapRules[i]
                    + " ;" + fromCharLength[i] + " ;" + fromNbWords[i]
                    + " ;" + fromtoCharLength[i] + " ;" + fromtoNbWords[i]
                    + " ; sim:" + fromSimil[i]
                    + " ;" + fromMapping[i]);
        }
        msg("fromCountOne:" + fromCountOne);
        msg("----------------------------------------------------------------------");
        msg("map to -> from");
        for (int i = 0; i < toMap.length; i++) {
            msg(i + " --> " + toMap[i] + " ;" + toShift[i]
                    + " ;" + toIncrement[i]
                    + " ;" + toCharLength[i] + " ;" + toNbWords[i]
                    + " ;" + tofromCharLength[i] + " ;" + tofromNbWords[i]
                    + " ; sim:" + toSimil[i]
                    + " ;" + toMapping[i]);
        }
        msg("toCountOne:" + toCountOne);
    }
//    public final double average(){
//        double sum=0;
//          for (int i=0;i<size;i++){
//            sum+=adjust[i];
//        }
//      return sum/(double)size;
//    }
//
//    public final int median(){
//       int[] copy=new int[size];
//        System.arraycopy(adjust, 0, copy, 0, size);
//        sort(copy);
//        return copy[size/2];
//    }
//
//    public final double ecartmoy(){
//            double avg=average();
//        double sum=0;
//          for (int i=0;i<size;i++){
//            sum+= Math.sqrt((adjust[i]-avg)*(adjust[i]-avg));
//        }
//      return sum/(double)size;
//    }
//
//
//
//
//
}

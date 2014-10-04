package org.olanto.mycat.tmx.dgt2014.extractor;


 public class ItemsCorrelation implements Comparable<ItemsCorrelation> { 
    String msg,termta;
    String[][] examples;
    int n1,n2,n12;
    float cor;
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
        float diff = this.cor - two.cor;
        if( diff < 0 ) { // they have different int
            return 1;
        }

         return -1;
   }

}
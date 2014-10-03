package org.olanto.mycat.tmx.dgt2014.extractor;

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
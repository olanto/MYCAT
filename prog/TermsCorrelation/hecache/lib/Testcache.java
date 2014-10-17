/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 *
 * @author x
 */
public class Testcache {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        CacheManager manager = CacheManager.newInstance("C:/Users/x/Desktop/new-article/ehcache/ehcache-2.9.0/ehcache.xml");
        Cache cache = manager.getCache("sampleCache3"); 
System.out.println("elementsInMemory ="+ cache.getSize());
System.out.println("getMemoryStoreSize ="+ cache.getMemoryStoreSize());
System.out.println("getDiskStoreSize ="+ cache.getDiskStoreSize());

for (int i=0;i<10000;i++){
    Element element = new Element("key"+i, "value"+i);
 
cache.put(element);
  }    

System.out.println("elementsInMemory ="+ cache.getSize());
System.out.println("getMemoryStoreSize ="+ cache.getMemoryStoreSize());
System.out.println("getDiskStoreSize ="+ cache.getDiskStoreSize());


    }
}
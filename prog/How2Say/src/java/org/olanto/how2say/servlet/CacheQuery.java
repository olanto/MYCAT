package org.olanto.how2say.servlet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.olanto.senseos.SenseOS;

/**
 *
 * @author x
 */
public class CacheQuery {

    static CacheManager manager;
    static Cache cache;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        manager = CacheManager.newInstance(SenseOS.getMYCAT_HOME("HOW2SAY") + "/config/ehcache.xml");
        cache = manager.getCache("how2sayCache");
        info();

        for (int i = 0; i < 100000; i++) {
            Element element = new Element("key" + i, "value" + i);

            cache.put(element);
        }

        Element x=cache.get("key9500");
         System.out.println((String)x.getObjectValue());
//       x=cache.get("key123123123");
//         System.out.println((String)x.getObjectValue());
        info();
     }

    public static void init(String fileName, String cacheName) {
        System.out.println("Open Cache:" + fileName);
        manager = CacheManager.newInstance(fileName);
        cache = manager.getCache("how2sayCache");
    }
    
      public static String get(String key) {
          Element x=cache.get(key);
          if (x==null)return null;
          return (String)x.getObjectValue();     
    }
        public static void put(String key, String value) {
           Element element = new Element(key, value);
            cache.put(element);  
    }

    public static void info() {
                System.out.println("HeapSize =" + cache.getStatistics().getLocalHeapSize()
               +", HeapSizeInBytes =" + cache.getStatistics().getLocalHeapSizeInBytes()
               +", DiskSize =" + cache.getStatistics().getLocalDiskSize()
               +", DiskSizeInBytes =" + cache.getStatistics().getLocalDiskSizeInBytes());
    }  
}
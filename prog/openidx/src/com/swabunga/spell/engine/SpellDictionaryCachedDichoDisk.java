/*
Jazzy - a Java library for Spell Checking
Copyright (C) 2001 Mindaugas Idzelis
Full text of license can be found in LICENSE.txt

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package com.swabunga.spell.engine;

import java.io.*;
import java.util.*;

/**
 * Yet another <code>SpellDictionary</code> this one is based on Damien Guillaume's
 * Diskbased dictionary but adds a cache to try to improve abit on performance.
 *
 * @author Robert Gustavsson
 * @version 0.01
 */

public class SpellDictionaryCachedDichoDisk extends SpellDictionaryDichoDisk {
    
    // Only used for testing to measure the effectiveness of the cache.

    /**
     *
     */
    static public int hits=0;

    /**
     *
     */
    static public int codes=0;

    /**
     *
     */
    public static final String JAZZY_DIR=".jazzy";

    /**
     *
     */
    public static final String PRE_CACHE_FILE_EXT=".pre";

    private static int  MAX_CACHED=10000;

    private HashMap     suggestionCache=new HashMap(MAX_CACHED);
    private String      preCacheFileName;
    private String      preCacheDir;

    /**
     * Dictionary Convienence Constructor.
     * @param wordList
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public SpellDictionaryCachedDichoDisk(File wordList)
                                     throws FileNotFoundException, IOException {
        super((File) wordList);
        loadPreCache(wordList);
    }
    
    /**
     * Dictionary Convienence Constructor.
     * @param wordList
     * @param encoding
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public SpellDictionaryCachedDichoDisk(File wordList, String encoding)
                                     throws FileNotFoundException, IOException {
        super(wordList, encoding);
        loadPreCache(wordList);
    }

    /**
     * Dictionary constructor that uses an aspell phonetic file to
     * build the transformation table.
     * @param wordList
     * @param phonetic
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */

    public SpellDictionaryCachedDichoDisk(File wordList, File phonetic)
                                     throws FileNotFoundException, IOException {
        super(wordList, phonetic);
        loadPreCache(wordList);
    }

    /**
     * Dictionary constructor that uses an aspell phonetic file to
     * build the transformation table.
     * @param wordList
     * @param phonetic
     * @param encoding
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public SpellDictionaryCachedDichoDisk(File wordList, File phonetic, String encoding)
                                     throws FileNotFoundException, IOException {
        super(wordList, phonetic, encoding);
        loadPreCache(wordList);
    }

    /**
     * Add a word permanantly to the dictionary (and the dictionary file).
     * <i>not implemented !</i>
     */
    public void addWord(String word) {
        System.err.println("error: addWord is not implemented for SpellDictionaryCachedDichoDisk");
    }

    /**
     * Clears the cache.
     */
    public void clearCache(){
        suggestionCache.clear();
    }

    /**
     * Returns a list of strings (words) for the code.
     * @return 
     */
    public List getWords(String code) {
        List list;
        codes++;
        if(suggestionCache.containsKey(code)){
            hits++;
            list=getCachedList(code);
            return list;
        }
        list=super.getWords(code);
        addToCache(code,list);
        
        return list;
    }
    /**
     * This method returns the cached suggestionlist and also moves the code to
     * the top of the codeRefQueue to indicate this code has resentlly been
     * referenced.
     */
    private List getCachedList(String code){
        CacheObject obj=(CacheObject)suggestionCache.get(code);
        obj.setRefTime();
        return obj.getSuggestionList();
    }

    /**
     * Adds a code and it's suggestion list to the cache.
     */
    private void addToCache(String code, List l){
        String      c=null;
        String      lowestCode=null;
        long        lowestTime=Long.MAX_VALUE;
        Iterator    it;
        CacheObject obj;

        if(suggestionCache.size()>=MAX_CACHED){
            it=suggestionCache.keySet().iterator();
            while(it.hasNext()){
                c=(String)it.next();
                obj=(CacheObject)suggestionCache.get(c);
                if(obj.getRefTime()==0){
                    lowestCode=c;
                    break;
                }
                if(lowestTime>obj.getRefTime()){
                    lowestCode=c;
                    lowestTime=obj.getRefTime();
                }
            }
            suggestionCache.remove(lowestCode);
        }        
        suggestionCache.put(code,new CacheObject(l));
    }

    /**
     * Load the cache from file. The cach file has the same name as the 
     * dico file with the .pre extension added.
     */
    private void loadPreCache(File dicoFile)throws IOException{
        String              code;
        List                suggestions;
        long                size,
                            time;
        File                preFile;
        ObjectInputStream   in;

        preCacheDir=System.getProperty("user.home")+"/"+JAZZY_DIR;
        preCacheFileName=preCacheDir+"/"+dicoFile.getName()+PRE_CACHE_FILE_EXT;
        //System.out.println(preCacheFileName);
        preFile=new File(preCacheFileName);
        if(!preFile.exists()){
            System.err.println("No precache file");
            return;
        }
        //System.out.println("Precaching...");
        in=new ObjectInputStream(new FileInputStream(preFile));
        try{
            size=in.readLong();
            for(int i=0;i<size;i++){
                code=(String)in.readObject();
                time=in.readLong();
                suggestions=(List)in.readObject();
                suggestionCache.put(code,new CacheObject(suggestions,time));
            }
        }catch(ClassNotFoundException ex){
            System.out.println(ex.getMessage());
        }
        in.close();
    }

    /**
     * Saves the current cache to file.
     * @throws java.io.IOException
     */
    public void saveCache() throws IOException{
        String              code;
        CacheObject         obj;
        File                preFile,
                            preDir;
        ObjectOutputStream  out;
        Iterator            it;

        if(preCacheFileName==null || preCacheDir==null){
            System.err.println("Precache filename has not been set.");
            return;
        }
        //System.out.println("Saving cache to precache file...");
        preDir=new File(preCacheDir);
        if(!preDir.exists())
            preDir.mkdir();
        preFile=new File(preCacheFileName);
        out=new ObjectOutputStream(new FileOutputStream(preFile));
        it=suggestionCache.keySet().iterator();
        out.writeLong(suggestionCache.size());
        while(it.hasNext()){
            code=(String)it.next();
            obj=(CacheObject)suggestionCache.get(code);
            out.writeObject(code);
            out.writeLong(obj.getRefTime());
            out.writeObject(obj.getSuggestionList());
        }
        out.close();
    }

    // INNER CLASSES
    // ------------------------------------------------------------------------
    private class CacheObject implements Serializable{
    
        private List    suggestions=null;
        private long    refTime=0;

        public CacheObject(List list){
            this.suggestions=list;
        }

        public CacheObject(List list, long time){
            this.suggestions=list;
            this.refTime=time;
        }
        
        public List getSuggestionList(){
            return suggestions;
        }

        public void setRefTime(){
            refTime=System.currentTimeMillis();
        }

        public long getRefTime(){
            return refTime;
        }
    }
}

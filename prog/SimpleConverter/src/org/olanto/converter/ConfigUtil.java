/*
 * 
 */
package org.olanto.converter;

import java.io.File;
import org.olanto.converter.plugin.ConverterPlugin;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 */
public class ConfigUtil {
    private final static Logger _logger = Logger.getLogger(ConfigUtil.class);
  
    private static String configFile = "default-config.xml";
    
    private static String sourcePath;
    private static String targetFormat;
    private static String badPath;
    private static String docPath;
    private static String tempPath;    
    private static Integer maxRetry;
    private static Boolean keepExtension;            
    private static String outputEncoding;            
    
    private static HashMap<String, ConverterPlugin> plugins = new HashMap<String, ConverterPlugin>();
    private static HashMap<String, Boolean> useBuiltin = new HashMap<String, Boolean>();    
    private static HashMap<String, ArrayList<ConverterPlugin>> mapping = new HashMap<String, ArrayList<ConverterPlugin>>();
    
    public static void loadConfigFromXml() throws ParserConfigurationException, SAXException, IOException, URISyntaxException{
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	Document doc = dbFactory.newDocumentBuilder().parse(new File(configFile));
	//optional, but recommended
	doc.getDocumentElement().normalize();
 
	Node node = doc.getElementsByTagName("targetFormat").item(0);
        targetFormat=node.getTextContent();
	node = doc.getElementsByTagName("sourcePath").item(0);
        sourcePath=node.getTextContent();
        node = doc.getElementsByTagName("badPath").item(0);
        badPath=node.getTextContent();
	node = doc.getElementsByTagName("docPath").item(0);
        docPath=node.getTextContent();
	node = doc.getElementsByTagName("tempPath").item(0);
        tempPath=node.getTextContent();
	node = doc.getElementsByTagName("maxRetry").item(0);
        maxRetry=Integer.parseInt(node.getTextContent());
	node = doc.getElementsByTagName("keepExtension").item(0);
        keepExtension=Boolean.valueOf(node.getTextContent());
	node = doc.getElementsByTagName("outputEncoding").item(0);
        outputEncoding=node.getTextContent();
        
        loadPlugins(doc);
        loadMaps(doc);
        
        validateConfiguration();
    }
    
    private static void loadPlugins(Document doc){
        NodeList nList = doc.getElementsByTagName("plugin");
        
        for (int i = 0; i < nList.getLength(); i++) {
		Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
                        String name = element.getElementsByTagName("plugName").item(0).getTextContent();
                        String command = element.getElementsByTagName("plugCommand").item(0).getTextContent();
                        String process = element.getElementsByTagName("plugProcessWindows").item(0).getTextContent();
                        ConverterPlugin plugin=new ConverterPlugin(name, command, process);
                        Map<String,String> vars=getEnvVariableMap(element.getElementsByTagName("envVar"));
                        plugin.setEnvVar(vars);
                        plugins.put(name, plugin);
                }
        }
    }
    
    private static Map<String,String> getEnvVariableMap(NodeList nodes){
        Map<String,String> map=new HashMap<String,String>();
        for (int i=0;i<nodes.getLength();i++){
            Node node=nodes.item(i);
            String name=node.getAttributes().getNamedItem("name").getTextContent();
            String value=node.getTextContent();
            map.put(name,value);
        }
        return map;
    }
    public static void loadMaps(Document doc){
        NodeList nList = doc.getElementsByTagName("ext");
        
        for (int i = 0; i < nList.getLength(); i++) {
		Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
                        String ext=element.getElementsByTagName("extName").item(0).getTextContent();
                        int len=element.getElementsByTagName("conv").getLength();
                        for (int j=0;j<len;j++) {
                            String conv=element.getElementsByTagName("conv").item(j).getTextContent();
                            ConverterPlugin plugin=plugins.get(conv);
                            if (plugin!=null) {
                                if (mapping.get(ext)==null){
                                    mapping.put(ext, new ArrayList<ConverterPlugin>());
                                }
                                mapping.get(ext).add(plugin);
                            }
                        }
                        String builtin=element.getElementsByTagName("builtin").item(0).getTextContent();
                        if (valueOfBooleanStr(builtin)) {
                            useBuiltin.put(ext, Boolean.TRUE);
                        } else {
                            useBuiltin.put(ext, Boolean.FALSE);
                        }                        
                }
        }
    }
    
    private static void validateConfiguration(){
        // check configuration ...
    }
    
    public static Boolean applyBuiltin(String ext){

        if (hasPluginForExtension(ext) && useBuiltin.get(ext)) 
            return true;
        else return false;
    }
    
    private static Boolean valueOfBooleanStr(String str){
        return (Boolean.TRUE.equals(Boolean.valueOf(str)) || str.equalsIgnoreCase("yes"));
    }
    

    public static Boolean hasPluginForExtension(String extension){
        ArrayList<ConverterPlugin> pls=mapping.get(extension);
        if (pls!=null && pls.size()>0) return true;
        else return false;
    }
    
    public static ArrayList<ConverterPlugin> getPluginsForExtension(String extension){
        return mapping.get(extension);
    }
    
    public static void setConfigFile(String filePath){
        configFile = filePath;
    }
    
    public static String getSourcePath(){
        return sourcePath;
    }
    
    public static String getDocPath(){
        return docPath;
    }
    
    public static String getBadPath(){
        return badPath;
    }
    
    public static String getTargetFormat(){
        return targetFormat;
    }
    
    public static String getTempPath(){
        return tempPath;
    }
    
    public static Integer getMaxRetry(){
        return maxRetry;
    }
    
    public static Boolean isKeepExtension(){
        return keepExtension;
    }
         
    public static String getOutputEncoding(){
        return outputEncoding;
    }
}

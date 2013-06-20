/*
 * 
 */
package org.olanto.converter.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.process.ProcessManager;
import org.artofsolving.jodconverter.process.ProcessQuery;
import org.artofsolving.jodconverter.process.PureJavaProcessManager;
import org.artofsolving.jodconverter.util.PlatformUtils;

/**
 *
 */
public class ConverterPlugin implements Callable {

    private final static Logger _logger = Logger.getLogger(ConverterPlugin.class);
    private String plugInName;
    private String pluginCommand;
    private String plugInProcess;
    private String sourceFileName;
    private String targetFileName;
    private Map<String, String> envVar;
    
    public ConverterPlugin(String plugInName, String plugInCommand, String plugInProcess) {
        this.plugInName = plugInName;
        this.pluginCommand = plugInCommand;
        this.plugInProcess = plugInProcess;
    }

    public void setEnvVar(Map<String,String> vars){
        this.envVar=vars;
    }
    
    public void setSourceFileName(String fileName) {
        this.sourceFileName = fileName;
    }

    public void setTargetFileName(String fileName) {
        this.targetFileName = fileName;
    }

    public String getSourceFileName() {
        return this.sourceFileName;
    }

    public String getTargetFileName() {
        return this.targetFileName;
    }

    public String getPlugInName() {
        return this.plugInName;
    }

    public String getPlugInCommand() {
        return this.pluginCommand;
    }

    public String getPlugInProcess() {
        return this.plugInProcess;
    }

    @Override
    public Integer call() throws Exception {
        String cmd=pluginCommand.replace("%source%", "\""+sourceFileName+"\"");
        cmd = cmd.replace("%target%", "\""+targetFileName+"\"");
        // String[] params = cmd.split(" ");
        ArrayList<String> commandes = new ArrayList<String>();
        if (PlatformUtils.isWindows()){
            commandes.add("cmd.exe");
            commandes.add("/C");
        } else {
            commandes.add("/bin/sh");
            commandes.add("-c");
        }
        commandes.addAll(Arrays.asList(cmd));
        System.out.println(commandes);
        ProcessBuilder pb=new ProcessBuilder(commandes);
        Map<String, String> env = pb.environment();
        if (envVar!=null && envVar.size()>0) {
            env.putAll(this.envVar);
        }
        Process p = pb.start();
        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
        outputGobbler.start();
        errorGobbler.start();
        int retVal = p.waitFor();
        outputGobbler.join();
        errorGobbler.join();
        System.out.println("Returned value: " + retVal);
        return p.waitFor();
    }

    public void stopProcess() {
        ProcessManager process = new PureJavaProcessManager();
        ProcessQuery query = new ProcessQuery(this.pluginCommand, this.plugInProcess);
        try {
            Long pid = process.findPid(query);
            _logger.info("Trying to kill plugin process: " + pid);
            process.kill(null, pid);
        } catch (IOException e1) {
            _logger.error(e1.getMessage());
        }
    }

    private class StreamGobbler extends Thread {

        InputStream is;
        String type;

        private StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(type + "> " + line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}

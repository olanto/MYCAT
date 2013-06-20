/*
 * 
 */
package org.olanto.converter;

import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import org.artofsolving.jodconverter.process.ProcessManager;
import org.artofsolving.jodconverter.process.ProcessQuery;
import org.artofsolving.jodconverter.process.PureJavaProcessManager;

/**
 *
 */
public class ConverterPlugin implements Callable {
    private final static Logger _logger = Logger.getLogger(ConverterPlugin.class);

    private String plugInName;
    private String plugInCommand;
    private String plugInProcess;
    
    public ConverterPlugin(String plugInName, String plugInCommand, String plugInProcess){
            this.plugInName = plugInName;
            this.plugInCommand = plugInCommand;
            this.plugInProcess = plugInProcess;
    }
    
    public String getPlugInName(){
        return this.plugInName;
    }
    
    public String getPlugInCommand(){
        return this.plugInCommand;
    }
    
    public String getPlugInProcess(){
        return this.plugInProcess;
    }
    
    @Override
    public Integer call() throws Exception {      
        Process p = new ProcessBuilder("cmd.exe", "/C",this.plugInCommand).start();
        return p.waitFor();
    }
    
    public void stopProcess() {
            ProcessManager process = new PureJavaProcessManager();
            ProcessQuery query = new ProcessQuery(this.plugInCommand, this.plugInProcess);          
             try {
                Long pid = process.findPid(query);
                _logger.info("Trying to kill plugin process: " + pid);
                process.kill(null, pid);
            } catch (IOException e1) {
                _logger.error(e1.getMessage());
            }
    }
    
}

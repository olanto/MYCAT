/*
 * 
 */
package org.olanto.converter;

import org.olanto.converter.plugin.ConverterPlugin;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;

/**
 *
 */
public class PluginConverterFactory extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(PluginConverterFactory.class);

    public static AbstractConverterFactory getInstance() {
        _logger.debug("Build new : PluginConverterFactory");
        return new PluginConverterFactory();
    }

    @Override
    public void startConvertion() {
        _logger.debug("Start converting " + source.getName());
        ArrayList<ConverterPlugin> converters = ConfigUtil.getPluginsForExtension(source.getExtention());
        try {
            for (ConverterPlugin conv : converters) {
                conv.setSourceFileName(source.getAbsolutePath());
                conv.setTargetFileName(target.getAbsolutePath());
                ExecutorService executor = Executors.newFixedThreadPool(1);
                Future<Integer> future = executor.submit(conv);
                int retValue = -1;
                try {
                    retValue = future.get(300000, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    System.out.println("No response after 30 seconds" + retValue);
                   try { 
                       conv.stopProcess();
                   
                   } catch (Exception x) {
                    System.out.println("error during stopProcess");
                   }
                   System.out.println("stopProcess ok");
                   future.cancel(true);
                    System.out.println("cancel");
               }
                executor.shutdownNow();
                if (retValue == 0) {
                    this.success = true;
                    return;
                }
            }
        } catch (Exception ex) {
            _logger.info(ex);
        }
        this.success = false;

        if (target.exists()) {
            target.delete();
        }

    }
}

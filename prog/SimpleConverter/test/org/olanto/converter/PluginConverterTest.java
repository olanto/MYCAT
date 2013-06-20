/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class PluginConverterTest {

    public PluginConverterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAntiwordPlugin() throws Exception {
        ConfigUtil.loadConfigFromXml();
        AbstractConverterFactory converterFactory = PluginConverterFactory.getInstance();
        Document inputFile = new Document("c:/MYCAT/prog/antiword/docs/testdoc.doc");
        Document outFile = new Document("c:/MYCAT/prog/antiword/docs/testdoc.txt");
        converterFactory.init(inputFile, outFile);
        converterFactory.setOutputFormat("txt");
        converterFactory.startConvertion();
        boolean success = converterFactory.isConverted();
        System.out.println("convertion : " + success);
    }

    //@Test
    public void testAntiword() {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "c:/MYCAT/prog/antiword/antiword.exe", "c:/MYCAT/prog/antiword/docs/testdoc.doc", ">>", "c:/MYCAT/prog/antiword/docs/testdoc.txt");
        Map<String, String> env = pb.environment();
        env.put("ANTIWORDHOME", "c:/MYCAT/prog/antiword");
        pb.directory(new File("c:/MYCAT/prog/antiword"));
        try {
            Process p = pb.start();
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
            outputGobbler.start();
            errorGobbler.start();
            int retVal = p.waitFor();
            outputGobbler.join();
            errorGobbler.join();
            System.out.println("Returned value: " + retVal);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
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

package xmlconvert;

/**
 *
 * @author jacqu
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import static org.olanto.senseos.SenseOS.*;

public class XmlLoaderAndConvertCheckError {

    static private boolean verbose = false; // for debugging
    static SetOfTagWithNL actionlist;
    static StringBuilder textContent;

    public static void main(String[] args) {
        String xmlFilePath = "C:\\MYCAT\\plugins\\XmlConvertion\\runpack\\xml\\ex1.xml";
        String txtFilePath = "C:\\MYCAT\\plugins\\XmlConvertion\\runpack\\xml\\ex1.xml.txt";
        String fileNL = getMYCAT_HOME()+"\\plugins\\XmlConvertion\\runpack\\tagWithNL.txt";

        actionlist = new SetOfTagWithNL(fileNL);

        if (verbose)System.out.println(args.length);
        if (args.length != 2) {
            System.out.println("usage: java XmlLoaderAndConvert2 xlmfilename txtfilename");
            System.exit(-1);
        }

        xmlFilePath = args[0];
        txtFilePath = args[1];


        try {
            // Charger le contenu du fichier XML dans un String
            String xmlContent = loadFileToString(xmlFilePath);
            if (verbose) {
                System.out.println("XML Content:\n" + xmlContent);
            }

            // Extraire le texte sans les balises
            textContent = extractTextFromXml(xmlContent);
            if (verbose) {
                System.out.println("\nExtracted Text:\n" + textContent);
            }

            // Écrire le texte extrait dans un fichier txt
            writeStringToFile(txtFilePath, textContent.toString().trim());
            if (verbose) {
                System.out.println("\nText written to file: " + txtFilePath);
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            System.out.println("Error during conversion... " + xmlFilePath);
            System.exit(-1);
        }
    }

    public static String loadFileToString(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        fis.close();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static StringBuilder extractTextFromXml(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = null;

        document = builder.parse(new java.io.ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        textContent = new StringBuilder();
        extractTextNodes(document.getDocumentElement(), textContent);

        return textContent;
    }

    private static void extractTextNodes(Node node, StringBuilder textContent) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            String text = node.getTextContent().trim();
            if (!text.isEmpty()) {
                if (textContent.length() > 0) {
                    textContent.append(" ");
                }
                textContent.append(text);
            }
        } else {
            beforeTagAction(node);
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                extractTextNodes(child, textContent);
            }
            afterTagAction(node);
        }
    }

    public static void beforeTagAction(Node node) {
        if (verbose)System.out.println("tag open:" + node.getNodeName());
        String action = actionlist.checkTagAction(node.getNodeName());
        if (action!=null&&action.equals("NL")) {
            textContent.append("\n");
        }
    }
    public static void afterTagAction(Node node) {
        if (verbose)System.out.println("tag close:" + node.getNodeName());
        String action = actionlist.checkTagAction(node.getNodeName());
        if (action!=null&&action.equals("NL")) {
            textContent.append("\n");
        }
    }

    public static void writeStringToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }
}

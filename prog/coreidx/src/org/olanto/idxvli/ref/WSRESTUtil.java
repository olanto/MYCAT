/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.idxvli.ref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.olanto.convsrv.server.ConvertService;
import org.olanto.idxvli.IdxConstant;
import org.olanto.idxvli.server.REFResultNice;
import org.olanto.idxvli.server.Reference;
import org.olanto.idxvli.server.Server_MyCat;
import org.olanto.idxvli.util.BytesAndFiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author simple
 */
public class WSRESTUtil {

    static String organisationTemplate = null;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//        byte[] bytes = null;
//        System.out.println(convertFileWithRMI("C:\\MYCAT\\corpus\\docs\\small-collection\\UNO\\A_RES_53_144_EN.pdf"));
        String mergedRefDoc = "";
        String file1 = "C:\\MYCAT\\doc2process\\A_RES_53_144_EN_1.xml";
        String file2 = "C:\\MYCAT\\doc2process\\A_RES_53_144_EN_2.xml";
        File fXmlFile = new File(file1);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        File fXmlFile1 = new File(file2);
        DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
            Document doc1 = dBuilder1.parse(fXmlFile1);
            doc1.getDocumentElement().normalize();

            // merge params
            mergedRefDoc += WSRESTUtil.mergeXMLParameters(doc, doc1);
            // merge statistics
            mergedRefDoc += WSRESTUtil.mergeXMLStatistics(doc, doc1);
            // merge HTML
            int totalRefs = doc.getElementsByTagName("reference").getLength() + doc1.getElementsByTagName("reference").getLength();
            mergedRefDoc += WSRESTUtil.mergeHTMLContentAndGenerateInfo(file1, file2, doc, doc1, "T", "J", "red", doc.getElementsByTagName("reference").getLength(), totalRefs);
            // merge details
            mergedRefDoc += "<origText>\n"
                    + doc.getDocumentElement().getElementsByTagName("origText").item(0).getTextContent()
                    + "</origText>";

            System.out.println(mergedRefDoc);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Server_MyCat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param s
     * @return
     */
    public static String unCommentRefDoc(String s) {
        s = s.replace("<!--", "</htmlstartcomment>");
        s = s.replace("-->", "</htmlendcomment>");
        return s;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String reCommentRefDoc(String s) {
        s = s.replace("</htmlstartcomment>", "<!--");
        s = s.replace("</htmlendcomment>", "-->");
        return s;
    }

    /**
     *
     * @param msg
     * @param TxtSrc
     * @param RefType
     * @param DocSrc
     * @param DocTgt
     * @param LngSrc
     * @param LngTgt
     * @param Filter
     * @param MinLen
     * @param RemFirst
     * @param Fast
     * @return
     */
    public static String niceXMLParameters(String msg, String TxtSrc, String RefType, String DocSrc, String DocTgt,
            String LngSrc, String LngTgt, String[] Filter, Integer MinLen, Boolean RemFirst, Boolean Fast) {

        String collections = "";
        if (Filter != null) {
            for (int i = 0; i < Filter.length; i++) {
                collections += Filter[i] + ";";
            }
        }
        return "<parameters>\n"
                + "<msg>" + msg + "</msg>\n"
                + "<TxtSrc>" + TxtSrc + "</TxtSrc>\n"
                + "<RefType>" + RefType + "</RefType>\n"
                + "<DocSrc>" + DocSrc + "</DocSrc>\n"
                + "<DocTgt>" + DocTgt + "</DocTgt>\n"
                + "<LngSrc>" + LngSrc + "</LngSrc>\n"
                + "<LngTgt>" + LngTgt + "</LngTgt>\n"
                + "<Filter>" + collections + "</Filter>\n"
                + "<MinLen>" + MinLen + "</MinLen>\n"
                + "<RemFirst>" + RemFirst + "</RemFirst>\n"
                + "<Fast>" + Fast + "</Fast>\n"
                + "</parameters>\n";

    }

    /**
     *
     * @param RefType
     * @param DocSrc1
     * @param DocSrc2
     * @param DocTgt
     * @param RepTag1
     * @param RepTag2
     * @param Color2
     * @return
     */
    public static String niceXMLParams(String RefType, String DocSrc1, String DocSrc2, String DocTgt, String RepTag1, String RepTag2, String Color2) {

        return "<params>\n"
                + "   <RefType>" + RefType + "</RefType>\n"
                + "   <DocSrc1>" + DocSrc1 + "</DocSrc1>\n"
                + "   <DocSrc2>" + DocSrc2 + "</DocSrc2>\n"
                + "   <DocTgt>" + DocTgt + "</DocTgt>\n"
                + "   <RepTag1>" + RepTag1 + "</RepTag1>\n"
                + "   <RepTag2>" + RepTag2 + "</RepTag2>\n"
                + "   <Color2>" + Color2 + "</Color2>\n"
                + "</params>\n";
    }

    /**
     *
     * @param RefDocFullName
     * @param RefDocType
     * @param RefDocLng
     * @param RefDocPerCent
     * @param RefDocOccurences
     * @return
     */
    public static String niceXMLInfo(String RefDocFullName, String RefDocType,
            String RefDocLng, String RefDocPerCent, String RefDocOccurences) {

        if (organisationTemplate == null) {
            organisationTemplate = BytesAndFiles.file2String(IdxConstant.IDX_XML_ORGANISATION_TEMPLATE, "UTF-8");
            if (organisationTemplate == null) {
                organisationTemplate = "<!-- Error impossible to load: "
                        + IdxConstant.IDX_XML_ORGANISATION_TEMPLATE + "-->";
            }
        }

        return "<statistics>\n"
                + "  <mycat>\n"
                + "    <RefDocFullName>" + RefDocFullName + "</RefDocFullName>\n"
                + "    <RefDocType>" + RefDocType + "</RefDocType>\n"
                + "    <RefDocLng>" + RefDocLng + "</RefDocLng>\n"
                + "    <RefDocPerCent>" + RefDocPerCent + "</RefDocPerCent>\n"
                + "    <RefDocOccurences>" + RefDocOccurences + "</RefDocOccurences>\n"
                + "  </mycat>\n"
                + "  <organisation>\n"
                + "     <!-- imported template from /config -->"
                + organisationTemplate
                + "  </organisation>\n"
                + "</statistics>\n";

    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String convertFileWithRMI(String fileName) {
        String ret = "Conversion Error";
        System.out.println("Request to convert file from WebService: " + fileName);
        try {
            Remote r = Naming.lookup("rmi://localhost/CONVSRV");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;
                // ret = is.getInformation();


                ret = is.File2Txt(fileName);

                //System.out.println("DEBUG Converted file content: " + ret);

            } else {
                return "Error: CONVSRV Service not found or not compatible.";
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return ret;
    }

    /**
     *
     * @param doc1
     * @param doc2
     * @param tgtDoc
     * @return
     */
    public static String CheckIfFilesExist(String doc1, String doc2, String tgtDoc) {
        String response = "Input Files Exist and are valid";
        File f1 = new File(doc1);
        File f2 = new File(doc2);

        if (!f1.exists() || f1.isDirectory() || !f2.exists() || f2.isDirectory()) {
            return "ERROR: One or both input files do not exist or are not valid files";
        }
        File file = new File(tgtDoc);
        if (!file.isDirectory()) {
            file = file.getParentFile();
            if (!file.exists()) {
                return "ERROR: The directory for saving the output file does not exist!";
            }
        } else {
            return "ERROR: The target file should not be a directory";
        }
        return response;
    }

    /**
     *
     * @param doc1
     * @param doc2
     * @return
     */
    public static String validateInputs(Document doc1, Document doc2) {
        String response = "Documents are Valid.\n";
        if ((doc1.getDocumentElement().getElementsByTagName("origText") == null) || (doc1.getDocumentElement().getElementsByTagName("origText").item(0) == null)
                || (doc2.getDocumentElement().getElementsByTagName("origText") == null) || (doc2.getDocumentElement().getElementsByTagName("origText").item(0) == null)) {
            return "ERROR: Missing original text in one or both documents.\n Please make sure you are merging valid compatibe documents";
        } else {
            String origText1 = doc1.getDocumentElement().getElementsByTagName("origText").item(0).getTextContent();
            String origText2 = doc2.getDocumentElement().getElementsByTagName("origText").item(0).getTextContent();
            if (origText1 == null || origText2 == null || origText1.isEmpty() || origText2.isEmpty()) {
                return "ERROR: One of the documents has an empty original text.\n Please make sure you are merging valid compatibe documents";
            } else if (!origText1.equalsIgnoreCase(origText2)) {
                return "ERROR: Original text is not the same in both documents.\n Please make sure you are merging valid compatibe documents";
            } else {
                return response;
            }
        }
    }

    /**
     *
     * @param doc1
     * @param doc2
     * @return
     */
    public static String mergeXMLParameters(Document doc1, Document doc2) {
        StringBuilder res = new StringBuilder("");
        res.append("<parameters>\n")
                .append("<msg>");
        if (doc1.getDocumentElement().getElementsByTagName("msg") != null && doc1.getDocumentElement().getElementsByTagName("msg").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("msg").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("msg") != null && doc2.getDocumentElement().getElementsByTagName("msg").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("msg").item(0).getTextContent());
            }
        }
        res.append("</msg>\n")
                .append("<TxtSrc>");
        if (doc1.getDocumentElement().getElementsByTagName("TxtSrc") != null && doc1.getDocumentElement().getElementsByTagName("TxtSrc").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("TxtSrc").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("TxtSrc") != null && doc2.getDocumentElement().getElementsByTagName("TxtSrc").item(0).getTextContent() != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("TxtSrc").item(0).getTextContent());
            }
        }
        res.append("</TxtSrc>\n")
                .append("<RefType>");
        if (doc1.getDocumentElement().getElementsByTagName("RefType") != null && doc1.getDocumentElement().getElementsByTagName("RefType").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("RefType").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("RefType") != null && doc2.getDocumentElement().getElementsByTagName("RefType").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("RefType").item(0).getTextContent());
            }
        }
        res.append("</RefType>\n")
                .append("<DocSrc>");
        if (doc1.getDocumentElement().getElementsByTagName("DocSrc") != null && doc1.getDocumentElement().getElementsByTagName("DocSrc").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("DocSrc").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("DocSrc") != null && doc2.getDocumentElement().getElementsByTagName("DocSrc").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("DocSrc").item(0).getTextContent());
            }
        }
        res.append("</DocSrc>\n")
                .append("<DocSrc>");
        if (doc1.getDocumentElement().getElementsByTagName("DocSrc") != null && doc1.getDocumentElement().getElementsByTagName("DocSrc").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("DocSrc").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("DocSrc") != null && doc2.getDocumentElement().getElementsByTagName("DocSrc").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("DocSrc").item(0).getTextContent());
            }
        }
        res.append("</DocSrc>\n")
                .append("<DocTgt>");
        if (doc1.getDocumentElement().getElementsByTagName("DocTgt") != null && doc1.getDocumentElement().getElementsByTagName("DocTgt").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("DocTgt").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("DocTgt") != null && doc2.getDocumentElement().getElementsByTagName("DocTgt").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("DocTgt").item(0).getTextContent());
            }
        }
        res.append("</DocTgt>\n")
                .append("<LngSrc>");

        if (doc1.getDocumentElement().getElementsByTagName("LngSrc") != null && doc1.getDocumentElement().getElementsByTagName("LngSrc").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("LngSrc").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("LngSrc") != null && doc2.getDocumentElement().getElementsByTagName("LngSrc").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("LngSrc").item(0).getTextContent());
            }
        }
        res.append("</LngSrc>\n")
                .append("<LngTgt>");
        if (doc1.getDocumentElement().getElementsByTagName("LngTgt") != null && doc1.getDocumentElement().getElementsByTagName("LngTgt").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("LngTgt").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("LngTgt") != null && doc2.getDocumentElement().getElementsByTagName("LngTgt").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("LngTgt").item(0).getTextContent());
            }
        }
        res.append("</LngTgt>\n")
                .append("<Filter>");

        if (doc1.getDocumentElement().getElementsByTagName("Filter") != null && doc1.getDocumentElement().getElementsByTagName("Filter").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("Filter").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("Filter") != null && doc2.getDocumentElement().getElementsByTagName("Filter").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("Filter").item(0).getTextContent());
            }
        }
        res.append("</Filter>\n")
                .append("<MinLen>");

        if (doc1.getDocumentElement().getElementsByTagName("MinLen") != null && doc1.getDocumentElement().getElementsByTagName("MinLen").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("MinLen").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("MinLen") != null && doc2.getDocumentElement().getElementsByTagName("MinLen").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("MinLen").item(0).getTextContent());
            }
        }
        res.append("</MinLen>\n")
                .append("<Fast>");

        if (doc1.getDocumentElement().getElementsByTagName("Fast") != null && doc1.getDocumentElement().getElementsByTagName("Fast").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("Fast").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("Fast") != null && doc2.getDocumentElement().getElementsByTagName("Fast").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("Fast").item(0).getTextContent());
            }
        }
        res.append("</Fast>\n")
                .append("</parameters>\n");

        return res.toString();
    }

    /**
     *
     * @param doc1
     * @param doc2
     * @return
     */
    public static String mergeXMLStatistics(Document doc1, Document doc2) {
        String organization = "";
        NodeList nodes = doc1.getDocumentElement().getElementsByTagName("organisation");
        NodeList nodes1 = doc2.getDocumentElement().getElementsByTagName("organisation");
        if (nodes != null && nodes.item(0) != null && nodes.item(0).getChildNodes() != null) {
            for (int i = 0; i < nodes.item(0).getChildNodes().getLength(); i++) {
                if (nodes.item(0).getChildNodes().item(i).getNodeName().contains("text")) {
                    organization += "";
                } else if (nodes.item(0).getChildNodes().item(i).getNodeName().contains("comment")) {
                    organization += "<!--" + nodes.item(0).getChildNodes().item(i).getTextContent() + "-->\n";
                } else {
                    organization += "<" + nodes.item(0).getChildNodes().item(i).getNodeName() + ">" + nodes.item(0).getChildNodes().item(i).getTextContent() + "</" + nodes.item(0).getChildNodes().item(i).getNodeName() + ">\n";
                }
            }
        }
        if (nodes1 != null && nodes1.item(0) != null && nodes1.item(0).getChildNodes() != null) {
            for (int i = 0; i < nodes1.item(0).getChildNodes().getLength(); i++) {
                if (nodes1.item(0).getChildNodes().item(i).getNodeName().contains("text")) {
                    organization += "";
                } else if (nodes1.item(0).getChildNodes().item(i).getNodeName().contains("comment")) {
                    organization += "<!--" + nodes1.item(0).getChildNodes().item(i).getTextContent() + "-->\n";
                } else {
                    organization += "<" + nodes1.item(0).getChildNodes().item(i).getNodeName() + ">" + nodes1.item(0).getChildNodes().item(i).getTextContent() + "</" + nodes1.item(0).getChildNodes().item(i).getNodeName() + ">\n";
                }
            }
        }
        StringBuilder res = new StringBuilder("");

        res.append("<statistics>\n")
                .append("<mycat>\n")
                .append("<RefDocFullName>");

        if (doc1.getDocumentElement().getElementsByTagName("RefDocFullName") != null && doc1.getDocumentElement().getElementsByTagName("RefDocFullName").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("RefDocFullName").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("RefDocFullName") != null && doc2.getDocumentElement().getElementsByTagName("RefDocFullName").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("RefDocFullName").item(0).getTextContent());
            }
        }
        res.append("</RefDocFullName>\n")
                .append("<RefDocType>");

        if (doc1.getDocumentElement().getElementsByTagName("RefDocType") != null && doc1.getDocumentElement().getElementsByTagName("RefDocType").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("RefDocType").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("RefDocType") != null && doc2.getDocumentElement().getElementsByTagName("RefDocType").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("RefDocType").item(0).getTextContent());
            }
        }
        res.append("</RefDocType>\n")
                .append("<RefDocLng>");

        if (doc1.getDocumentElement().getElementsByTagName("RefDocLng") != null && doc1.getDocumentElement().getElementsByTagName("RefDocLng").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("RefDocLng").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("RefDocLng") != null && doc2.getDocumentElement().getElementsByTagName("RefDocLng").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("RefDocLng").item(0).getTextContent());
            }
        }
        res.append("</RefDocLng>\n")
                .append("<RefDocPerCent>");

        if (doc1.getDocumentElement().getElementsByTagName("RefDocPerCent") != null && doc1.getDocumentElement().getElementsByTagName("RefDocPerCent").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("RefDocPerCent").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("RefDocPerCent") != null && doc2.getDocumentElement().getElementsByTagName("RefDocPerCent").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("RefDocPerCent").item(0).getTextContent());
            }
        }
        res.append("</RefDocPerCent>\n")
                .append("<RefDocOccurences>");

        if (doc1.getDocumentElement().getElementsByTagName("RefDocOccurences") != null && doc1.getDocumentElement().getElementsByTagName("RefDocOccurences").item(0) != null) {
            res.append(doc1.getDocumentElement().getElementsByTagName("RefDocOccurences").item(0).getTextContent());
            if (doc2.getDocumentElement().getElementsByTagName("RefDocOccurences") != null && doc2.getDocumentElement().getElementsByTagName("RefDocOccurences").item(0) != null) {
                res.append("|").append(doc2.getDocumentElement().getElementsByTagName("RefDocOccurences").item(0).getTextContent());
            }
        }
        res.append("</RefDocOccurences>\n")
                .append("  </mycat>\n")
                .append("  <organisation>\n")
                .append(organization)
                .append("  </organisation>\n")
                .append("</statistics>\n");
        return res.toString();
    }

    /**
     *
     * @param docSource1
     * @param docSource2
     * @param doc1
     * @param doc2
     * @param repTag1
     * @param repTag2
     * @param color2
     * @param start
     * @param totalRefs
     * @return
     */
    public static String mergeHTMLContentAndGenerateInfo(String docSource1, String docSource2, Document doc1, Document doc2, String repTag1, String repTag2, String color2, int start, int totalRefs) {
        String origText = doc1.getDocumentElement().getElementsByTagName("origText").item(0).getTextContent();
        StringBuilder res = new StringBuilder("");
        res.append("<htmlRefDoc>\n");
        res.append("<!-- <html>\n");
        res.append("<head>\n");
        res.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n");
        res.append("<title>myQuote</title>");
        res.append("</head>\n");
        res.append("<body>\n");
        List<Reference> references = new ArrayList();
        List<Reference> references1 = new ArrayList();
        if (origText != null && !origText.isEmpty()) {
            origText = origText.replace("Â", "");

            references = getReferences(doc1, origText, repTag1, "");
            addRefText(references, docSource1);

            references1 = getReferences(doc2, origText, repTag2, color2);
            addRefText(references1, docSource2);

            references.addAll(references1);
            Collections.sort(references);

            res.append("<A NAME=\"TOP\">" + "</A>" + "<A HREF=\"#STATISTIC\">")
                    .append(IdxConstant.MSG.get("server.qd.MSG_19"))
                    .append("</A><br/>");

            res.append(mergeReferences(references, origText));
            res.append("<br/><br/><A NAME=\"STATISTIC\"></A><A HREF=\"#TOP\">TOP</A><br/>");
            res.append(parseHtmlAndGetStats(docSource1));
            res.append(parseHtmlAndGetStats(docSource2));
            res.append(parseHtmlAndGetStatsTables(docSource1, repTag1, docSource2, repTag2));
            res.append(generateStatsTable(references));
            res.append(generateHTMLComments(references));
        }
        res.append("</body> </html> -->\n"
                + "</htmlRefDoc>\n");
        return res.toString()
                + getInfoForDocument(references);
    }

    /**
     *
     * @param s
     * @return
     */
    public static String clean4xml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;");
    }

    /**
     *
     * @param refs
     * @return
     */
    public static String getInfoForDocument(List<Reference> refs) {
        String references = "<references>\n";

        for (int j = 0; j < refs.size(); ++j) {
            references += "<reference>\n";

            references += "<number>" + refs.get(j).getGlobalIDX() + "</number>\n"
                    + "<id>" + refs.get(j).getLocalIDX() + "</id>\n";
            references += "<quote>" + clean4xml(refs.get(j).getTextOfRef()) + "</quote>\n";
            references += "<documents>\n";
            if (!refs.get(j).getReferencedDocs().isEmpty()) {
                for (int i = 0; i < refs.get(j).getReferencedDocs().size(); ++i) {
                    references += "<document>" + clean4xml(refs.get(j).getReferencedDocs().get(i)) + "</document>\n";
                }
                if (refs.get(j).getColor() != null && !refs.get(j).getColor().isEmpty()) {
                    references += "<color>" + refs.get(j).getColor() + "</color>\n";
                } else {
                    references += "<color>yellow</color>\n";
                }

                if (refs.get(j).getTag() != null && !refs.get(j).getTag().isEmpty()) {
                    references += "<tag>" + refs.get(j).getTag() + "</tag>\n";
                } else {
                    references += "<tag>T</tag>\n";
                }
            }
            references += "</documents>\n";
            references += "</reference>\n";
        }
        references += "</references>\n";

        return "<Info>\n"
                + references
                + "</Info>\n";
    }

    private static Integer getReferenceNumber(String refId) {
        int refNumber = 0;
        if (refId.matches("\\d+")) {
            refNumber = Integer.parseInt(refId);
        }
        return refNumber;
    }

    private static String parseHtmlAndGetStats(String docSource) {
        FileInputStream in = null;
        String stats = "<hr/>";
        try {
            in = new FileInputStream(docSource);
            String xmlContent = UtilsFiles.file2String(in, "UTF-8").replace("Â", "");
            String html = xmlContent.substring(xmlContent.indexOf("<html>"), xmlContent.indexOf("</html>"));
            if (html.contains("<body>")) {
                int statsIDX = html.indexOf("<hr/>");
                int endIDX = html.indexOf("table BORDER") - 5;
                stats += html.substring(statsIDX, endIDX);
            }
            return stats;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return stats;
    }

    private static String parseHtmlAndGetStatsTables(String docSource, String tag1, String docSource1, String tag2) {
        FileInputStream in = null, in1 = null;
        StringBuilder res = new StringBuilder();
        res.append("</p><table BORDER=\"1\">\n");
        res.append("<caption><b>")
                .append(IdxConstant.MSG.get("server.qd.MSG_9"))
                .append("</b></caption>\n");
        res.append("<tr>\n" + "<th>")
                .append(IdxConstant.MSG.get("server.qd.MSG_10"))
                .append("</br>")
                .append(IdxConstant.MSG.get("server.qd.MSG_11"))
                .append("</th>\n" + "<th>" + "%" + "</th>\n" + "<th>")
                .append(IdxConstant.MSG.get("server.qd.MSG_12"))
                .append("</th>\n"
                + "</tr>\n");
        try {
            in = new FileInputStream(docSource);
            in1 = new FileInputStream(docSource1);
            String xmlContent = UtilsFiles.file2String(in, "UTF-8").replace("Â", "");
            String xmlContent1 = UtilsFiles.file2String(in1, "UTF-8").replace("Â", "");
            String html = xmlContent.substring(xmlContent.indexOf("<html>"), xmlContent.indexOf("</html>"));
            String html1 = xmlContent1.substring(xmlContent1.indexOf("<html>"), xmlContent1.indexOf("</html>"));
            if (html.contains("<td>") && html.contains("</table>")) {
                int idx1 = html.indexOf("<td>");
                int idx2 = html.indexOf("</table>");
                if (idx2 > idx1) {
                    String stats1 = html.substring(idx1, idx2);
                    int number;
                    String regex = "<td>(.*, )+</td>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(stats1);
                    String sub = "";
                    while (matcher.find()) {
                        String[] refs = matcher.group().replace("<td>", "").replace("</td>", "").split(",");
                        for (int i = 0; i < refs.length - 1; i++) {
                            if (refs[i].replace(" ", "").matches("\\d+")) {
                                number = Integer.parseInt(refs[i].replace(" ", ""));
                                sub += tag1 + number + ", ";
                            } else {
                                sub += refs[i].replace(" ", "") + ", ";
                            }
                        }
                        stats1 = stats1.replace(matcher.group(), "<td>" + sub + "</td>");
                    }
                    res.append(stats1);
                }
            }
            if (html1.contains("<td>") && html1.contains("</table>")) {
                int idx1 = html1.indexOf("<td>");
                int idx2 = html1.indexOf("</table>");
                if (idx2 > idx1) {
                    String stats2 = html1.substring(idx1, idx2);
                    int number;
                    String regex = "<td>(.*, )+</td>";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(stats2);
                    String sub = "";
                    while (matcher.find()) {
                        String[] refs = matcher.group().replace("<td>", "").replace("</td>", "").split(",");
                        for (int i = 0; i < refs.length - 1; i++) {
                            if (refs[i].replace(" ", "").matches("\\d+")) {
                                number = Integer.parseInt(refs[i].replace(" ", ""));
                                sub += tag2 + number + ", ";
                            } else {
                                sub += refs[i].replace(" ", "") + ", ";
                            }
                        }
                        stats2 = stats2.replace(matcher.group(), "<td>" + sub + "</td>");
                    }
                    res.append("<tr>\n").append(stats2);
                }
            }
            return res.toString() + "</table>";
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
                in1.close();
            } catch (IOException ex) {
                Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "";
    }

    // this method gets all the references and locally manages their tags, colors etc.
    private static List<Reference> getReferences(Document doc, String originalText, String repTag, String targetColor) {
        NodeList referencesList = doc.getElementsByTagName("reference");
        String remainingText = originalText;
        int fromStart = 0;
        List<Reference> references = new ArrayList<Reference>();
        if (referencesList != null) {
            if (referencesList.getLength() > 0) {
                for (int j = 0; j < referencesList.getLength(); j++) {
                    Element reference = (Element) referencesList.item(j);
                    Reference ref = new Reference();
                    if (reference.getElementsByTagName("quote") != null && reference.getElementsByTagName("quote").getLength() > 0) {
                        ref.setTextOfRef(reference.getElementsByTagName("quote").item(0).getTextContent().replace("&amp;", "&").replace("&lt;", "<"));
                    }
                    if (reference.getElementsByTagName("id") != null && reference.getElementsByTagName("id").getLength() > 0) {
                        ref.setLocalIDX(getReferenceNumber(reference.getElementsByTagName("id").item(0).getTextContent()));
                    }
                    if (reference.getElementsByTagName("color") != null && (reference.getElementsByTagName("color").getLength() > 0)) {
                        ref.setColor(reference.getElementsByTagName("color").item(0).getTextContent());
                    } else {
                        if (!targetColor.isEmpty()) {
                            ref.setColor(targetColor);
                        } else {
                            ref.setColor("yellow");
                        }
                    }
                    if (reference.getElementsByTagName("tag") != null && (reference.getElementsByTagName("tag").getLength() > 0)) {
                        ref.setTag(reference.getElementsByTagName("tag").item(0).getTextContent());
                    } else {
                        if (!repTag.isEmpty()) {
                            ref.setTag(repTag);
                        } else {
                            ref.setTag("T");
                        }
                    }
                    List<String> docs = new ArrayList<String>();

                    if (reference.getElementsByTagName("documents").item(0) != null) {
                        Element documents = (Element) reference.getElementsByTagName("documents").item(0);
                        NodeList documentsList = documents.getElementsByTagName("document");
                        if (documentsList != null) {
                            for (int i = 0; i < documentsList.getLength(); ++i) {
                                docs.add(documentsList.item(i).getTextContent().replace("&amp;", "&").replace("&lt;", "<"));
                            }
                        }
                    }
                    ref.setReferencedDocs(docs);

                    int currentpos = remainingText.indexOf(ref.getTextOfRef());
                    remainingText = remainingText.substring(currentpos + 1);
                    if (j > 0) {
                        fromStart += currentpos + 1;
                    } else {
                        fromStart = currentpos;
                    }
                    ref.setStartIDX(fromStart);
                    ref.setEndIDX(fromStart + ref.getTextOfRef().length());
                    references.add(ref);
                }
            }
            return references;
        }
        return null;
    }

    private static void addRefText(List<Reference> refs, String docSource) {
        FileInputStream in;
        try {
            in = new FileInputStream(docSource);
            String xmlContent = UtilsFiles.file2String(in, "UTF-8");
            String html = xmlContent.substring(xmlContent.indexOf("<html>"), xmlContent.indexOf("</html>"));
            if (html.contains("<body>")) {
                String comments = html.substring(html.indexOf("MYQUOTEREF") + 12, html.lastIndexOf("MYQUOTEREF"));
                if (comments.contains("0|")) {
                    String curlines = comments.substring(comments.indexOf("0"));
                    int j = 0;
                    String[] Lines = curlines.split("\\n+");
                    for (int i = 0; i < Lines.length; i++) {
                        if ((!(Lines[i].isEmpty())) && (Lines[i].contains(REFResultNice.DOC_REF_SEPARATOR))) {
                            curlines = Lines[i].substring(Lines[i].indexOf(REFResultNice.DOC_REF_SEPARATOR) + 1);
                            if ((!(curlines.isEmpty())) && (curlines.contains(REFResultNice.DOC_REF_SEPARATOR))) {
                                refs.get(j).setRefTextInDoc(curlines.substring(0, curlines.indexOf(REFResultNice.DOC_REF_SEPARATOR)));
                                j++;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WSRESTUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String generateStatsTable(List<Reference> references) {
        StringBuilder res = new StringBuilder("");
        res.append("</p><table BORDER=\"1\">\n");
        res.append("<caption><b>")
                .append(IdxConstant.MSG.get("server.qd.MSG_13"))
                .append("</b></caption>\n"); // titre
        res.append("<tr>\n" + "<th>")
                .append(IdxConstant.MSG.get("server.qd.MSG_14"))
                .append("</br>")
                .append(IdxConstant.MSG.get("server.qd.MSG_15"))
                .append("</th>\n" + "<th>")
                .append(IdxConstant.MSG.get("server.qd.MSG_16"))
                .append("</th>\n" + "<th>")
                .append(IdxConstant.MSG.get("server.qd.MSG_17"))
                .append("</th>\n"
                + "</tr>\n");
        for (Reference ref : references) {
            res.append("<tr>\n" + "<td>")
                    .append(ref.getGlobalIDX())
                    .append("</td>\n");
            res.append("<td>\n")
                    .append(ref.getTextOfRef())
                    .append("</td>\n");
            res.append("<td>\n");
            for (String doc : ref.getReferencedDocs()) {
                res.append(doc).append("<br/>\n");
            }
            res.append("</td>\n"
                    + "</tr>");
        }
        return res.toString() + "</table>";
    }

    private static String generateHTMLComments(List<Reference> references) {
        StringBuilder s = new StringBuilder("");
        s.append("\n</htmlstartcomment>MYQUOTEREF");
        s.append("\n")
                .append(references.size());
        for (int i = 0; i < references.size(); i++) {
            s.append("\n")
                    .append(i)
                    .append(REFResultNice.DOC_REF_SEPARATOR)
                    .append(references.get(i).getRefTextInDoc());
            StringBuilder dlist = new StringBuilder("");
            if (references.get(i).getReferencedDocs().isEmpty()) {
                dlist.append(REFResultNice.DOC_REF_SEPARATOR);
            }
            for (String doc : references.get(i).getReferencedDocs()) {
                dlist.append(REFResultNice.DOC_REF_SEPARATOR).append(doc);
            }
            s.append(dlist);
        }
        s.append("\nMYQUOTEREF</htmlendcomment>");
        s.append("</P>\n");
        return s.toString();
    }

    private static String mergeReferences(List<Reference> references, String originalText) {
        Stack latestOpenReference = new Stack();
        StringBuilder finalText = new StringBuilder("<P>");
        for (int i = 0; i < references.size(); i++) {
            StringBuilder ref = new StringBuilder("");
            Reference current = references.get(i);
            current.setGlobalIDX(i + 1);
            // getting the text before starting the reference
            if (i > 0) {
                Reference previous = references.get(i - 1);
                Reference containing = null;
                if (!latestOpenReference.isEmpty()) {
                    containing = (Reference) latestOpenReference.peek();
                }
                if (containing != null) {
                    if (previous.SameAs(containing)) {
                        // current is completely included in the previous: add previous text and open reference
                        // append the text before start
                        if (containing.getEffectiveStartIDX() < current.getStartIDX()) {
                            if (originalText.length() >= current.getStartIDX()) {
                                ref.append("<a href=\"#")
                                        .append(containing.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(containing.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(containing.getColor())
                                        .append("\">")
                                        .append(originalText.substring(containing.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                        .append("</FONT></a>");
                            }
                        }
                        containing.setEffectiveStartIDX(current.getStartIDX());
                        // start current
                        ref.append("<a href=\"#")
                                .append(current.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(current.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(current.getColor())
                                .append("\">[R")
                                .append(current.getTag())
                                .append(current.getLocalIDX())
                                .append("]</FONT></a>");
                    } else {
                        // previous is completely included in another reference and current might also be included
                        // test if current is fully included in containing: if it is the case we fall into checking 
                        // between prev and current
                        if (current.getEndIDX() < containing.getEndIDX()) {
                            // No pop will happen
                            if (previous.getEndIDX() <= current.getStartIDX()) {
                                // 1. previous is not overlapping with current: prev.start -> prev.end, close, add between text then open
                                // get text of previous reference
                                if (previous.getEffectiveStartIDX() < previous.getEndIDX()) {
                                    if (originalText.length() >= previous.getEndIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(previous.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(previous.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(previous.getColor())
                                                .append("\">")
                                                .append(originalText.substring(previous.getEffectiveStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                containing.setEffectiveStartIDX(previous.getEndIDX());
                                // close previous and its anchor
                                ref.append("<a href=\"#")
                                        .append(previous.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(previous.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(previous.getColor())
                                        .append("\">[E")
                                        .append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]</FONT></a>");
                                // get text before current and open the anchor of the containing reference
                                if (previous.getEndIDX() < current.getStartIDX()) {
                                    if (originalText.length() >= current.getStartIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(containing.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(containing.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(containing.getColor())
                                                .append("\">")
                                                .append(originalText.substring(previous.getEndIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                containing.setEffectiveStartIDX(current.getStartIDX());
                                // start current
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor())
                                        .append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]</FONT></a>");
                            } else {
                                // 2. previous is overlapping with current. prev.start -> curr.start, then open current, close previous 
                                // append the text before start
                                if (previous.getEffectiveStartIDX() < current.getStartIDX()) {
                                    if (originalText.length() >= current.getStartIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(previous.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(previous.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(previous.getColor())
                                                .append("\">")
                                                .append(originalText.substring(previous.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                containing.setEffectiveStartIDX(current.getStartIDX());
                                // close prev anchor and start current
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor())
                                        .append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]</FONT></a>");

                                // get text between start of current and end of previous
                                if (previous.getEndIDX() > current.getStartIDX()) {
                                    if (originalText.length() >= previous.getEndIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(current.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(current.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(current.getColor())
                                                .append("\">")
                                                .append(originalText.substring(current.getStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                current.setEffectiveStartIDX(previous.getEndIDX());
                                containing.setEffectiveStartIDX(previous.getEndIDX());
                                // close previous
                                ref.append("<a href=\"#")
                                        .append(previous.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(previous.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(previous.getColor())
                                        .append("\">[E")
                                        .append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]</FONT></a>");
                            }
                        } else {
                            // Anything ending before the current starts should be ended
                            // close everything before the start of current 
                            // && close everything before the end of current
                            if (previous.getEndIDX() <= current.getStartIDX()) {
                                // 1. previous is not overlapping with current
                                // close previous
                                if (previous.getEffectiveStartIDX() < previous.getEndIDX()) {
                                    if (originalText.length() >= previous.getEndIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(previous.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(previous.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(previous.getColor())
                                                .append("\">")
                                                .append(originalText.substring(previous.getEffectiveStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                ref.append("<a href=\"#")
                                        .append(previous.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(previous.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(previous.getColor())
                                        .append("\">[E")
                                        .append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]</FONT></a>");
                                containing.setEffectiveStartIDX(previous.getEndIDX());
                                // close anything in between
                                boolean stop = false;
                                int latestPosition = previous.getEndIDX();
                                // previous anchor is closed
                                while (!latestOpenReference.isEmpty() && !stop) {
                                    if (((Reference) latestOpenReference.peek()).getEndIDX() <= current.getStartIDX()) {
                                        Reference prevContaining = (Reference) latestOpenReference.pop();
                                        // open an anchor for the latest containing reference
                                        // add remaining text of the latest containing reference
                                        if (prevContaining.getEffectiveStartIDX() < prevContaining.getEndIDX()) {
                                            if (originalText.length() >= prevContaining.getEndIDX()) {
                                                ref.append("<a href=\"#")
                                                        .append(prevContaining.getGlobalIDX())
                                                        .append("\" id=\"ref")
                                                        .append(prevContaining.getGlobalIDX())
                                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                        .append(prevContaining.getColor())
                                                        .append("\">")
                                                        .append(originalText.substring(prevContaining.getEffectiveStartIDX(), prevContaining.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                        .append("</FONT></a>");
                                            }
                                        }
                                        // close the latest containing reference
                                        ref.append("<a href=\"#")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(prevContaining.getColor())
                                                .append("\">[E")
                                                .append(prevContaining.getTag())
                                                .append(prevContaining.getLocalIDX())
                                                .append("]</FONT></a>");
                                        latestPosition = prevContaining.getEndIDX();
                                        // if something is still in the stack then it references the text 
                                        // between the last close and the current start
                                        Reference prevPrevContaining = null;
                                        if (!latestOpenReference.isEmpty()) {
                                            prevPrevContaining = (Reference) latestOpenReference.peek();
                                        }
                                        if (prevPrevContaining != null) {
                                            // update its position
                                            prevPrevContaining.setEffectiveStartIDX(prevContaining.getEndIDX());
                                        }
                                    } else {
                                        stop = true;
                                    }
                                }
                                // append text before start
                                containing = null;
                                if (!latestOpenReference.isEmpty()) {
                                    containing = (Reference) latestOpenReference.peek();
                                }
                                if (containing != null) {
                                    // get text before current
                                    if (containing.getEffectiveStartIDX() < current.getStartIDX()) {
                                        if (originalText.length() >= current.getStartIDX()) {
                                            ref.append("<a href=\"#")
                                                    .append(containing.getGlobalIDX())
                                                    .append("\" id=\"ref")
                                                    .append(containing.getGlobalIDX())
                                                    .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                    .append(containing.getColor())
                                                    .append("\">")
                                                    .append(originalText.substring(containing.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                                    .append("</FONT></a>");
                                        }
                                    }
                                    containing.setEffectiveStartIDX(current.getStartIDX());
                                } else {
                                    if (latestPosition < current.getStartIDX()) {
                                        if (originalText.length() >= current.getStartIDX()) {
                                            ref.append(originalText.substring(latestPosition, current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                                        }
                                    }
                                }
                                // start current
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor())
                                        .append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]</FONT></a>");

                                // close anything before end of current
                                stop = false;
                                while (!latestOpenReference.isEmpty() && !stop) {
                                    if (((Reference) latestOpenReference.peek()).getEndIDX() <= current.getEndIDX()) {
                                        Reference prevContaining = (Reference) latestOpenReference.pop();
                                        if (prevContaining.getEffectiveStartIDX() < prevContaining.getEndIDX()) {
                                            if (originalText.length() >= prevContaining.getEndIDX()) {
                                                ref.append("<a href=\"#")
                                                        .append(current.getGlobalIDX())
                                                        .append("\" id=\"ref")
                                                        .append(current.getGlobalIDX())
                                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                        .append(current.getColor())
                                                        .append("\">")
                                                        .append(originalText.substring(prevContaining.getEffectiveStartIDX(), prevContaining.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                        .append("</FONT></a>");
                                            }
                                        }
                                        // close prevContaining
                                        ref.append("<a href=\"#")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(prevContaining.getColor())
                                                .append("\">[E")
                                                .append(prevContaining.getTag())
                                                .append(prevContaining.getLocalIDX())
                                                .append("]</FONT></a>");
                                        current.setEffectiveStartIDX(prevContaining.getEndIDX());
                                        // if something is still in the stack then it references the text 
                                        // between the last close and the current start
                                        Reference prevPrevContaining = null;
                                        if (!latestOpenReference.isEmpty()) {
                                            prevPrevContaining = (Reference) latestOpenReference.peek();
                                        }
                                        if (prevPrevContaining != null) {
                                            prevPrevContaining.setEffectiveStartIDX(prevContaining.getEndIDX());
                                        }
                                    } else {
                                        stop = true;
                                    }
                                }
                            } else {
                                // 2. previous is overlapping with current
                                // add text before current starts
                                if (previous.getEffectiveStartIDX() < current.getStartIDX()) {
                                    if (originalText.length() >= current.getStartIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(previous.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(previous.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(previous.getColor())
                                                .append("\">")
                                                .append(originalText.substring(previous.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                // close prev anchor and start current
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor())
                                        .append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]</FONT></a>");

                                // get text between start of current and end of previous
                                if (previous.getEndIDX() > current.getStartIDX()) {
                                    if (originalText.length() >= current.getStartIDX()) {
                                        ref.append("<a href=\"#")
                                                .append(current.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(current.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(current.getColor())
                                                .append("\">")
                                                .append(originalText.substring(current.getStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                }
                                // close previous
                                ref.append("<a href=\"#")
                                        .append(previous.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(previous.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(previous.getColor())
                                        .append("\">[E")
                                        .append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]</FONT></a>");
                                current.setEffectiveStartIDX(previous.getEndIDX());
                                containing.setEffectiveStartIDX(previous.getEndIDX());
                                // close anything before end of current
                                boolean stop = false;
                                while (!latestOpenReference.isEmpty() && !stop) {
                                    if (((Reference) latestOpenReference.peek()).getEndIDX() <= current.getEndIDX()) {
                                        Reference prevContaining = (Reference) latestOpenReference.pop();
                                        if (prevContaining.getEffectiveStartIDX() < prevContaining.getEndIDX()) {
                                            if (originalText.length() >= prevContaining.getEndIDX()) {
                                                ref.append("<a href=\"#")
                                                        .append(current.getGlobalIDX())
                                                        .append("\" id=\"ref")
                                                        .append(current.getGlobalIDX())
                                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                        .append(current.getColor())
                                                        .append("\">").append(originalText.substring(prevContaining.getEffectiveStartIDX(), prevContaining.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                                        .append("</FONT></a>");
                                            }
                                        }
                                        // close prevContaining
                                        ref.append("<a href=\"#")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(prevContaining.getColor())
                                                .append("\">[E")
                                                .append(prevContaining.getTag())
                                                .append(prevContaining.getLocalIDX())
                                                .append("]</FONT></a>");
                                        current.setEffectiveStartIDX(prevContaining.getEndIDX());
                                        // if something is still in the stack then it references the text 
                                        // between the last close and the current start
                                        Reference prevPrevContaining = null;
                                        if (!latestOpenReference.isEmpty()) {
                                            prevPrevContaining = (Reference) latestOpenReference.peek();
                                        }
                                        if (prevPrevContaining != null) {
                                            prevPrevContaining.setEffectiveStartIDX(prevContaining.getEndIDX());
                                        }
                                    } else {
                                        stop = true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // No contaning reference yet
                    if (previous.getEndIDX() <= current.getStartIDX()) {
                        // 1. previous is not overlapping with current: prev.start -> prev.end, close, add between text then open
                        if (previous.getEffectiveStartIDX() < previous.getEndIDX()) {
                            if (originalText.length() >= previous.getEndIDX()) {
                                ref.append("<a href=\"#")
                                        .append(previous.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(previous.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(previous.getColor())
                                        .append("\">")
                                        .append(originalText.substring(previous.getEffectiveStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                        .append("</FONT></a>");
                            }
                        }
                        ref.append("<a href=\"#")
                                .append(previous.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(previous.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(previous.getColor())
                                .append("\">[E")
                                .append(previous.getTag())
                                .append(previous.getLocalIDX())
                                .append("]</FONT></a>");
                        if (previous.getEndIDX() < current.getStartIDX()) {
                            if (originalText.length() >= current.getStartIDX()) {
                                if (originalText.length() >= current.getStartIDX()) {
                                    ref.append(originalText.substring(previous.getEndIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                            }
                        }
                        ref.append("<a href=\"#")
                                .append(current.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(current.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(current.getColor())
                                .append("\">[R")
                                .append(current.getTag())
                                .append(current.getLocalIDX())
                                .append("]</FONT></a>");

                    } else {
                        // 2. previous is overlapping with current. prev.start -> curr.start, then open current, close previous 
                        if (previous.getEffectiveStartIDX() < current.getStartIDX()) {
                            if (originalText.length() >= current.getStartIDX()) {
                                ref.append("<a href=\"#")
                                        .append(previous.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(previous.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(previous.getColor())
                                        .append("\">")
                                        .append(originalText.substring(previous.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                        .append("</FONT></a>");
                            }
                        }
                        ref.append("<a href=\"#")
                                .append(current.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(current.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(current.getColor())
                                .append("\">[R")
                                .append(current.getTag())
                                .append(current.getLocalIDX())
                                .append("]</FONT></a>");
                        if (previous.getEndIDX() > current.getStartIDX()) {
                            if (originalText.length() >= previous.getEndIDX()) {
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor())
                                        .append("\">")
                                        .append(originalText.substring(current.getStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                        .append("</FONT></a>");
                            }
                            current.setEffectiveStartIDX(previous.getEndIDX());
                        }
                        ref.append("<a href=\"#")
                                .append(previous.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(previous.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(previous.getColor())
                                .append("\">[E")
                                .append(previous.getTag())
                                .append(previous.getLocalIDX())
                                .append("]</FONT></a>");
                    }
                }
            } else {
                // first reference, append all the text before it starts
                ref.append(originalText.substring(0, current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                ref.append("<a href=\"#")
                        .append(current.getGlobalIDX())
                        .append("\" id=\"ref")
                        .append(current.getGlobalIDX())
                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                        .append(current.getColor())
                        .append("\">[R")
                        .append(current.getTag())
                        .append(current.getLocalIDX())
                        .append("]</FONT></a>");
            }
            // Adding in the pile (if current does not close before the closing of the next)
            if (i < references.size() - 2) {
                Reference next = references.get(i + 1);
                if (current.getEndIDX() > next.getEndIDX()) {
                    latestOpenReference.push(current);
                }
            }
            if (i == (references.size() - 1)) {
                // last reference and no other reference to close, 
                // add the highlighted text and the remining text of the document
                if (latestOpenReference.isEmpty()) {
                    if (current.getEffectiveStartIDX() < current.getEndIDX()) {
                        if (originalText.length() >= current.getEndIDX()) {
                            ref.append("<a href=\"#")
                                    .append(current.getGlobalIDX())
                                    .append("\" id=\"ref")
                                    .append(current.getGlobalIDX())
                                    .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                    .append(current.getColor())
                                    .append("\">")
                                    .append(originalText.substring(current.getEffectiveStartIDX(), current.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                    .append("</FONT></a>");
                        }
                    }
                    ref.append("<a href=\"#")
                            .append(current.getGlobalIDX())
                            .append("\" id=\"ref")
                            .append(current.getGlobalIDX())
                            .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                            .append(current.getColor())
                            .append("\">[E")
                            .append(current.getTag())
                            .append(current.getLocalIDX())
                            .append("]</FONT></a>");
                    if (originalText.length() >= current.getEndIDX()) {
                        ref.append(originalText.substring(current.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                    }
                } else {
                    // Any remaining reference in the stack is a containing reference for the current
                    Reference containing = null;
                    if (!latestOpenReference.isEmpty()) {
                        containing = (Reference) latestOpenReference.peek();
                    }
                    if (current.getEffectiveStartIDX() < current.getEndIDX()) {
                        if (originalText.length() >= current.getEndIDX()) {
                            ref.append("<a href=\"#")
                                    .append(current.getGlobalIDX())
                                    .append("\" id=\"ref")
                                    .append(current.getGlobalIDX())
                                    .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                    .append(current.getColor())
                                    .append("\">")
                                    .append(originalText.substring(current.getEffectiveStartIDX(), current.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                    .append("</FONT></a>");
                        }
                    }
                    ref.append("<a href=\"#")
                            .append(current.getGlobalIDX())
                            .append("\" id=\"ref")
                            .append(current.getGlobalIDX())
                            .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                            .append(current.getColor())
                            .append("\">[E")
                            .append(current.getTag())
                            .append(current.getLocalIDX())
                            .append("]</FONT></a>");
                    if (containing != null) {
                        containing.setEffectiveStartIDX(current.getEndIDX());
                    }
                    int lastClosingPosition = current.getEndIDX();
                    while (!latestOpenReference.isEmpty()) {
                        containing = (Reference) latestOpenReference.pop();
                        if (containing.getEffectiveStartIDX() < containing.getEndIDX()) {
                            if (originalText.length() >= containing.getEndIDX()) {
                                ref.append("<a href=\"#")
                                        .append(containing.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(containing.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(containing.getColor())
                                        .append("\">")
                                        .append(originalText.substring(containing.getEffectiveStartIDX(), containing.getEndIDX()).replaceAll("\n", "<br/><br/>"))
                                        .append("</FONT></a>");
                            }
                        }
                        ref.append("<a href=\"#")
                                .append(containing.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(containing.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(containing.getColor())
                                .append("\">[E")
                                .append(containing.getTag())
                                .append(containing.getLocalIDX())
                                .append("]</FONT></a>");
                        lastClosingPosition = containing.getEndIDX();
                        Reference prevPrevContaining = null;
                        if (!latestOpenReference.isEmpty()) {
                            prevPrevContaining = (Reference) latestOpenReference.peek();
                        }
                        if (prevPrevContaining != null) {
                            prevPrevContaining.setEffectiveStartIDX(containing.getEndIDX());
                        }
                    }
                    // add the remaining text if any
                    if (lastClosingPosition < originalText.length() - 1) {
                        ref.append(originalText.substring(lastClosingPosition).replaceAll("\n", "<br/><br/>"));
                    }
                }
            }
            finalText.append(ref.toString());
        }
        finalText.append("</P>\n");
        return finalText.toString();
    }
}

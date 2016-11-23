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
            mergedRefDoc += WSRESTUtil.mergeHTMLContent(file1, file2, doc, doc1, "T", "J", "red", doc.getElementsByTagName("reference").getLength(), totalRefs);
            // merge details
            mergedRefDoc += WSRESTUtil.mergeInfo(doc, doc1, "red", doc.getElementsByTagName("reference").getLength());
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

    public static String unCommentRefDoc(String s) {
        s = s.replace("<!--", "</htmlstartcomment>");
        s = s.replace("-->", "</htmlendcomment>");
        return s;
    }

    public static String reCommentRefDoc(String s) {
        s = s.replace("</htmlstartcomment>", "<!--");
        s = s.replace("</htmlendcomment>", "-->");
        return s;
    }

    /*
     * (@DefaultValue("") @QueryParam("TxtSrc") String TxtSrc,
     @DefaultValue("") @QueryParam("TxtTgt") String DocSrc,
     @DefaultValue("") @QueryParam("TxtTgt") String DocTgt,
     @DefaultValue("EN") @QueryParam("LngSrc") String LngSrc,
     @DefaultValue("FR") @QueryParam("LngTgt") String LngTgt,
     @DefaultValue("") @QueryParam("Filter") String Filter,
     @DefaultValue("3") @QueryParam("MinLen") Integer MinLen,
     @DefaultValue("FALSE") @QueryParam("RemFirst") Boolean RemFirst,
     @DefaultValue("FALSE") @QueryParam("Fast") Boolean Fast) 
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
                + "   <msg>" + msg + "</msg>\n"
                + "   <TxtSrc>" + TxtSrc + "</TxtSrc>\n"
                + "   <RefType>" + RefType + "</RefType>\n"
                + "   <DocSrc>" + DocSrc + "</DocSrc>\n"
                + "   <DocTgt>" + DocTgt + "</DocTgt>\n"
                + "   <LngSrc>" + LngSrc + "</LngSrc>\n"
                + "   <LngTgt>" + LngTgt + "</LngTgt>\n"
                + "   <Filter>" + collections + "</Filter>\n"
                + "   <MinLen>" + MinLen + "</MinLen>\n"
                + "   <RemFirst>" + RemFirst + "</RemFirst>\n"
                + "   <Fast>" + Fast + "</Fast>\n"
                + "</parameters>\n";

    }

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

    public static String mergeXMLParameters(Document doc1, Document doc2) {
        return "<parameters>\n"
                + "   <msg>" + doc1.getDocumentElement().getElementsByTagName("msg").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("msg").item(0).getTextContent() + "</msg>\n"
                + "   <TxtSrc>" + doc1.getDocumentElement().getElementsByTagName("TxtSrc").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("TxtSrc").item(0).getTextContent() + "</TxtSrc>\n"
                + "   <RefType>" + doc1.getDocumentElement().getElementsByTagName("RefType").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("RefType").item(0).getTextContent() + "</RefType>\n"
                + "   <DocSrc>" + doc1.getDocumentElement().getElementsByTagName("DocSrc").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("DocSrc").item(0).getTextContent() + "</DocSrc>\n"
                + "   <DocTgt>" + doc1.getDocumentElement().getElementsByTagName("DocTgt").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("DocTgt").item(0).getTextContent() + "</DocTgt>\n"
                + "   <LngSrc>" + doc1.getDocumentElement().getElementsByTagName("LngSrc").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("LngSrc").item(0).getTextContent() + "</LngSrc>\n"
                + "   <LngTgt>" + doc1.getDocumentElement().getElementsByTagName("LngTgt").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("LngTgt").item(0).getTextContent() + "</LngTgt>\n"
                + "   <Filter>" + doc1.getDocumentElement().getElementsByTagName("Filter").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("Filter").item(0).getTextContent() + "</Filter>\n"
                + "   <MinLen>" + doc1.getDocumentElement().getElementsByTagName("MinLen").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("MinLen").item(0).getTextContent() + "</MinLen>\n"
                + "   <Fast>" + doc1.getDocumentElement().getElementsByTagName("Fast").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("Fast").item(0).getTextContent() + "</Fast>\n"
                + "</parameters>\n";
    }

    public static String mergeXMLStatistics(Document doc1, Document doc2) {
        String organization = "";
        NodeList nodes = doc1.getDocumentElement().getElementsByTagName("organisation");
        NodeList nodes1 = doc2.getDocumentElement().getElementsByTagName("organisation");
        for (int i = 0; i < nodes.item(0).getChildNodes().getLength(); i++) {
            if (nodes.item(0).getChildNodes().item(i).getNodeName().contains("text")) {
                organization += "";
            } else if (nodes.item(0).getChildNodes().item(i).getNodeName().contains("comment")) {
                organization += "<!--" + nodes.item(0).getChildNodes().item(i).getTextContent() + "-->\n";
            } else {
                organization += "<" + nodes.item(0).getChildNodes().item(i).getNodeName() + ">" + nodes.item(0).getChildNodes().item(i).getTextContent() + "</" + nodes.item(0).getChildNodes().item(i).getNodeName() + ">\n";
            }
        }
        for (int i = 0; i < nodes1.item(0).getChildNodes().getLength(); i++) {
            if (nodes1.item(0).getChildNodes().item(i).getNodeName().contains("text")) {
                organization += "";
            } else if (nodes1.item(0).getChildNodes().item(i).getNodeName().contains("comment")) {
                organization += "<!--" + nodes1.item(0).getChildNodes().item(i).getTextContent() + "-->\n";
            } else {
                organization += "<" + nodes1.item(0).getChildNodes().item(i).getNodeName() + ">" + nodes1.item(0).getChildNodes().item(i).getTextContent() + "</" + nodes1.item(0).getChildNodes().item(i).getNodeName() + ">\n";
            }
        }
        return "<statistics>\n"
                + "  <mycat>\n"
                + "    <RefDocFullName>" + doc1.getDocumentElement().getElementsByTagName("RefDocFullName").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("RefDocFullName").item(0).getTextContent() + "</RefDocFullName>\n"
                + "    <RefDocType>" + doc1.getDocumentElement().getElementsByTagName("RefDocType").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("RefDocType").item(0).getTextContent() + "</RefDocType>\n"
                + "    <RefDocLng>" + doc1.getDocumentElement().getElementsByTagName("RefDocLng").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("RefDocLng").item(0).getTextContent() + "</RefDocLng>\n"
                + "    <RefDocPerCent>" + doc1.getDocumentElement().getElementsByTagName("RefDocPerCent").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("RefDocPerCent").item(0).getTextContent() + "</RefDocPerCent>\n"
                + "    <RefDocOccurences>" + doc1.getDocumentElement().getElementsByTagName("RefDocOccurences").item(0).getTextContent() + "|" + doc2.getDocumentElement().getElementsByTagName("RefDocOccurences").item(0).getTextContent() + "</RefDocOccurences>\n"
                + "  </mycat>\n"
                + "  <organisation>\n"
                + organization
                + "  </organisation>\n"
                + "</statistics>\n";
    }

    public static String mergeHTMLContent(String docSource1, String docSource2, Document doc1, Document doc2, String repTag1, String repTag2, String color2, int start, int totalRefs) {
        String origText = doc1.getDocumentElement().getElementsByTagName("origText").item(0).getTextContent().replace("Â", "");

        List<Reference> references = getReferences(doc1, origText, repTag1, "");
        references.addAll(getReferences(doc2, origText, repTag2, color2));
        Collections.sort(references);

        StringBuilder res = new StringBuilder("");
        res.append("<htmlRefDoc>\n");
        res.append("<!-- <html>\n");
        res.append("<head>\n");
        res.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">\n");
        res.append("<title>myQuote</title>");
        res.append("</head>\n");
        res.append("<body>\n");
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
        res.append("</body> </html> -->\n"
                + "</htmlRefDoc>");
        return res.toString();
    }

    public static String mergeInfo(Document doc1, Document doc2, String color, int start) {
        return "<Info>\n"
                + "<references>"
                + getReferencesFromDocument(doc1, "", 0)
                + getReferencesFromDocument(doc2, color, start)
                + "</references>"
                + "</Info>";
    }

    public static String getReferencesFromDocument(Document doc, String color, int start) {
        String references = "";
        NodeList referencesList = doc.getElementsByTagName("reference");
        for (int j = 0; j < referencesList.getLength(); ++j) {
            Element reference = (Element) referencesList.item(j);
            references += "<reference>\n"
                    + "<number>" + getReferenceNumberAsString(reference.getElementsByTagName("id").item(0).getTextContent(), start) + "</number>\n"
                    + "<id>" + reference.getElementsByTagName("id").item(0).getTextContent() + "</id>\n"
                    + "<quote>" + reference.getElementsByTagName("quote").item(0).getTextContent() + "</quote>\n"
                    + "<documents>\n";
            Element documents = (Element) reference.getElementsByTagName("documents").item(0);
            NodeList documentsList = documents.getElementsByTagName("document");
            for (int i = 0; i < documentsList.getLength(); ++i) {
                references += "<document>" + documentsList.item(i).getTextContent() + "</document>\n";
            }
            references += "</documents>\n";
            if (!color.isEmpty()) {
                references += "<color>" + color + "</color>\n";
            } else {
                if (reference.getElementsByTagName("color") != null && (reference.getElementsByTagName("color").getLength() > 0)) {
                    references += "<color>" + reference.getElementsByTagName("color").item(0).getTextContent() + "</color>\n";
                } else {
                    references += "<color>yellow</color>\n";
                }
            }
            references += "</reference>\n";
        }
        return references;
    }

    private static Integer getReferenceNumber(String refId) {
        int refNumber = 0;
        if (refId.matches("\\d+")) {
            refNumber = Integer.parseInt(refId);
        }
        return refNumber;
    }

    private static String getReferenceNumberAsString(String refId, int start) {
        int refNumber = 0;
        if (refId.matches("\\d+")) {
            refNumber = Integer.parseInt(refId) + start;
        }
        return "" + refNumber;
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
            String html1 = xmlContent1.substring(xmlContent.indexOf("<html>"), xmlContent.indexOf("</html>"));
            if (html.contains("<body>")) {
                String stats1 = html.substring(html.indexOf("<td>"), html.indexOf("</table>"));
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
            if (html1.contains("<body>")) {
                String stats2 = html1.substring(html1.indexOf("<td>"), html1.indexOf("</table>"));
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
                res.append(stats2);
            }
            return res.toString();
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
        List<Reference> references = new ArrayList<Reference>();
        if (referencesList.getLength() > 0) {
            for (int j = 0; j < referencesList.getLength(); j++) {
                Element reference = (Element) referencesList.item(j);
                Reference ref = new Reference();
                ref.setTextOfRef(reference.getElementsByTagName("quote").item(0).getTextContent());
                ref.setLocalIDX(getReferenceNumber(reference.getElementsByTagName("id").item(0).getTextContent()));
                if (!targetColor.isEmpty()) {
                    ref.setColor(targetColor);
                } else {
                    if (reference.getElementsByTagName("color") != null && (reference.getElementsByTagName("color").getLength() > 0)) {
                        ref.setColor(reference.getElementsByTagName("color").item(0).getTextContent());
                    } else {
                        ref.setColor("yellow");
                    }
                }
                Element documents = (Element) reference.getElementsByTagName("documents").item(0);
                NodeList documentsList = documents.getElementsByTagName("document");
                List<String> docs = new ArrayList<String>();
                for (int i = 0; i < documentsList.getLength(); ++i) {
                    docs.add(documentsList.item(i).getTextContent());
                }
                ref.setTag(repTag);
                ref.setStartIDX(originalText.indexOf(ref.getTextOfRef()));
                ref.setEndIDX(ref.getStartIDX() + ref.getTextOfRef().length());
                ref.setReferencedDocs(docs);
                references.add(ref);
            }
            return references;
        }
        return null;
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
        return res.toString();
    }

    private static String generateHTMLComments(List<Reference> references) {
        StringBuilder s = new StringBuilder("");
        s.append("\n</htmlstartcomment>MYQUOTEREF");
        s.append("\n").append(references.size());
        for (int i = 0; i < references.size(); i++) {
            s.append("\n")
                    .append(i)
                    .append(REFResultNice.DOC_REF_SEPARATOR)
                    .append(references.get(i).getTextOfRef())
                    .append(REFResultNice.DOC_REF_SEPARATOR);
            StringBuilder dlist = new StringBuilder("");
            for (String doc : references.get(i).getReferencedDocs()) {
                dlist.append(REFResultNice.DOC_REF_SEPARATOR)
                        .append(doc);
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
                            ref.append(originalText.substring(containing.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                        }
                        containing.setEffectiveStartIDX(current.getStartIDX());
                        // start current
                        ref.append("</FONT></a><a href=\"#")
                                .append(current.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(current.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(current.getColor())
                                .append("\">[R")
                                .append(current.getTag())
                                .append(current.getLocalIDX())
                                .append("]");
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
                                    ref.append(originalText.substring(previous.getEffectiveStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                                // close previous ans its anchor
                                ref.append("[E").append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]</FONT></a>");
                                // get text before current and open the anchor of the containing reference
                                if (previous.getEndIDX() < current.getStartIDX()) {
                                    ref.append("<a href=\"#")
                                            .append(containing.getGlobalIDX())
                                            .append("\" id=\"ref")
                                            .append(containing.getGlobalIDX())
                                            .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                            .append(containing.getColor())
                                            .append("\">");
                                    ref.append(originalText.substring(previous.getEndIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                            .append("</FONT></a>");
                                }
                                containing.setEffectiveStartIDX(current.getStartIDX());
                                // start current
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor()).append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]");
                            } else {
                                // 2. previous is overlapping with current. prev.start -> curr.start, then open current, close previous 
                                // append the text before start
                                if (previous.getEffectiveStartIDX() < current.getStartIDX()) {
                                    ref.append(originalText.substring(previous.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                                // close prev anchor and start current
                                ref.append("</FONT></a><a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor()).append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]");
                                // get text between start of current and end of previous
                                if (previous.getEndIDX() > current.getStartIDX()) {
                                    ref.append(originalText.substring(current.getStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                                current.setEffectiveStartIDX(previous.getEndIDX());
                                containing.setEffectiveStartIDX(previous.getEndIDX());
                                // close previous
                                ref.append("[E")
                                        .append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]");
                            }
                        } else {
                            // Anything ending before the current starts should be ended
                            // close everything before the start of current 
                            // && close everything before the end of current
                            if (previous.getEndIDX() <= current.getStartIDX()) {
                                // 1. previous is not overlapping with current
                                // close previous
                                if (previous.getEffectiveStartIDX() < previous.getEndIDX()) {
                                    ref.append(originalText.substring(previous.getEffectiveStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                                ref.append("[E")
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
                                        ref.append("<a href=\"#")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(prevContaining.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(prevContaining.getColor())
                                                .append("\">");
                                        // add remaining text of the latest containing reference
                                        if (prevContaining.getEffectiveStartIDX() < prevContaining.getEndIDX()) {
                                            ref.append(originalText.substring(prevContaining.getEffectiveStartIDX(), prevContaining.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                        }
                                        // close the latest containing reference
                                        ref.append("[E")
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
                                        ref.append("<a href=\"#")
                                                .append(containing.getGlobalIDX())
                                                .append("\" id=\"ref")
                                                .append(containing.getGlobalIDX())
                                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                                .append(containing.getColor())
                                                .append("\">");
                                        ref.append(originalText.substring(containing.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"))
                                                .append("</FONT></a>");
                                    }
                                    containing.setEffectiveStartIDX(current.getStartIDX());
                                } else {
                                    if (latestPosition < current.getStartIDX()) {
                                        ref.append(originalText.substring(latestPosition, current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                                    }
                                }
                                // start current
                                ref.append("<a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor())
                                        .append("\">");
                                // close anything before end of current
                                stop = false;
                                while (!latestOpenReference.isEmpty() && !stop) {
                                    if (((Reference) latestOpenReference.peek()).getEndIDX() <= current.getEndIDX()) {
                                        Reference prevContaining = (Reference) latestOpenReference.pop();
                                        if (prevContaining.getEffectiveStartIDX() < prevContaining.getEndIDX()) {
                                            ref.append(originalText.substring(prevContaining.getEffectiveStartIDX(), prevContaining.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                        }
                                        // close prevContaining
                                        ref.append("[E")
                                                .append(prevContaining.getTag())
                                                .append(prevContaining.getLocalIDX())
                                                .append("]");
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
                                    ref.append(originalText.substring(previous.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                                // close prev anchor and start current
                                ref.append("</FONT></a><a href=\"#")
                                        .append(current.getGlobalIDX())
                                        .append("\" id=\"ref")
                                        .append(current.getGlobalIDX())
                                        .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                        .append(current.getColor()).append("\">[R")
                                        .append(current.getTag())
                                        .append(current.getLocalIDX())
                                        .append("]");
                                // get text between start of current and end of previous
                                if (previous.getEndIDX() > current.getStartIDX()) {
                                    ref.append(originalText.substring(current.getStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                }
                                current.setEffectiveStartIDX(previous.getEndIDX());
                                containing.setEffectiveStartIDX(previous.getEndIDX());
                                // close previous
                                ref.append("[E")
                                        .append(previous.getTag())
                                        .append(previous.getLocalIDX())
                                        .append("]");

                                // close anything before end of current
                                boolean stop = false;
                                while (!latestOpenReference.isEmpty() && !stop) {
                                    if (((Reference) latestOpenReference.peek()).getEndIDX() <= current.getEndIDX()) {
                                        Reference prevContaining = (Reference) latestOpenReference.pop();
                                        if (prevContaining.getEffectiveStartIDX() < prevContaining.getEndIDX()) {
                                            ref.append(originalText.substring(prevContaining.getEffectiveStartIDX(), prevContaining.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                                        }
                                        // close prevContaining
                                        ref.append("[E")
                                                .append(prevContaining.getTag())
                                                .append(prevContaining.getLocalIDX())
                                                .append("]");
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
                            ref.append(originalText.substring(previous.getEffectiveStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                        }
                        ref.append("[E")
                                .append(previous.getTag())
                                .append(previous.getLocalIDX())
                                .append("]</FONT></a>");
                        if (previous.getEndIDX() < current.getStartIDX()) {
                            ref.append(originalText.substring(previous.getEndIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
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
                                .append("]");
                    } else {
                        // 2. previous is overlapping with current. prev.start -> curr.start, then open current, close previous 
                        if (previous.getEffectiveStartIDX() < current.getStartIDX()) {
                            ref.append(originalText.substring(previous.getEffectiveStartIDX(), current.getStartIDX()).replaceAll("\n", "<br/><br/>"));
                        }
                        ref.append("</FONT></a><a href=\"#")
                                .append(current.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(current.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(current.getColor())
                                .append("\">[R")
                                .append(current.getTag())
                                .append(current.getLocalIDX())
                                .append("]");
                        if (previous.getEndIDX() > current.getStartIDX()) {
                            ref.append(originalText.substring(current.getStartIDX(), previous.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                            current.setEffectiveStartIDX(previous.getEndIDX());
                        }
                        ref.append("[E")
                                .append(previous.getTag())
                                .append(previous.getLocalIDX())
                                .append("]");
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
                        .append("]");
            }
            // Adding in the pile (if current does not close before the closing of the next)
            if (i < references.size() - 2) {
                Reference next = references.get(i + 1);
                if (current.getEndIDX() > next.getEndIDX()) {
                    latestOpenReference.push(current);
                }
            } else {
                // last reference and no other reference to close, add the highlighted text and the remining test of the document
                if (latestOpenReference.isEmpty()) {
                    if (current.getEffectiveStartIDX() < current.getEndIDX()) {
                        ref.append(originalText.substring(current.getEffectiveStartIDX(), current.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                    }
                    ref.append("[E")
                            .append(current.getTag())
                            .append(current.getLocalIDX())
                            .append("]</FONT></a>");
                    ref.append(originalText.substring(current.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                } else {
                    // Any remaining reference in the stack is a containing reference for the current
                    Reference containing = null;
                    if (!latestOpenReference.isEmpty()) {
                        containing = (Reference) latestOpenReference.peek();
                    }
                    if (current.getEffectiveStartIDX() < current.getEndIDX()) {
                        ref.append(originalText.substring(current.getEffectiveStartIDX(), current.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                    }
                    ref.append("[E")
                            .append(current.getTag())
                            .append(current.getLocalIDX())
                            .append("]</FONT></a>");
                    if (containing != null) {
                        containing.setEffectiveStartIDX(current.getEndIDX());
                    }
                    int lastClosingPosition = current.getEndIDX();
                    while (!latestOpenReference.isEmpty()) {
                        containing = (Reference) latestOpenReference.pop();
                        ref.append("<a href=\"#")
                                .append(containing.getGlobalIDX())
                                .append("\" id=\"ref")
                                .append(containing.getGlobalIDX())
                                .append("\" onClick=\"return gwtnav(this);\"><FONT style=\"BACKGROUND-COLOR: ")
                                .append(containing.getColor())
                                .append("\">[R")
                                .append(containing.getTag())
                                .append(containing.getLocalIDX())
                                .append("]");
                        if (containing.getEffectiveStartIDX() < containing.getEndIDX()) {
                            ref.append(originalText.substring(containing.getEffectiveStartIDX(), containing.getEndIDX()).replaceAll("\n", "<br/><br/>"));
                        }
                        ref.append("[E")
                                .append(current.getTag())
                                .append(current.getLocalIDX())
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

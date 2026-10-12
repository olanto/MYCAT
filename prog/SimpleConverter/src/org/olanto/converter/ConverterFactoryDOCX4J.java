/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myCAT.

   myCAT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myCAT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myCAT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package org.olanto.converter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.docx4j.TraversalUtil;
import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.AbstractHtmlExporter.HtmlSettings;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.model.listnumbering.Emulator;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FootnotesPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Br;
import org.docx4j.wml.CTFootnotes;
import org.docx4j.wml.CTFtnEdn;
import org.docx4j.wml.CTFtnEdnRef;
import org.docx4j.wml.CTSimpleField;
import org.docx4j.wml.CTTxbxContent;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.SdtRun;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;

/**
 * Conversion .docx → TXT/HTML via docx4j 6.1.2.
 *
 * Branche TXT : walker structuré qui préserve paragraphes, titres, tableaux,
 * notes de bas de page et nettoie les champs Word (PAGEREF, _Toc, HYPERLINK…).
 */
public class ConverterFactoryDOCX4J extends AbstractConverterFactory {

    private static final Logger _logger = Logger.getLogger(ConverterFactoryDOCX4J.class);

    public static AbstractConverterFactory getInstance() {
        return new ConverterFactoryDOCX4J();
    }

    @Override
    public void startConvertion() {
        _logger.debug("Start converting " + source.getName());
        long startTime = System.currentTimeMillis();
        Writer out = null;
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(source);
            if (outputFormat.equalsIgnoreCase(Constants.TXT)) {
                // UTF-8 : sans ça, l'encodage défaut Windows (ex. CP1252) remplace AR/RU/ZH par '?'
                out = new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8);
                DocxTextWalker walker = new DocxTextWalker(wordMLPackage, out);
                walker.run();
                success = true;
            } else if (outputFormat.equalsIgnoreCase(Constants.HTML)) {
                HtmlSettings htmlSetting = new HtmlSettings();
                AbstractHtmlExporter exporter = new HtmlExporterNG2();
                out = new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8);
                StreamResult result = new StreamResult(out);
                exporter.html(wordMLPackage, result, htmlSetting);
                success = true;
            }

        } catch (Docx4JException e) {
            _logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            _logger.error(e.getMessage(), e);
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                _logger.error(ex.getMessage(), ex);
            }
        }

        _logger.debug("convertion time: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * Walker JAXB qui produit un TXT lisible :
     *  - un \n par paragraphe, \n\n autour des titres,
     *  - cellules de tableau séparées par \t, lignes par \n,
     *  - champs Word résolus (instrText ignoré, résultat conservé),
     *  - références de footnote remplacées par [^N] et bloc final en fin de fichier.
     */
    private static final class DocxTextWalker {

        private final WordprocessingMLPackage pkg;
        private final Writer out;
        private final StringBuilder buf = new StringBuilder();
        private final Map<String, String> footnoteMarkerById = new LinkedHashMap<String, String>();
        private int nextNoteNumber = 1;
        /** Contenu de textboxes rencontre dans une run, a flusher apres le paragraphe courant. */
        private final List<CTTxbxContent> pendingTextboxes = new ArrayList<CTTxbxContent>();

        DocxTextWalker(WordprocessingMLPackage pkg, Writer out) {
            this.pkg = pkg;
            this.out = out;
        }

        void run() throws IOException {
            MainDocumentPart md = pkg.getMainDocumentPart();
            for (Object o : md.getContent()) {
                walkBlock(unwrap(o));
            }
            appendFootnotes(md);
            out.write(normalize(buf.toString()));
            out.flush();
        }

        private void walkBlock(Object o) {
            if (o == null) {
                return;
            }
            if (o instanceof P) {
                walkParagraph((P) o);
            } else if (o instanceof Tbl) {
                walkTable((Tbl) o);
            } else if (o instanceof SdtBlock) {
                SdtBlock sdt = (SdtBlock) o;
                if (sdt.getSdtContent() != null) {
                    for (Object child : sdt.getSdtContent().getContent()) {
                        walkBlock(unwrap(child));
                    }
                }
            } else if (o instanceof ContentAccessor) {
                for (Object child : ((ContentAccessor) o).getContent()) {
                    walkBlock(unwrap(child));
                }
            }
        }

        private void walkParagraph(P p) {
            boolean isHeading = false;
            String pStyleVal = null;
            String numId = null;
            String ilvl = null;
            PPr pPr = p.getPPr();
            if (pPr != null) {
                if (pPr.getPStyle() != null) {
                    pStyleVal = pPr.getPStyle().getVal();
                    if (pStyleVal != null) {
                        String lower = pStyleVal.toLowerCase();
                        if (lower.startsWith("heading") || lower.startsWith("title")) {
                            isHeading = true;
                        }
                    }
                }
                if (pPr.getNumPr() != null) {
                    if (pPr.getNumPr().getNumId() != null
                            && pPr.getNumPr().getNumId().getVal() != null) {
                        numId = pPr.getNumPr().getNumId().getVal().toString();
                    }
                    if (pPr.getNumPr().getIlvl() != null
                            && pPr.getNumPr().getIlvl().getVal() != null) {
                        ilvl = pPr.getNumPr().getIlvl().getVal().toString();
                    }
                }
            }

            String listPrefix = resolveListPrefix(pStyleVal, numId, ilvl);

            StringBuilder line = new StringBuilder();
            for (Object child : p.getContent()) {
                walkInline(child, line);
            }
            String text = trimRight(line.toString());

            if (text.isEmpty() && listPrefix == null) {
                buf.append('\n');
                flushPendingTextboxes();
                return;
            }
            if (isHeading) {
                buf.append('\n');
            }
            if (listPrefix != null) {
                buf.append(listPrefix);
            }
            buf.append(text).append('\n');
            if (isHeading) {
                buf.append('\n');
            }
            flushPendingTextboxes();
        }

        /** Vide la queue des textboxes : leur contenu est ecrit comme blocs apres le paragraphe. */
        private void flushPendingTextboxes() {
            if (pendingTextboxes.isEmpty()) {
                return;
            }
            List<CTTxbxContent> toFlush = new ArrayList<CTTxbxContent>(pendingTextboxes);
            pendingTextboxes.clear();
            for (CTTxbxContent tbx : toFlush) {
                for (Object child : tbx.getContent()) {
                    walkBlock(unwrap(child));
                }
            }
        }

        /** Trouve tous les CTTxbxContent dans un sous-arbre (Drawing, AlternateContent, ...). */
        private void collectTextboxes(Object subtree, final List<CTTxbxContent> dst) {
            try {
                TraversalUtil.CallbackImpl cb = new TraversalUtil.CallbackImpl() {
                    @Override
                    public List<Object> apply(Object o) {
                        Object v = (o instanceof JAXBElement) ? ((JAXBElement<?>) o).getValue() : o;
                        if (v instanceof CTTxbxContent) {
                            dst.add((CTTxbxContent) v);
                        }
                        return null;
                    }
                };
                new TraversalUtil(subtree, cb);
            } catch (Throwable t) {
                _logger.debug("textbox collection failed: " + t.getMessage());
            }
        }

        /**
         * Résout le préfixe d'une liste numérotée/à puces via le moteur de
         * numérotation docx4j (`Emulator`), qui consulte numbering.xml et la
         * chaîne de styles. Retourne null si le paragraphe n'est pas une liste.
         * Le moteur est stateful : appeler dans l'ordre des paragraphes.
         */
        private String resolveListPrefix(String pStyleVal, String numId, String ilvl) {
            if (numId == null && pStyleVal == null) {
                return null;
            }
            if (ilvl == null) {
                ilvl = "0";
            }
            try {
                Emulator.ResultTriple triple = Emulator.getNumber(pkg, pStyleVal, numId, ilvl);
                if (triple == null) {
                    return null;
                }
                String numString = triple.getNumString();
                if (numString != null && !numString.isEmpty()) {
                    return numString + " ";
                }
                String bullet = triple.getBullet();
                if (bullet != null && !bullet.isEmpty()) {
                    return bullet + " ";
                }
            } catch (Throwable t) {
                _logger.debug("list numbering resolution failed: " + t.getMessage());
            }
            return null;
        }

        private void walkTable(Tbl tbl) {
            buf.append('\n');
            for (Object trObj : tbl.getContent()) {
                Object trVal = unwrap(trObj);
                if (!(trVal instanceof Tr)) {
                    continue;
                }
                Tr tr = (Tr) trVal;
                StringBuilder rowLine = new StringBuilder();
                boolean firstCell = true;
                for (Object tcObj : tr.getContent()) {
                    Object tcVal = unwrap(tcObj);
                    if (!(tcVal instanceof Tc)) {
                        continue;
                    }
                    Tc tc = (Tc) tcVal;
                    StringBuilder cellBuf = new StringBuilder();
                    collectCellText(tc, cellBuf);
                    if (!firstCell) {
                        rowLine.append('\t');
                    }
                    firstCell = false;
                    rowLine.append(trimRight(cellBuf.toString()).replace('\n', ' '));
                }
                buf.append(rowLine).append('\n');
            }
            buf.append('\n');
        }

        private void collectCellText(ContentAccessor container, StringBuilder cellBuf) {
            for (Object child : container.getContent()) {
                Object cv = unwrap(child);
                if (cv instanceof P) {
                    StringBuilder pBuf = new StringBuilder();
                    for (Object pChild : ((P) cv).getContent()) {
                        walkInline(pChild, pBuf);
                    }
                    if (cellBuf.length() > 0) {
                        cellBuf.append(' ');
                    }
                    cellBuf.append(trimRight(pBuf.toString()));
                } else if (cv instanceof Tbl) {
                    StringBuilder nested = new StringBuilder();
                    flattenNestedTable((Tbl) cv, nested);
                    if (cellBuf.length() > 0) {
                        cellBuf.append(' ');
                    }
                    cellBuf.append(nested);
                } else if (cv instanceof ContentAccessor) {
                    collectCellText((ContentAccessor) cv, cellBuf);
                }
            }
        }

        private void flattenNestedTable(Tbl tbl, StringBuilder dst) {
            for (Object trObj : tbl.getContent()) {
                Object trVal = unwrap(trObj);
                if (!(trVal instanceof Tr)) {
                    continue;
                }
                boolean firstCell = true;
                for (Object tcObj : ((Tr) trVal).getContent()) {
                    Object tcVal = unwrap(tcObj);
                    if (!(tcVal instanceof Tc)) {
                        continue;
                    }
                    StringBuilder cellBuf = new StringBuilder();
                    collectCellText((Tc) tcVal, cellBuf);
                    if (!firstCell) {
                        dst.append(' ');
                    }
                    firstCell = false;
                    dst.append(trimRight(cellBuf.toString()).replace('\n', ' '));
                }
                dst.append(' ');
            }
        }

        /**
         * Inline content (à l'intérieur d'un paragraphe ou d'une cellule).
         * Conserve l'enveloppe JAXBElement pour distinguer w:t (texte) de
         * w:instrText (instruction de champ — à ignorer).
         */
        private void walkInline(Object node, StringBuilder dst) {
            if (node == null) {
                return;
            }
            String localName = null;
            Object value = node;
            if (node instanceof JAXBElement) {
                JAXBElement<?> je = (JAXBElement<?>) node;
                localName = je.getName().getLocalPart();
                value = je.getValue();
            }

            if (value instanceof R) {
                walkRun((R) value, dst);
            } else if (value instanceof Text) {
                if (!"instrText".equals(localName)) {
                    String s = ((Text) value).getValue();
                    if (s != null) {
                        dst.append(s);
                    }
                }
            } else if (value instanceof CTSimpleField) {
                // <w:fldSimple> : l'instruction est dans @instr (jetée),
                // les enfants sont le résultat affiché.
                for (Object child : ((CTSimpleField) value).getContent()) {
                    walkInline(child, dst);
                }
            } else if (value instanceof SdtRun) {
                SdtRun sr = (SdtRun) value;
                if (sr.getSdtContent() != null) {
                    for (Object child : sr.getSdtContent().getContent()) {
                        walkInline(child, dst);
                    }
                }
            } else if (value instanceof CTFtnEdnRef) {
                appendNoteMarker((CTFtnEdnRef) value, localName, dst);
            } else if (value instanceof ContentAccessor) {
                // Hyperlink, smartTag, etc. : descendre dans le contenu.
                for (Object child : ((ContentAccessor) value).getContent()) {
                    walkInline(child, dst);
                }
            }
        }

        private void walkRun(R r, StringBuilder dst) {
            for (Object o : r.getContent()) {
                String localName = null;
                Object value = o;
                if (o instanceof JAXBElement) {
                    JAXBElement<?> je = (JAXBElement<?>) o;
                    localName = je.getName().getLocalPart();
                    value = je.getValue();
                }

                if (value instanceof Text) {
                    if (!"instrText".equals(localName)) {
                        String s = ((Text) value).getValue();
                        if (s != null) {
                            dst.append(s);
                        }
                    }
                } else if (value instanceof Br) {
                    // Soft break <w:br/> au milieu d'un paragraphe : espace, pas \n.
                    // Sinon FileSegmentation2 (lecture ligne par ligne) coupe les phrases
                    // qui chevauchent un soft-break, et le rendu HTML cote QD UI s'effondre.
                    dst.append(' ');
                } else if (value instanceof R.Tab) {
                    dst.append('\t');
                } else if (value instanceof CTFtnEdnRef) {
                    appendNoteMarker((CTFtnEdnRef) value, localName, dst);
                } else if (value instanceof Drawing) {
                    // Texte des zones de texte (textbox) : a sortir comme blocs apres le paragraphe
                    collectTextboxes(value, pendingTextboxes);
                }
                // FldChar (begin/separate/end), R.NoBreakHyphen, R.SoftHyphen, etc. → ignorés
            }
        }

        private void appendNoteMarker(CTFtnEdnRef ref, String localName, StringBuilder dst) {
            if (ref.getId() == null) {
                return;
            }
            String scope = "endnoteReference".equals(localName) ? "EN" : "FN";
            String key = scope + "-" + ref.getId().toString();
            String marker = footnoteMarkerById.get(key);
            if (marker == null) {
                marker = "[^" + (nextNoteNumber++) + "]";
                footnoteMarkerById.put(key, marker);
            }
            dst.append(marker);
        }

        private void appendFootnotes(MainDocumentPart md) {
            if (footnoteMarkerById.isEmpty()) {
                return;
            }
            FootnotesPart fp = md.getFootnotesPart();
            if (fp == null) {
                return;
            }
            CTFootnotes ctf;
            try {
                ctf = fp.getJaxbElement();
            } catch (Exception e) {
                _logger.warn("Cannot read footnotes part: " + e.getMessage());
                return;
            }
            if (ctf == null) {
                return;
            }

            Map<String, CTFtnEdn> byId = new HashMap<String, CTFtnEdn>();
            for (CTFtnEdn ftn : ctf.getFootnote()) {
                if (ftn.getId() != null) {
                    byId.put(ftn.getId().toString(), ftn);
                }
            }

            buf.append('\n').append("--- Notes ---").append('\n');
            for (Map.Entry<String, String> e : footnoteMarkerById.entrySet()) {
                String key = e.getKey();
                if (!key.startsWith("FN-")) {
                    continue;
                }
                String id = key.substring(3);
                CTFtnEdn ftn = byId.get(id);
                if (ftn == null) {
                    continue;
                }
                StringBuilder noteBuf = new StringBuilder();
                noteBuf.append(e.getValue()).append(' ');
                for (Object child : ftn.getContent()) {
                    Object cv = unwrap(child);
                    if (cv instanceof P) {
                        for (Object pChild : ((P) cv).getContent()) {
                            walkInline(pChild, noteBuf);
                        }
                        noteBuf.append(' ');
                    }
                }
                buf.append(trimRight(noteBuf.toString())).append('\n');
            }
        }

        private static Object unwrap(Object o) {
            if (o instanceof JAXBElement) {
                return ((JAXBElement<?>) o).getValue();
            }
            return o;
        }

        private static String trimRight(String s) {
            int end = s.length();
            while (end > 0 && (s.charAt(end - 1) == ' ' || s.charAt(end - 1) == '\t')) {
                end--;
            }
            return end == s.length() ? s : s.substring(0, end);
        }

        private static String normalize(String s) {
            // Strip CR (Word peut emettre \r dans <w:t>) et le BOM/zero-width-no-break-space
            s = s.replace("\r", "");
            s = s.replace("﻿", "");
            s = s.replaceAll("[ \\t]+\\n", "\n");
            s = s.replaceAll("\\n{3,}", "\n\n");
            s = s.replaceAll("^\\n+", "");
            if (!s.endsWith("\n")) {
                s = s + "\n";
            }
            return s;
        }
    }
}

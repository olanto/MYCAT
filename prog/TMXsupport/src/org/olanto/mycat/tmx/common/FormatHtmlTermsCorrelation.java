/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.mycat.tmx.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.olanto.mysqd.util.Ref;
import org.olanto.util.Timer;

/**
 * test une recherche
 *
 */
public class FormatHtmlTermsCorrelation {

    private StringBuilder htmlResult;
    private int minfreqSO, minTermSO, lengthSO, minfreqTA;
    private int minNgram = 1;
    private float corlimit = 0.1f;

    public FormatHtmlTermsCorrelation() {
        NgramAndCorrelation.initIS();
        LangMap.init("BG " + "CS " + "DA " + "DE " + "EL " + "EN " + "ES " + "ET " + "FI " + "FR " + "GA " + "HU " + "IT " + "LT " + "LV " + "MT " + "NL " + "PL " + "PT " + "RO " + "SH " + "SK " + "SL " + "SV");


    }

    public String getHtmlResult(String termso, String langso, String termta, String langta) {
        Timer t1 = new Timer("total time");
        int freqso = NgramAndCorrelation.getFrequency(termso, langso, langta);
        setHtmlHeader("<p>Result from: \"" + termso + "\" " + langso + " to \"" + termta + "\" " + langta
                + ", Term frequency: " + freqso + "</p>\n");

        if (freqso != 0) {

            Ref targetRef = new Ref("", termta, 1, 1);
            List<Ref> ref = new ArrayList<>();
            ref.add(targetRef);
            List<ItemsCorrelation> list = new ArrayList<>();

            NgramAndCorrelation.NB_EXAMPLES = 10;
            for (int i = 0; i < ref.size(); i++) { // pour chaque n-gram
                list.add(NgramAndCorrelation.correlationObj(termso, ref.get(i).ngram, langso, langta));
            }
            try {
                Collections.sort(list);
            } catch (Exception ex) {
                Logger.getLogger(FormatHtmlTermsCorrelation.class.getName()).log(Level.SEVERE, null, ex);
            }
            int countskip = 0;
            int count = 0;
            addhtml("<h2>Correlation</h2>\n");
            addhtml("<table>\n");
            addhtml("<tr><th width=\"60%\">Correlation with the source term: " + termso
                    + "</th><th style=\"width:100px\">Cor. %</th>"
                    + "</th><th style=\"width:100px\">In " + langso + "</th>"
                    + "</th><th style=\"width:100px\">In " + langta + "</th>"
                    + "</th><th style=\"width:100px\">In both</th></tr>\n");
            for (ItemsCorrelation item : list) { // pour chaque n-gram
                addhtml("<tr>\n");
                addhtml("<td width=\"60%\">" + item.termta + "</td>"
                        + "<td style=\"width:100px\">" + (int) (item.cor * 100) + "%</td>\n"
                        + "<td style=\"width:100px\">" + item.n1 + "</td>\n"
                        + "<td style=\"width:100px\">" + item.n2 + "</td>\n"
                        + "<td style=\"width:100px\">" + item.n12 + "</td>\n");
                addhtml("</tr>\n");
                addhtml("</table>\n");
                addhtml("<table class=\"ex\">\n");
                for (int i = 0; i < item.examples.length; i++) {
                    addhtml("<tr>\n");
                    addhtml("<td  class=\"ex0\">" + "</td>"
                            + "<td class=\"ex1\">" + emphase(item.examples[i][0], termso) + "</td>"
                            + "<td  class=\"ex2\">" + emphase(item.examples[i][1], item.termta) + "</td>\n");
                    addhtml("</tr>\n");
                    addhtml("</table>\n");
                    addhtml("<table>\n");
                }

                count++;
            }
            addhtml("</table>\n");

            addhtml("<p>total time: " + t1.getstop() + "millisec</p>\n");
            setHtmlFooter();

        }
        return htmlResult.toString();
    }

    public void setHtmlHeader(String title) {
        htmlResult = new StringBuilder();
        htmlResult.append("<h1>"
                + title
                + "</h1>" + "<hr/>");
    }

    public void setHtmlFooter() {
    }

    public void addhtml(String s) {
        htmlResult.append(s);
    }

    public String emphase(String s, String toEmphase) {
        String REGEX_BEFORE_TOKEN = "([^\\p{L}\\p{N}]|^)";
        String REGEX_AFTER_TOKEN = "([^\\p{L}\\p{N}]|$)";

        int start, length = toEmphase.length();
        String regex, newtoEmphase = "";
        Pattern p;
        Matcher m;
//        regex = REGEX_BEFORE_TOKEN + Pattern.quote(toEmphase) + REGEX_AFTER_TOKEN;
        p = Pattern.compile(toEmphase, Pattern.CASE_INSENSITIVE);
        m = p.matcher(s);
        if (m.find()) {
            start = m.start();
            newtoEmphase = s.substring(start, start + length);
            return s.replace(newtoEmphase, "<em>" + newtoEmphase + "</em>");
        }
        return s.replace(toEmphase, "<em>" + toEmphase + "</em>");
    }

    public String linkTo(String query, String langso, String langta) {
        String encodedURL = "encodingError";
        try {
            encodedURL = "how2say?query=" + URLEncoder.encode(query, "UTF-8") + "&langso=" + langso + "&langta=" + langta;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FormatHtmlTermsCorrelation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "<a class=\"reverse\" href=\"" + encodedURL + "\">" + query + "</a>";
    }
}

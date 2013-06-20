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
package org.olanto.mysqd.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.olanto.mysqd.server.MySelfQuoteDetection;


/**
 * calcul des statitisques pour les références
 */
public class RefStatistic {

    private Ref[] alldoc;  // la liste des documents
    

    public RefStatistic(List<Ref> refList) {
        alldoc=new Ref[refList.size()];
        for (int i = 0; i < refList.size(); i++) {
            alldoc[i]=refList.get(i);
        }
        Arrays.sort(alldoc, new RefComparator());
    }

        public String getHeaderSat(String fileName,int minlen,int minocc) {
        StringBuilder res = new StringBuilder("");
         res.append("</p>");
        res.append("<span style=\"font-family:Arial; font-size:12px;\">");
      res.append("____________________________________________________________________________________");
        res.append("</p>");
        res.append("</p>");
        res.append("</p>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_1")+" " + fileName +MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_1"));
        res.append("</p>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_2")+" " + alldoc.length);
        res.append("</p>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_3")+" " + minlen);
        res.append("</p>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_4")+" " + minocc);
        res.append("</p>");
          return res.toString();
    }

 
    public String getStatByRef(String fileName,int minlen,int minocc) {
        StringBuilder res = new StringBuilder(getHeaderSat( fileName, minlen, minocc));
        res.append("</p><table BORDER=\"1\">\n");
        res.append("<caption><b>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_5")+"</b></caption>\n"); // titre
        res.append("<tr>\n" //entête
                + "<th>#</th>\n"
                + "<th>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_6")+"</th>\n"
                 + "<th>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_7")+"</br>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_8")+"</th>\n"
               + "<th>"+MySelfQuoteDetection.MsgManager.get("widget.sqd.MSG_9")+"</th>\n"
                + "</tr>\n");
        for (int i = 0; i < alldoc.length; i++) {
            Ref current = alldoc[i];
            res.append("<tr>\n");
            res.append("<td>" + (i+1) + "</td>\n");  // numéro de la ref
            res.append("<td>" 
                    +"<a href=\"#"+current.docref+"\" onClick=\"return gwtnav(this);\">"
                    +current.ngram + "</a></td>\n");  // la ref
            res.append("<td>"+ current.nbocc +"</td>"); // nbr d'occurences
             res.append("<td>" + current.len + "</td>\n");  //la longueur
           res.append("</tr>\n");

        }
        res.append("</table>\n");
        res.append("</span>\n");
        return res.toString();
    }

}

/**
 * ********
 * Copyright © 2010-2016 Olanto Foundation Geneva
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
package org.olanto.idxvli.server;

import java.io.Serializable;

/**
 *
 * @author simple
 */
public class Reference implements Serializable {

    private Integer localIDX;
    private String textOfRef;
    private Integer startIDX;
    private Integer endIDX;
    private String tag;
    private String color;
    private Integer globalIDX;
    private Integer finalPos;
    private String openingText;
    private String closingText;

    public Reference() {
        this.localIDX = 0;
        this.textOfRef = "";
        this.startIDX = 0;
        this.endIDX = 0;
        this.tag = "";
        this.color = "";
        this.globalIDX = 0;
        this.finalPos = 0;
        this.openingText = "";
        this.closingText = "";
    }

    public void setLocalIDX(Integer localIDX) {
        this.localIDX = localIDX;
    }

    public Integer getLocalIDX() {
        return this.localIDX;
    }

    public void setTextOfRef(String textOfRef) {
        this.textOfRef = textOfRef;
    }

    public String getTextOfRef() {
        return this.textOfRef;
    }

    public void setStartIDX(Integer startIDX) {
        this.startIDX = startIDX;
    }

    public Integer getStartIDX() {
        return this.startIDX;
    }

    public void setEndIDX(Integer endIDX) {
        this.endIDX = endIDX;
    }

    public Integer getEndIDX() {
        return this.endIDX;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public void setGlobalIDX(Integer globalIDX) {
        this.globalIDX = globalIDX;
    }

    public Integer getGlobalIDX() {
        return this.globalIDX;
    }

    public void setFinalPos(Integer finalPos) {
        this.finalPos = finalPos;
    }

    public Integer getFinalPos() {
        return this.finalPos;
    }

    public void setOpeningText(String openingText) {
        this.openingText = openingText;
    }

    public String getOpeningText() {
        return this.openingText;
    }

    public void setClosingText(String closingText) {
        this.closingText = closingText;
    }

    public String getClosingText() {
        return this.closingText;
    }
}

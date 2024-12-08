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

import java.util.Collections;
import java.util.List;

/**
 *
 * @author simple
 */
public class Reference implements Comparable<Reference> {

    private Integer localIDX;
    private String textOfRef;
    private String refTextInDoc;
    private Integer startIDX;
    private Integer effectiveStartIDX;
    private Integer endIDX;
    private String tag;
    private String color;
    private Integer globalIDX;
    private List<String> referencedDocs;

    /**
     * create a new ref
     */
    public Reference() {
        this.localIDX = 0;
        this.textOfRef = "";
        this.startIDX = 0;
        this.effectiveStartIDX = 0;
        this.endIDX = 0;
        this.tag = "";
        this.color = "";
        this.globalIDX = 0;
        this.referencedDocs = Collections.emptyList();
    }

    /**
     *
     * @param localIDX
     */
    public void setLocalIDX(Integer localIDX) {
        this.localIDX = localIDX;
    }

    /**
     *
     * @return
     */
    public Integer getLocalIDX() {
        return this.localIDX;
    }

    /**
     *
     * @param textOfRef
     */
    public void setTextOfRef(String textOfRef) {
        this.textOfRef = textOfRef;
    }

    /**
     *
     * @return
     */
    public String getTextOfRef() {
        return this.textOfRef;
    }
    
    /**
     *
     * @param refTextInDoc
     */
    public void setRefTextInDoc(String refTextInDoc) {
        this.refTextInDoc = refTextInDoc;
    }

    /**
     *
     * @return
     */
    public String getRefTextInDoc() {
        return this.refTextInDoc;
    }

    /**
     *
     * @param startIDX
     */
    public void setStartIDX(Integer startIDX) {
        this.startIDX = startIDX;
    }

    /**
     *
     * @return
     */
    public Integer getStartIDX() {
        return this.startIDX;
    }

    /**
     *
     * @param effectiveStartIDX
     */
    public void setEffectiveStartIDX(Integer effectiveStartIDX) {
        this.effectiveStartIDX = effectiveStartIDX;
    }

    /**
     *
     * @return
     */
    public Integer getEffectiveStartIDX() {
        if (this.effectiveStartIDX <= this.startIDX) {
            return this.startIDX;
        }
        return this.effectiveStartIDX;
    }

    /**
     *
     * @param endIDX
     */
    public void setEndIDX(Integer endIDX) {
        this.endIDX = endIDX;
    }

    /**
     *
     * @return
     */
    public Integer getEndIDX() {
        return this.endIDX;
    }

    /**
     *
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     *
     * @return
     */
    public String getTag() {
        return this.tag;
    }

    /**
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return
     */
    public String getColor() {
        return this.color;
    }

    /**
     *
     * @param globalIDX
     */
    public void setGlobalIDX(Integer globalIDX) {
        this.globalIDX = globalIDX;
    }

    /**
     *
     * @return
     */
    public Integer getGlobalIDX() {
        return this.globalIDX;
    }

    /**
     *
     * @return
     */
    public List<String> getReferencedDocs() {
        return this.referencedDocs;
    }

    /**
     *
     * @param referencedDocs
     */
    public void setReferencedDocs(List<String> referencedDocs) {
        this.referencedDocs = referencedDocs;
    }

    @Override
    public int compareTo(Reference o) {
        return this.startIDX - o.startIDX;
    }

    /**
     *
     * @param o
     * @return
     */
    public boolean SameAs(Reference o) {
        return ((this.startIDX == o.startIDX) && (this.endIDX == o.endIDX));
    }
}

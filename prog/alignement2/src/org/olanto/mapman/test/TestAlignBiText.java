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
package org.olanto.mapman.test;

import org.olanto.mapman.server.AlignBiText;

/**
 * Test de AlignBiText
 *
 *
 *
 */
public class TestAlignBiText {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        //AlignBiText align = new AlignBiText("EN/small-collection¦WIPO¦WO_GA¦wo_ga_31_1.doc.txt", "EN", "EN", null, 53, 28);
        AlignBiText align = new AlignBiText("EN/Glossaries¦External¦Product classification¦Documents¦Customs Tariffs¦Philippines¦Philippines2003.pdf.txt", "EN", "EN", null, 53, 28, false);
        //align.dump();
    }
}

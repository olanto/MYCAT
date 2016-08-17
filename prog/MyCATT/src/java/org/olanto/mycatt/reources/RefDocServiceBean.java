/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.olanto.mycatt.reources;

import javax.ejb.Singleton;
import org.olanto.idxvli.server.IndexService_MyCat;

/**
 *
 * @author simple
 */
@Singleton
public class RefDocServiceBean {

    // name field
    private IndexService_MyCat is;

    public IndexService_MyCat getContent() {
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        return is;
    }
}

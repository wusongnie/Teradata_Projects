package org.krylov.lib.models;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: krylov
 * Date: 05.07.13
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class XlsRow {
    public LinkedHashMap getlHashMap() {
        return lHashMap;
    }

    public void setlHashMap(LinkedHashMap lHashMap) {
        this.lHashMap = lHashMap;
    }

    public LinkedHashMap lHashMap = new LinkedHashMap();
}

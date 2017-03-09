/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestione.bilancio.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author petitpapa
 */
public class DocumentLoaderFactory {

    static Map<String, DocumentManager> reader_map = new HashMap<>();

    static {
        reader_map.put("txt", new TextDocumentManager());
        reader_map.put("cvs", new CSVDocumentManager());
        reader_map.put("xls", new ExcelDocumentReader());
    }

    public static DocumentManager createDocument(String type) {
        return reader_map.get(type);
    }

}

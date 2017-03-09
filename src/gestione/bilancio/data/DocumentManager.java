/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestione.bilancio.data;

import gestione.bilancio.model.Bilancio;
import java.io.File;
import javafx.scene.control.TableView;

/**
 *
 * @author petitpapa
 */
public interface DocumentManager {
    public void read(File selectedFile, TableView<Bilancio> table);
    public void save(File file, TableView<Bilancio> data);
}

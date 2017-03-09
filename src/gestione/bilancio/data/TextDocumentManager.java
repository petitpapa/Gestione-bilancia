
package gestione.bilancio.data;

import gestione.bilancio.model.Bilancio;
import gestione.bilancio.utils.DateUtility;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author petitpapa
 */
public class TextDocumentManager implements DocumentManager {

    private static final String SPACE_SEPARATOR = " ";

    @Override
    public void read(File selectedFile, TableView<Bilancio> table) {
        ObservableList<Bilancio> list = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = null;
                if (!line.isEmpty())
                    tokens = line.split(SPACE_SEPARATOR);
                Bilancio bilancio = new Bilancio();

                if (tokens != null) {
                    bilancio.setDate(new DateUtility().fromString(tokens[0]));
                    bilancio.setDescription(tokens[1]);
                    bilancio.setAmmontare(Double.parseDouble(tokens[2]));
                    bilancio.setTipo(Bilancio.Tipo.valueOf(tokens[3]));
                    list.add(bilancio);
                }
            }
            table.setItems(list);
            table.refresh();
        } catch (IOException ex) {
            Logger.getLogger(TextDocumentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save(File file, TableView<Bilancio> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            data.getItems().stream().forEach(e -> {
                try {
                    bw.write(new StringJoiner(SPACE_SEPARATOR).add(e.getDate().toString())
                            .add(e.getDescription()).add(String.valueOf(e.getAmmontare()))
                            .add(e.getTipo().name()).toString());
                    bw.write(System.getProperty("line.separator"));

                } catch (IOException ex) {
                    Logger.getLogger(TextDocumentManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(TextDocumentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

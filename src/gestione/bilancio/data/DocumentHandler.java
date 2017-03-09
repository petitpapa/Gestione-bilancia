
package gestione.bilancio.data;

import gestione.bilancio.model.Bilancio;
import java.io.File;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author petitpapa
 */
public class DocumentHandler {

    private FileChooser fileChooser;
    private Stage stage;

    public DocumentHandler() {
        configureFileChooser();
    }

    private void configureFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xls"),
                new FileChooser.ExtensionFilter("Csv Files", "*.csv"));

    }

    public void load(TableView<Bilancio> table) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            int commaIndex = selectedFile.getName().lastIndexOf(".");
            String extension = selectedFile.getName().substring(commaIndex + 1);
            DocumentLoaderFactory.createDocument(extension).read(selectedFile, table);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void save(TableView<Bilancio> table) {
        File fileToSave = fileChooser.showSaveDialog(stage);
        if (fileToSave != null) {
            int commaIndex = fileToSave.getName().lastIndexOf(".");
            String extension = fileToSave.getName().substring(commaIndex + 1);
            DocumentLoaderFactory.createDocument(extension).save(fileToSave, table);
        }
    }

}

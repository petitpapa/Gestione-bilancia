
package gestione.bilancio.data;
import gestione.bilancio.model.Bilancio;
import java.io.File;
import javafx.scene.control.TableView;

/**
 *
 * @author petitpapa
 */
public class ExcelDocumentReader implements DocumentManager{

    @Override
    public void read(File selectedFile, TableView<Bilancio> table) {
        
    }

    @Override
    public void save(File file, TableView<Bilancio> data) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}

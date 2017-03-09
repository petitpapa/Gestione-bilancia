
package gestione.bilancio;

import gestione.bilancio.controller.BilancioController;
import gestione.bilancio.data.DataFilter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author petitpapa
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        DataFilter filter = new DataFilter();
        BilancioController controller = new BilancioController(primaryStage,filter);
        
        Scene scene = new Scene(controller.getRoot(), 800, 550);
        scene.getStylesheets().add("resources/style.css");
        primaryStage.setTitle("Gestione Bilancio");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

package gestione.bilancio.controller;

import gestione.bilancio.data.DataFilter;
import gestione.bilancio.data.DocumentHandler;
import gestione.bilancio.data.DocumentPrinter;
import gestione.bilancio.model.Bilancio;
import gestione.bilancio.model.PeriodoTemp;
import static gestione.bilancio.model.PeriodoTemp.DATA_CORRENTE;
import java.time.LocalDate;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author petitpapa
 */
public class BilancioController {

    private final VBox root = new VBox();
    private final Label headTile = new Label("Gestione Bilancio");
    private final TableView<Bilancio> table = new TableView<>();
    private Label totalLbl = new Label();
    private PeriodoTemp searchFilter;

    private final DataFilter data;
    private Button addButton;
    private final Stage stage;
    private DocumentHandler doc = new  DocumentHandler();

    public BilancioController(Stage stage, DataFilter data) {
        this.stage = stage;
        this.data = data;
        init();
        loadData();
        renderView();
    }

    private void init() {
        headTile.setId("titleHeader");
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        totalLbl.textProperty().bind(TotalHandler.totalProperty.asString());
    }

    private void loadData() {
        table.setMinWidth(700);
        table.setPrefWidth(700);
        table.setMaxWidth(700);
        table.setItems(data.getItemsBy(DATA_CORRENTE));
        searchFilter = DATA_CORRENTE;
    }

    private void renderView() {
        TableColumn<Bilancio, LocalDate> dateCol = new TableColumn<>("Data");
        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Bilancio, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setMinWidth(300);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Bilancio, LocalDate> ammontareCol = new TableColumn<>("Ammontare");

        ammontareCol.setMinWidth(100);
        ammontareCol.setCellValueFactory(new PropertyValueFactory<>("ammontare"));

        TableColumn action = new TableColumn("Action");
        action.setMinWidth(200);
        action.setCellFactory((Object param) -> new ButtonCell(table));

        table.getColumns().addAll(dateCol, descriptionCol, ammontareCol, action);
        pack();
    }

    private void pack() {

        HBox addPane = new HBox(10);
        addButton = new Button("Add...");
        addButton.setId("addBtn");

        Button saveAsBtn = new Button("SAVE AS...");
        saveAsBtn.setId("addBtn");
        saveAsBtn.setOnAction(e -> saveDocument());

        Button loadFileBtn = new Button("LOAD...");
        loadFileBtn.setId("addBtn");
        loadFileBtn.setOnAction(e -> loadDocument());
        
        Button printBtn = new Button("PRINT JOB");
        printBtn.setId("addBtn");
        printBtn.setOnAction(e -> printItems());
        
        addPane.setPadding(new Insets(15, 12, 15, 12));
        addPane.getChildren().addAll(addButton, saveAsBtn, loadFileBtn, printBtn);
        root.getChildren().addAll(headTile, addTotalPane(), table, addPane);
        addButton.setOnAction(e -> handleSaving());
    }

    private void loadDocument() {
       doc.setStage(stage);
       doc.load(table);
        updateTable();
    }

    private void updateTable() {
        table.refresh();
    }

    private void saveDocument() {
       doc.setStage(stage);
       doc.save(table);
    }

    public VBox getRoot() {
        return root;
    }
    
    private void printItems(){
        final DocumentPrinter documentPrinter = new DocumentPrinter(table);
        FutureTask<Boolean> futureTask = new FutureTask<>(documentPrinter);
        if(!futureTask.isDone()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Printer Error");
            alert.setContentText("The printer is not available");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Printer Success");
            alert.setContentText("The items has been printed!");
            alert.showAndWait();
        }
    }

    void updateItem(Bilancio bilancio, int rowIndex) {
        updateTable();
    }

    private void handleSaving() {
        new UpdateItem(data, table).showPane();
        updateTable();
    }

    private VBox addTotalPane() {
        VBox totalPane = new VBox(10);
        TotalHandler.setTotal(table);

        HBox searchPane = new HBox(2);
        TextField searchTxt = new TextField();
        searchTxt.setPromptText("Search...");
        searchPane.getChildren().add(searchTxt);
        searchTxt.setFocusTraversable(false);
        searchTxt.textProperty().addListener((obs, oldValue, newValue) -> filterItems(searchFilter, oldValue, newValue));

        Label visitItemLbl = new Label("Visualizza dati per: ");
        visitItemLbl.setStyle("-fx-font-weight:bold;-fx-font-size:1.2em");
        ChoiceBox<PeriodoTemp> timeChoices = new ChoiceBox<>(FXCollections.observableArrayList(PeriodoTemp.values()));
        timeChoices.setValue(PeriodoTemp.DATA_CORRENTE);
        searchFilter = DATA_CORRENTE;
        timeChoices.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, old, newValue) -> getDataBy(newValue.intValue()));
        HBox leftPanel = new HBox(10, visitItemLbl, timeChoices);
        Label bilancioLblTitlt = new Label("total bilancio: ");
        bilancioLblTitlt.setStyle("-fx-font-weight:bold;-fx-font-size:1.2em");
        HBox totBox = new HBox(10, bilancioLblTitlt, totalLbl);
        totBox.setAlignment(Pos.CENTER_LEFT);
        HBox hData = new HBox(60, totBox, searchPane, leftPanel);
        totalPane.getChildren().addAll(hData);
        return totalPane;
    }

    private void getDataBy(int selectedPeriod) {
        table.setItems(data.getItemsBy(PeriodoTemp.values()[selectedPeriod]));
        searchFilter = PeriodoTemp.values()[selectedPeriod];
        TotalHandler.setTotal(table);
        updateTable();
    }

    private void filterItems(PeriodoTemp currentFilter, String oldValue, String newValue) {
        if (oldValue != null && newValue.length() < oldValue.length())
            table.setItems(data.getItemsBy(currentFilter));

        ObservableList<Bilancio> subEntries = FXCollections.observableArrayList();
        subEntries.addAll(table.getItems().stream()
                .filter(e -> e.getDescription().toLowerCase().contains(newValue.toLowerCase()))
                .collect(Collectors.toList()));
        table.setItems(subEntries);
        updateTable();
    }

    public class ButtonCell extends TableCell {

        Button editBtn = new Button("Edit");
        Button removeBtn = new Button("Remove");

        HBox pane = new HBox();

        public ButtonCell(TableView table) {
            pane.setSpacing(10);
            pane.setPadding(new Insets(0, 20, 0, 20));
            pane.getChildren().addAll(editBtn, removeBtn);
            styleButtons();
            removeBtn.setOnAction((e) -> removeRow(table, e));
            editBtn.setOnAction((e) -> getColumnIndex(table, e));
        }

        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty)
                setGraphic(pane);
        }

        private void removeRow(TableView<Bilancio> table, ActionEvent event) {
            table.getItems().remove(table.getItems().get(ButtonCell.this.getIndex()));
            table.refresh();
            TotalHandler.setTotal(table);
        }

        private void getColumnIndex(TableView table, ActionEvent e) {
            Bilancio b = (Bilancio) table.getItems().get(ButtonCell.this.getIndex());
            showBilancio(b);
        }

        private void showBilancio(Bilancio newValue) {
            new UpdateItem(newValue, data, table).showPane();

        }

        private void styleButtons() {
            editBtn.setId("edit");
            editBtn.getStyleClass().add("action");
            removeBtn.getStyleClass().add("action");
            removeBtn.setId("remove");
        }
    }

}

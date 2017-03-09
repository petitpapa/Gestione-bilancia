package gestione.bilancio.controller;

import gestione.bilancio.data.DataFilter;
import gestione.bilancio.model.Bilancio;
import gestione.bilancio.utils.DateUtility;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author petitpapa
 */
public class UpdateItem {

    private Bilancio bilancio;
    private TextField txtDate;
    private TextField txtDescription;
    private TextField txtammontare;
    private Label descriptionError;
    private Label amountError;
    private Label dateError;
    private DataFilter factory;
    private TableView<Bilancio> table;
    private double amount;
    private Stage stage;
    private ChoiceBox<Bilancio.Tipo> tipoBilancio;
    private Label emptyChoice;

    UpdateItem(Bilancio newValue, DataFilter factory, TableView<Bilancio> table) {
        this.bilancio = newValue;
        this.factory = factory;
        this.table = table;
    }

    UpdateItem(DataFilter factory, TableView<Bilancio> table) {
        this.factory = factory;
        this.table = table;
    }

    public void showPane() {
        stage = new Stage();
        Scene scene = new Scene(buildPane());
        scene.getStylesheets().add("resources/style.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Modifica bilancio");
        stage.show();
        stage.setOnCloseRequest(e -> {
            e.consume();
            stage.close();
        });
    }

    private VBox buildPane() {
        return new VBox(10, createDatePane(),
                createPaneDescription(),
                createAmountPane(),
                createTypePane(),
                createButtonPane()
        );
    }

    private Button createButtonPane() {
        Button btnSave = new Button("Save");

        btnSave.setOnAction((e) -> updateFields(e));

        btnSave.setId("addBtn");

        HBox paneButton = new HBox(btnSave);
        paneButton.setPadding(new Insets(10, 0, 0, 20));
        paneButton.setAlignment(Pos.BOTTOM_RIGHT);
        return btnSave;
    }

    private void updateFields(ActionEvent e) throws NumberFormatException {

        if (isValidAllFields()) {
            if (bilancio == null)
                bilancio = new Bilancio();
            if (notAmount() && tableContainsDuplicateDescription())
                updateAmount();

            bilancio.setDate(LocalDate.parse(txtDate.getText()));
            bilancio.setDescription(txtDescription.getText());
            bilancio.setAmmontare(amount);
            bilancio.setTipo(tipoBilancio.getValue());
            if (!tableContainsDuplicateDescription())
                factory.getList().add(bilancio);
            TotalHandler.updateTotalSumForLabel(bilancio);
            table.refresh();

            e.consume();
            stage.close();
        }
    }

    private boolean notAmount() {
        return bilancio.getAmmontare() == 0;
    }

    private void updateAmount() throws NumberFormatException {
        Bilancio bil = factory.getList().stream()
                .filter(bilancio -> bilancio.getDescription().equalsIgnoreCase(txtDescription.getText())).findFirst().get();

        bil.setAmmontare(bil.getAmmontare() + Double.parseDouble(txtammontare.getText()));
    }

    private HBox createAmountPane() {
        Label lblAmmontare = new Label("Ammontare:");
        lblAmmontare.setMinWidth(140);
        lblAmmontare.setAlignment(Pos.BOTTOM_RIGHT);
        txtammontare = new TextField();
        if (bilancio != null)
            txtammontare.setText(String.valueOf(bilancio.getAmmontare()));
        txtammontare.setMinWidth(200);
        txtammontare.setMaxWidth(200);
        amountError = new Label();
        HBox paneAmmontare = new HBox(20, lblAmmontare, txtammontare, amountError);
        paneAmmontare.setPadding(new Insets(10));
        return paneAmmontare;
    }

    private HBox createPaneDescription() {
        Label lbldesription = new Label("Descrizione del bilancio:");
        lbldesription.setMinWidth(140);
        lbldesription.setAlignment(Pos.BOTTOM_RIGHT);
        txtDescription = new TextField();
        if (bilancio != null)
            txtDescription.setText(bilancio.getDescription());
        txtDescription.setMinWidth(200);
        txtDescription.setMaxWidth(200);
        descriptionError = new Label();
        descriptionError.setMinWidth(240);
        descriptionError.setVisible(false);
        HBox paneDescription = new HBox(20, lbldesription, txtDescription, descriptionError);
        paneDescription.setPadding(new Insets(10));
        return paneDescription;
    }

    private HBox createDatePane() {
        Label dateLabel = new Label("Data bilancio:");
        dateLabel.setMinWidth(140);
        dateLabel.setAlignment(Pos.BOTTOM_RIGHT);
        txtDate = new TextField();
        if (bilancio != null)
            txtDate.setText(bilancio.getDate().toString());
        txtDate.setMinWidth(200);
        txtDate.setMaxWidth(200);
        dateError = new Label();
        HBox paneDate = new HBox(20, dateLabel, txtDate, dateError);
        paneDate.setPadding(new Insets(10));
        return paneDate;
    }

    private HBox createTypePane() {
        Label choiceLbl = new Label("Il tipo di bilancio");
        emptyChoice = new Label();
        choiceLbl.setMinWidth(140);
        choiceLbl.setAlignment(Pos.BOTTOM_RIGHT);
        tipoBilancio = new ChoiceBox<>(FXCollections.observableArrayList(Bilancio.Tipo.SPESA, Bilancio.Tipo.ENTRATA));
        if (bilancio != null)
            tipoBilancio.setValue(bilancio.getTipo());
        tipoBilancio.setPadding(new Insets(0, 0, 0, 50));
        return new HBox(30, choiceLbl, tipoBilancio, emptyChoice);
    }

    private boolean isValidAllFields() {
        resetAllErrorsDescription();
        try {
            amount = Double.parseDouble(txtammontare.getText());
        } catch (NumberFormatException e) {
            amountError.setText("X The amount should be Double not a String");
            amountError.getStyleClass().add("error");
            return false;
        }
        if (new DateUtility().fromString(txtDate.getText()) == null) {
            dateError.setText("X the date should be yyyy-dd-mm");
            dateError.getStyleClass().add("error");
            return false;
        } else if (txtDescription.getText() == null || txtDescription.getText().length() == 0) {
            descriptionError.setText("X The description should not be empty");
            descriptionError.getStyleClass().add("error");
            descriptionError.setVisible(true);

            return false;
        } else if (tableContainsDuplicateDescription()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Duplicate item");
            confirmation.setContentText("Confirm to update the duplicate item!");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK))
                return true;
            else if (result.isPresent() && (result.get() == ButtonType.CANCEL)) {
                confirmation.close();
                stage.close();
            }
            return false;
        } else if (tipoBilancio.getValue() == null) {
            emptyChoice.setText("! Choose a type");
            emptyChoice.getStyleClass().add("error");
            return false;
        }
        return true;
    }

    private void resetAllErrorsDescription() {
        dateError.setText("");
        dateError.getStyleClass().remove("error");
        descriptionError.setText("");
        descriptionError.getStyleClass().remove("error");
        amountError.setText("");
        amountError.getStyleClass().remove("error");
    }

    private boolean tableContainsDuplicateDescription() {
        return factory.getList().stream()
                .filter(e -> e.getDescription().equalsIgnoreCase(txtDescription.getText()))
                .count() >= 1;
    }

}

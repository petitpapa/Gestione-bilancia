package gestione.bilancio.data;

import gestione.bilancio.model.Bilancio;
import gestione.bilancio.model.PeriodoTemp;
import static gestione.bilancio.model.PeriodoTemp.ALL;
import static gestione.bilancio.model.PeriodoTemp.DATA_CORRENTE;
import static gestione.bilancio.model.PeriodoTemp.MESE;
import static gestione.bilancio.model.PeriodoTemp.SETTIMANA;
import gestione.bilancio.utils.DateUtility;
import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author petitpapa
 */
public class DataFilter {

    ObservableList<Bilancio> list = FXCollections.observableArrayList();
    private LocalDate fromDate;
    private LocalDate toDate;

    public DataFilter() {
        init();
    }

    private void init() {

        Bilancio b = new Bilancio("Affito", 500, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Mutuo", 200, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Luce", 1290, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Gas", 90, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Telefono", 15, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Acqua", 27, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Assicurazione", 45, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("spese auto", 112, Bilancio.Tipo.SPESA);
        list.add(b);
        b = new Bilancio("Stipendio", 1290, Bilancio.Tipo.ENTRATA);
        list.add(b);
        b = new Bilancio("Rendite immobiliare", 3000, Bilancio.Tipo.ENTRATA);
        list.add(b);
    }

    public ObservableList<Bilancio> getList() {

        return list;
    }

    public ObservableList<Bilancio> getItemsBy(PeriodoTemp selectedPeriod) {

        Collections.sort(list, (b1, b2) -> b1.getDate().compareTo(b2.getDate()));
        if (selectedPeriod.equals(DATA_CORRENTE))
            return filterItems(LocalDate.now().minusDays(1));
        else if (selectedPeriod.equals(SETTIMANA))
            return filterItems(LocalDate.now().minusWeeks(1));
        else if (selectedPeriod.equals(MESE))
            return filterItems(LocalDate.now().minusMonths(1));
        else if (selectedPeriod.equals(PeriodoTemp.ANNO))
            return filterItems(LocalDate.now().minusMonths(1));
        else if (selectedPeriod.equals(ALL))
            return list;
        else if (selectedPeriod.equals(PeriodoTemp.INTERVALLO_TEMPO)) {
            addDatePicker();
            return itemsByInterval();
        }
        return null;
    }

    private ObservableList<Bilancio> itemsByInterval() {
        return FXCollections.observableArrayList(list.stream()
                .filter(e -> e.getDate().isAfter(fromDate.minusDays(1)))
                .filter(e -> e.getDate().isBefore(toDate.plusDays(1)))
                .collect(Collectors.toList()));
    }

    private ObservableList<Bilancio> filterItems(LocalDate weekBefore) {
        return FXCollections.observableArrayList(list.stream()
                .filter(e -> e.getDate().isAfter(weekBefore))
                .filter(e -> e.getDate().isBefore(LocalDate.now().plusDays(1)))
                .collect(Collectors.toList()));
    }

    private void addDatePicker() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox();

        DatePicker from = new DatePicker();
        from.setShowWeekNumbers(false);
        from.setConverter(new DateUtility());

        DatePicker to = new DatePicker();
        to.setShowWeekNumbers(false);
        to.setConverter(new DateUtility());

        HBox datePicker = new HBox(20, from, to);

        Button btn = new Button("Ok");

        btn.setStyle("-fx-padding: 5px 4px 10px 4px;"
                + "    -fx-background-radius: 4px;"
                + "    -fx-background-color: #00cc00;"
                + "    -fx-text-fill: white;"
                + "    -fx-cursor: pointer;"
                + "    -fx-cursor: hand;"
                + "    -fx-font-family: Arial;"
                + "    -fx-font-size: 1.2em;background-color: #cfd8dc;");
        defaultValueToCurrentDay(from, to);

        btn.setOnAction(e -> {
            fromDate = from.getValue();
            toDate = to.getValue();
            stage.close();
        });
        VBox empty = new VBox();
        empty.setMinHeight(50);
        empty.setMaxHeight(50);
        empty.setPrefHeight(50);
        VBox addPane = new VBox(10, btn);
        root.getChildren().addAll(datePicker, empty, addPane);
        root.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            e.consume();
            stage.close();
        });
        stage.showAndWait();
    }

    private void defaultValueToCurrentDay(DatePicker from, DatePicker to) {
        from.setValue(LocalDate.now());
        to.setValue(LocalDate.now());
    }

}

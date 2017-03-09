
package gestione.bilancio.controller;

import gestione.bilancio.model.Bilancio;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TableView;

/**
 *
 * @author petitpapa
 */
public class TotalHandler {

    public static SimpleDoubleProperty totalProperty = new SimpleDoubleProperty(0);

    public static void setTotal(TableView<Bilancio> table) {
        totalProperty.set(calculateSum(table));
    }

    public static double calculateSum(TableView<Bilancio> table) {
        return table.getItems().stream()
                .filter(pred -> pred.getTipo().equals(Bilancio.Tipo.ENTRATA))
                .mapToDouble(e -> e.getAmmontare()).sum() - table.getItems().stream()
                .filter(pred -> pred.getTipo().equals(Bilancio.Tipo.SPESA))
                .mapToDouble(e -> e.getAmmontare()).sum();
    }

    public static void updateTotalSumForLabel(Bilancio bilancio) {
        if (bilancio.getTipo().equals(Bilancio.Tipo.ENTRATA)) {
            totalProperty.set(totalProperty.get() + bilancio.getAmmontare());
        } else {
            totalProperty.set(totalProperty.get() - bilancio.getAmmontare());
        }
    }

}

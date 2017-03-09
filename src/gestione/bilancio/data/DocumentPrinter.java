/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestione.bilancio.data;

import gestione.bilancio.model.Bilancio;
import java.util.concurrent.Callable;
import javafx.concurrent.Task;
import javafx.print.PrinterJob;

import javafx.scene.control.TableView;

/**
 *
 * @author petitpapa
 */
public class DocumentPrinter implements Callable<Boolean> {

    private final TableView<Bilancio> table;
    private PrinterJob job;

    public DocumentPrinter(TableView<Bilancio> table) {
        this.table = table;
    }

    @Override
    public Boolean call() throws Exception {
        job = PrinterJob.createPrinterJob();
        return job.printPage(table);
    }

    public void close(){
        if(job != null)
            job.endJob();
    }
}

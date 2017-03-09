
package gestione.bilancio.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author petitpapa
 */
public class Bilancio {

    private LocalDate date;
    private String description;
    private double ammontare;

    public Bilancio() {
    }

    public enum Tipo {
        SPESA, ENTRATA
    };
    private Tipo tipo;

    public Bilancio(String description, double ammontare, Tipo tipo) {
        this.date = LocalDate.now();
        this.description = description;
        this.ammontare = ammontare;
        this.tipo = tipo;
    }

    static Bilancio spesa(String description, double ammontare) {
        return new Bilancio(description, ammontare, Tipo.SPESA);
    }

    static Bilancio entrata(String description, double ammontare) {
        return new Bilancio(description, ammontare, Tipo.ENTRATA);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmmontare() {
        return ammontare;
    }

    public void setAmmontare(double ammontare) {
        this.ammontare = ammontare;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Bilancio other = (Bilancio)obj;
        return Objects.equals(description, other.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("date: ").append(getDate()).append(" Description: ").append(getDescription());
        return sb.toString();
    }

    
}

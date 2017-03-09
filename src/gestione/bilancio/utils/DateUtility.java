package gestione.bilancio.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.zip.DataFormatException;
import javafx.util.StringConverter;

/**
 *
 * @author petitpapa
 */
public class DateUtility extends StringConverter<LocalDate> {

    private static final String PATTERN = "yyyy-MM-dd";
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);

    @Override
    public String toString(LocalDate localDate) {
        if (localDate != null)
            return dateFormatter.format(localDate);
        else
            return "";
    }

    @Override
    public LocalDate fromString(String date) {
        if (date != null && !date.trim().isEmpty())
            try {
                return LocalDate.parse(date, dateFormatter);
            } catch (DateTimeParseException e) {
                return null;
            }
        else 
            return null;
    }
}

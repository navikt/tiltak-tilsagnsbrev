package no.nav.tag.tilsagnsbrev;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DatoUtils {
    public static LocalDateTime getNow() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
}

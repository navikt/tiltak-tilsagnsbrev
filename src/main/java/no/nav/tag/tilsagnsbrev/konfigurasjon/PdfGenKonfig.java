package no.nav.tag.tilsagnsbrev.konfigurasjon;

import com.google.gson.*;
import lombok.Data;
import no.nav.tag.tilsagnsbrev.mapper.json.GsonWrapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Configuration
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.pdfgen")
public class PdfGenKonfig {
    private String uri;
}

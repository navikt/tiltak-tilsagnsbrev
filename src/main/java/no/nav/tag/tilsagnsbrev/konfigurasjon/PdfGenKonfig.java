package no.nav.tag.tilsagnsbrev.konfigurasjon;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.pdfgen")
public class PdfGenKonfig {
    private String uri;

    private static final String PDF_DATOFORMAT = "d. MMMM yyyy";
    private static final Locale LOCALE_NO = Locale.forLanguageTag("no");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PDF_DATOFORMAT, LOCALE_NO);

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(PDF_DATOFORMAT);
            builder.serializers(new LocalDateSerializer(DATE_FORMATTER));
        };
    }
}

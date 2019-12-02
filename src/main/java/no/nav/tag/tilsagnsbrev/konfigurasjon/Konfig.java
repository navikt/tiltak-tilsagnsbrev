package no.nav.tag.tilsagnsbrev.konfigurasjon;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnRetryProsess;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class Konfig {

    private static final String PDF_DATOFORMAT = "dd MMMM yyyy";
    private static final Locale LOCALE_NO = Locale.forLanguageTag("no");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PDF_DATOFORMAT, LOCALE_NO);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TilsagnRetryProsess feiletTilsagnsBehandler() {
        return new TilsagnRetryProsess();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(PDF_DATOFORMAT);
            builder.serializers(new LocalDateSerializer(DATE_FORMATTER));
        };
    }

}

package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.pdfgen")
public class PdfGenKonfig {
    private String uri;
}

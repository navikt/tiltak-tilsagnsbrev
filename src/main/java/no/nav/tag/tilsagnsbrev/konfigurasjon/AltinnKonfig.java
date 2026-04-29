package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.altinn")
public class AltinnKonfig {
    private String baseUrl;
}

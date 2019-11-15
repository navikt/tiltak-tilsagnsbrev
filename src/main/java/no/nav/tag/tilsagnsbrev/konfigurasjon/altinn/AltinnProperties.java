package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.altinn")
public class AltinnProperties {
    private String uri;
    private String systemBruker;
    private String systemPassord;
}

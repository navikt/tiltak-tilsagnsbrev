package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.persondata")
public class PersondataKonfig {
    private String uri;
}

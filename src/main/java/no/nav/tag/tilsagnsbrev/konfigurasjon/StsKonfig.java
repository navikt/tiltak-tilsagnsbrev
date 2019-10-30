package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.sts")
public class StsKonfig {
    private URI uri;
    private String bruker;
    private String passord;
}

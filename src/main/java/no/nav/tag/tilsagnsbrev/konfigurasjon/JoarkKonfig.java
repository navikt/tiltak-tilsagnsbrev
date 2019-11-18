package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.joark")
public class JoarkKonfig {
    private URI uri = URI.create("http://localhost:4444");
}

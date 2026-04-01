package no.nav.tag.tilsagnsbrev.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.persondata")
public class PersondataProperties {
    private String uri;
}

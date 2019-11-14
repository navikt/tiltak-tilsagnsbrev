package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import lombok.Data;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Data
@Configuration
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.altinn")
public class AltinnProperties {
    private String uri;
    private String user;
    private String password;
}

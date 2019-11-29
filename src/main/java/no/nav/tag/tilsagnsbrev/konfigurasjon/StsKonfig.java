package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.Data;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.net.URI;

@Data
@Component
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.sts")
public class StsKonfig {
    private URI wsUri;
    private URI uri;
    private String bruker;
    private String passord;
}

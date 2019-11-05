package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Data
@Configuration
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.altinn")
public class MqKonfig {
    private String queue;
    private String user;
    private String password;
    private String queueManager;
    private String channel;
    private String connName;
    private String host;
    private String port;
}

package no.nav.tag.tilsagnsbrev.konfigurasjon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Konfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class TilsagnKonfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
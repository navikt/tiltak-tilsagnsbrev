package no.nav.tag.tilsagnsbrev.konfigurasjon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestTemplateKonfigMock {
    @Bean
    @Profile("local")
    public RestTemplate tokenSupportRestTemplate() {
        return new RestTemplate();
    }
}

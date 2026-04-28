package no.nav.tag.tilsagnsbrev.konfigurasjon;

import no.nav.security.token.support.client.core.ClientProperties;
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService;
import no.nav.security.token.support.client.spring.ClientConfigurationProperties;
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher;
import no.nav.security.token.support.client.spring.oauth2.OAuth2ClientRequestInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

@Configuration
@Profile({"preprod", "prod"})
public class RestTemplateKonfig {

    @Bean
    public RestTemplate tokenSupportRestTemplate(
            RestTemplateBuilder restTemplateBuilder,
            OAuth2ClientRequestInterceptor oAuth2ClientRequestInterceptor
    ) {
        return restTemplateBuilder.interceptors(oAuth2ClientRequestInterceptor).build();
    }

    @Bean
    public OAuth2ClientRequestInterceptor oAuth2ClientRequestInterceptor(
        ClientConfigurationProperties properties,
        OAuth2AccessTokenService service,
        ClientConfigurationPropertiesMatcher matcher
    ) {
        return new OAuth2ClientRequestInterceptor(properties, service, matcher);
    }

    @Bean
    public ClientConfigurationPropertiesMatcher clientConfigurationPropertiesMatcher() {
        return new ClientConfigurationPropertiesMatcher() {
            @Override
            public ClientProperties findProperties(
                    ClientConfigurationProperties properties,
                    String s
            ) {
                return findProperties(properties, URI.create(s));
            }

            @Override
            public ClientProperties findProperties(
                    ClientConfigurationProperties properties,
                    URI uri
            ) {
                String registration = Arrays.stream(uri.getHost().split("\\.")).findFirst().orElse("");

                return switch (registration) {
                    case "dokarkiv-q1" -> properties.getRegistration().get("dokarkiv");
                    default -> properties.getRegistration().get(registration);
                };
            }
        };
    }
}

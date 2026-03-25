package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.altinn.MaskinportenTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static no.nav.tag.tilsagnsbrev.konfigurasjon.MaskinportenCacheKonfig.MASKINPORTEN_CACHE;

@Slf4j
@Service
public class MaskinportenTokenService {
    private static final String IDENTITY_PROVIDER = "maskinporten";

    @Value("${NAIS_TOKEN_ENDPOINT:#{null}}")
    private String naisTokenEndpoint;

    @Value("${MASKINPORTEN_SCOPES:#{null}}")
    private String maskinportenScopes;

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(MASKINPORTEN_CACHE)
    public String hentToken() {
        log.info("Henter nytt Maskinporten-token");
        return hentTokenFraMaskinporten();
    }

    @CacheEvict(MASKINPORTEN_CACHE)
    public void evict() {
        log.info("Sletter cachet Maskinporten-token");
    }

    private String hentTokenFraMaskinporten() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of(
            "identity_provider", IDENTITY_PROVIDER,
            "target", maskinportenScopes
        );

        MaskinportenTokenResponse response = restTemplate.postForObject(
            naisTokenEndpoint,
            new HttpEntity<>(body, headers),
            MaskinportenTokenResponse.class
        );

        if (response == null || response.getAccessToken() == null) {
            throw new RuntimeException("Tomt svar fra Maskinporten token-endepunkt");
        }

        return response.getAccessToken();
    }
}

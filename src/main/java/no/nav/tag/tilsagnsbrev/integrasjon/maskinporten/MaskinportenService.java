package no.nav.tag.tilsagnsbrev.integrasjon.maskinporten;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

import static no.nav.tag.tilsagnsbrev.konfigurasjon.MaskinportenCacheKonfig.MASKINPORTEN_CACHE;

@Slf4j
@Service
public class MaskinportenService {

    private static final String SCOPES = "altinn:serviceowner altinn:correspondence.write";
    private static final String JWT_BEARER_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    private static final int TOKEN_LIFETIME_MS = 120_000;

    @Value("${MASKINPORTEN_CLIENT_ID:#{null}}")
    private String clientId;

    @Value("${MASKINPORTEN_CLIENT_JWK:#{null}}")
    private String clientJwk;

    @Value("${MASKINPORTEN_ISSUER:#{null}}")
    private String issuer;

    @Value("${MASKINPORTEN_TOKEN_ENDPOINT:#{null}}")
    private String tokenEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(MASKINPORTEN_CACHE)
    public String hentToken() {
        log.info("Henter nytt Maskinporten-token");
        String jwtGrant = lagJwtGrant();
        return veksleTilAccessToken(jwtGrant);
    }

    @CacheEvict(MASKINPORTEN_CACHE)
    public void evict() {
        log.info("Sletter cachet Maskinporten-token");
    }

    private String lagJwtGrant() {
        try {
            JWK jwk = JWK.parse(clientJwk);
            RSAKey rsaKey = (RSAKey) jwk;
            RSASSASigner signer = new RSASSASigner(rsaKey);

            Date now = new Date();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .audience(issuer)
                    .issuer(clientId)
                    .issueTime(now)
                    .expirationTime(new Date(now.getTime() + TOKEN_LIFETIME_MS))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", SCOPES)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(rsaKey.getKeyID())
                            .build(),
                    claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Feil ved opprettelse av Maskinporten JWT grant: " + e.getMessage(), e);
        }
    }

    private String veksleTilAccessToken(String jwtGrant) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", JWT_BEARER_GRANT_TYPE);
        body.add("assertion", jwtGrant);

        MaskinportenTokenResponse response = restTemplate.postForObject(
                tokenEndpoint,
                new HttpEntity<>(body, headers),
                MaskinportenTokenResponse.class);

        if (response == null || response.getAccessToken() == null) {
            throw new RuntimeException("Tomt svar fra Maskinporten token-endepunkt");
        }
        return response.getAccessToken();
    }
}

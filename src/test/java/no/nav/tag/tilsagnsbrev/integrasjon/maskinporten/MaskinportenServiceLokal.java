package no.nav.tag.tilsagnsbrev.integrasjon.maskinporten;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Lokal testimplementasjon av MaskinportenService som returnerer en dummy-token
 * uten å forsøke å koble til Maskinporten. Brukes automatisk for @Profile("local").
 */
@Service
@Primary
@Profile("local")
public class MaskinportenServiceLokal extends MaskinportenService {

    @Override
    public String hentToken() {
        return "test-maskinporten-token";
    }

    @Override
    public void evict() {
    }
}

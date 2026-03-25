package no.nav.tag.tilsagnsbrev.konfigurasjon;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MaskinportenCacheKonfig {

    public static final String MASKINPORTEN_CACHE = "maskinporten_cache";

    @Bean
    public CaffeineCache maskinportenCache() {
        return new CaffeineCache(MASKINPORTEN_CACHE,
                Caffeine.newBuilder()
                        .maximumSize(1)
                        .expireAfterWrite(100, TimeUnit.SECONDS)
                        .recordStats()
                        .build());
    }
}

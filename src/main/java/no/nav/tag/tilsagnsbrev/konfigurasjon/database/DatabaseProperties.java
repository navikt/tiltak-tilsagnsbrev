package no.nav.tag.tilsagnsbrev.konfigurasjon.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Data
@Component
@Profile({"preprod", "prod"})
@ConfigurationProperties(prefix = "tilsagnsbrev.database")
public class DatabaseProperties {
    private String databaseNavn;
    private String databaseUrl;
    private String vaultSti;
    private Integer maximumPoolSize;
    private Integer minimumIdle;
    private Integer maxLifetime;
}
package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import lombok.Data;
import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasic;
import no.nav.tag.tilsagnsbrev.integrasjon.stsws.WsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Data
@Profile("dev")
@Configuration
public class AltinnKonfig {

    @Autowired
    private AltinnProperties altinnProperties;

    @Bean
    public ICorrespondenceAgencyExternalBasic iCorrespondenceAgencyExternalBasic() {
        return WsClient.createPort(altinnProperties.getUri(), ICorrespondenceAgencyExternalBasic.class);
    }
}

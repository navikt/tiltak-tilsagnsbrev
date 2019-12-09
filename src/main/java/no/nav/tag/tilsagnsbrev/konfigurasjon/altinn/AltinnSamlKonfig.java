package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasic;
import no.nav.tag.tilsagnsbrev.integrasjon.sts.ws.STSClientConfigurer;
import no.nav.tag.tilsagnsbrev.integrasjon.sts.ws.WsClient;
import no.nav.tag.tilsagnsbrev.konfigurasjon.StsKonfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"preprod", "prod"})
public class AltinnSamlKonfig {

    @Autowired
    private AltinnProperties altinnProperties;

    @Autowired
    private StsKonfig stsKonfig;

    @Bean
    public ICorrespondenceAgencyExternalBasic iCorrespondenceAgencyExternalBasic() {
        ICorrespondenceAgencyExternalBasic port = WsClient.createPort(altinnProperties.getUri(), ICorrespondenceAgencyExternalBasic.class);
        STSClientConfigurer configurer = new STSClientConfigurer(stsKonfig.getWsUri(), stsKonfig.getBruker(), stsKonfig.getPassord());
        configurer.configureRequestSamlToken(port);
        return port;
    }
}
package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasic;
import no.nav.tag.tilsagnsbrev.integrasjon.sts.ws.WsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.xml.namespace.QName;

@Profile("local")
@Configuration
public class AltinnKonfig {

    static final String ALTINN_NS = "http://www.altinn.no/services/ServiceEngine/Correspondence/2009/10";
    static final QName ALTINN_SERVICE = new QName(ALTINN_NS, "CorrespondenceAgencyExternalBasicSF");
    static final QName ALTINN_PORT = new QName(ALTINN_NS, "BasicHttpBinding_ICorrespondenceAgencyExternalBasic");

    @Autowired
    private AltinnProperties altinnProperties;

    @Bean
    public ICorrespondenceAgencyExternalBasic iCorrespondenceAgencyExternalBasic() {
        return WsClient.createPort(
                altinnProperties.getUri(),
                ICorrespondenceAgencyExternalBasic.class,
                ICorrespondenceAgencyExternalBasic.class.getResource("/wsdl/CorrespondenceAgencyExternalBasic.svc.wsdl"),
                ALTINN_SERVICE,
                ALTINN_PORT
        );
    }
}

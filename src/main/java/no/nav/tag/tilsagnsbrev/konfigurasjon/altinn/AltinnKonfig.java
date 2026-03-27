package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import no.altinn.services.serviceengine.correspondence._2009._10.CorrespondenceAgencyExternalBasicSF;
import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

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
        var wsdl = ICorrespondenceAgencyExternalBasic.class.getResource("/wsdl/CorrespondenceAgencyExternalBasic.svc.wsdl");
        var service = new CorrespondenceAgencyExternalBasicSF(wsdl);
        ICorrespondenceAgencyExternalBasic port = service.getBasicHttpBindingICorrespondenceAgencyExternalBasic();
        ((BindingProvider) port).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, altinnProperties.getUri());
        return port;
    }
}

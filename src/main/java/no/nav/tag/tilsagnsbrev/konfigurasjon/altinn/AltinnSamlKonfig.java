package no.nav.tag.tilsagnsbrev.konfigurasjon.altinn;

import lombok.RequiredArgsConstructor;
import no.altinn.services.serviceengine.correspondence._2009._10.ICorrespondenceAgencyExternalBasic;
import no.nav.tag.tilsagnsbrev.integrasjon.stsws.STSClientConfigurer;
import no.nav.tag.tilsagnsbrev.integrasjon.stsws.WsClient;
import no.nav.tag.tilsagnsbrev.konfigurasjon.StsProperties;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
@Profile({"preprod, prod"})
@RequiredArgsConstructor
public class AltinnSamlKonfig {
    private final AltinnProperties altinnProperties;
    private final StsProperties stsProperties;

    @Bean
    public ICorrespondenceAgencyExternalBasic iCorrespondenceAgencyExternalBasic() {
        ICorrespondenceAgencyExternalBasic port = WsClient.createPort(altinnProperties.getUri(), ICorrespondenceAgencyExternalBasic.class);
        STSClientConfigurer configurer = new STSClientConfigurer(stsProperties.getWsUri(), stsProperties.getBruker(), stsProperties.getPassord());
        configurer.configureRequestSamlToken(port);
        return port;
    }

    @Bean(name = "altinnWebService")
    public WebServiceTemplate webServiceTemplate(WebServiceTemplateBuilder builder) throws Exception {

        UsernamePasswordCredentials credential = new UsernamePasswordCredentials(altinnProperties.getUser(), altinnProperties.getPassword());

        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setAcceptGzipEncoding(false);
        messageSender.setCredentials(credential);
        messageSender.afterPropertiesSet();

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.altinn.services.serviceengine.correspondence._2009._10");
        marshaller.afterPropertiesSet();

        WebServiceTemplate webServiceTemplate = builder
                .setDefaultUri(altinnProperties.getUri())
                .messageSenders(messageSender)
                .setMarshaller(marshaller)
                .setUnmarshaller(marshaller)
                .build();
        webServiceTemplate.afterPropertiesSet();



        return webServiceTemplate;
    }
}

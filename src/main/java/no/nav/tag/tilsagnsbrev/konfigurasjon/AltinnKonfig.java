package no.nav.tag.tilsagnsbrev.konfigurasjon;

import lombok.Data;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Data
@Configuration
@ConfigurationProperties(prefix = "tilsagnsbrev.integrasjon.altinn")
public class AltinnKonfig {
    private String uri;
    private String user;
    private String password;

    @Bean
    public WebServiceTemplate webServiceTemplate(WebServiceTemplateBuilder builder) throws Exception {

        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        UsernamePasswordCredentials credential = new UsernamePasswordCredentials(user, password);
        messageSender.setCredentials(credential);
        messageSender.afterPropertiesSet();

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("no.nav.tag.tilsagnsbrev.dto.altinn");
        marshaller.afterPropertiesSet();

        WebServiceTemplate webServiceTemplate = builder
                .setDefaultUri("http://localhost:4444/mock")
                .messageSenders(messageSender)
                .setMarshaller(marshaller)
                .build();

        webServiceTemplate.afterPropertiesSet();
        return webServiceTemplate;
    }
}

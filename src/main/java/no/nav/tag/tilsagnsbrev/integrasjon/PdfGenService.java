package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class PdfGenService {

    static final String PATH = "/template/tilsagnsbrev/create-pdf";

    private final URI uri;
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    public PdfGenService(PdfGenKonfig pdfGenKonfig) {
        uri = UriComponentsBuilder
                .fromUri(URI.create(pdfGenKonfig.getUri()))
                .path(PATH)
                .build()
                .toUri();
        headers.setContentType((MediaType.APPLICATION_JSON));
    }

    public byte[] tilsagnTilPdfBrev(String tilsagnJson) {
        HttpEntity<String> entity = new HttpEntity<>(tilsagnJson, headers);
        try {
            return restTemplate.postForObject(uri, entity, byte[].class);
        } catch (Exception e) {
            log.error("Feil ved oppretting av pdf fil", e);
            throw new SystemException(e.getMessage());
        }
    }
}



package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class PdfGenService {

    static final String PATH = "/template/tilsagnsbrev/create-pdf";

    private URI uri;

    private RestTemplate restTemplate = new RestTemplate();

    public PdfGenService(PdfGenKonfig pdfGenKonfig){
        uri = UriComponentsBuilder
                .fromUri(URI.create(pdfGenKonfig.getUri()))
                .path(PATH)
                .build()
                .toUri();
    }

    public byte[] tilsagnTilPdfBrev(String tilsagnJson) {
        return restTemplate.postForObject(uri, tilsagnJson, byte[].class);
    }
}



package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

@Slf4j
@Service
public class PdfGenService {

    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PdfTemplateURI pdfTemplateURI;

    public PdfGenService() {
        headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, Charset.defaultCharset()));
    }

    public byte[] tilsagnsbrevTilPdfBytes(TilsagnUnderBehandling tilsagnUnderBehandling, String pdfJson) {
        Tilsagn tilsagn = tilsagnUnderBehandling.getTilsagn();
        String tiltakKode = tilsagn.getTiltakKode();
        log.info("Oppretter pdf av tilsagnsbrev med tiltakskode {} for bedrift {}", tiltakKode, tilsagn.getTiltakArrangor().getOrgNummer());
        URI uri = pdfTemplateURI.getTemplateURI(tiltakKode);
        return hentPdf(pdfJson, uri);
    }

    private byte[] hentPdf(String tilsagnJson, URI uri) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(tilsagnJson, headers);
            return restTemplate.postForObject(uri, entity, byte[].class);
        } catch (Exception e) {
            log.error("Feil ved oppretting av pdf fil", e);
            throw new SystemException(e.getMessage());
        }
    }
}

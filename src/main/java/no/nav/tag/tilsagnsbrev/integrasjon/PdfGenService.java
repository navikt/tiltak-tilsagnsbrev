package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import no.nav.tag.tilsagnsbrev.mapper.TiltakType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Service
public class PdfGenService {

    private RestTemplate restTemplate;
    private URI pdfgenUri;

    public PdfGenService(RestTemplate restTemplate, PdfGenKonfig pdfGenKonfig) {
        this.restTemplate = restTemplate;
        this.pdfgenUri = pdfGenKonfig.getUri();
    }

    public byte[] tilsagnsbrevTilPdfBytes(Tilsagn tilsagn) {
        TiltakType tiltakstype = tilsagn.getTiltakType();
        log.info("Oppretter pdf av tilsagnsbrev med tiltakskode {} for bedrift {}", tiltakstype, tilsagn.getTiltakArrangor().getOrgNummer());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Tilsagn> request = new HttpEntity<>(tilsagn, headers);
            String url = pdfgenUri + "/tiltak-tilsagnsbrev/" + tiltakstype.getPdfgenMal();
            return restTemplate.postForObject(url, request, byte[].class);
        } catch (Exception e) {
            log.error("Feil ved oppretting av pdf fil", e);
            throw new SystemException(e.getMessage());
        }
    }
}

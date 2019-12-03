package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
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

    private static final String PATH_DELTAKER = "/template/tilsagnsbrev-deltaker/create-pdf";
    private static final String PATH_GRUPPE = "/template/tilsagnsbrev-gruppe/create-pdf";

    private final URI uriTilsagnGruppe;
    private final URI uriTilsagnDeltaker;
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private RestTemplate restTemplate;

    public PdfGenService(PdfGenKonfig pdfGenKonfig) {
        uriTilsagnGruppe = UriComponentsBuilder.fromUri(URI.create(pdfGenKonfig.getUri())).path(PATH_GRUPPE).build().toUri();
        uriTilsagnDeltaker = UriComponentsBuilder.fromUri(URI.create(pdfGenKonfig.getUri())).path(PATH_DELTAKER).build().toUri();
        headers.setContentType((MediaType.APPLICATION_JSON));
    }

    public byte[] tilsagnsbrevTilPdfBytes(TilsagnUnderBehandling tilsagnUnderBehandling, String pdfJson) {
        if (tilsagnUnderBehandling.getTilsagn().erGruppeTilsagn()) {
            log.info("Oppretter pdf av tilsagnsbrev for gruppe for bedrift {}", tilsagnUnderBehandling.getTilsagn().getTiltakArrangor().getOrgNummer());
            return hentPdf(pdfJson, uriTilsagnGruppe);
        }
        log.info("Oppretter pdf av tilsagnsbrev for enkel deltaker for bedrift {}", tilsagnUnderBehandling.getTilsagn().getTiltakArrangor().getOrgNummer());
        return hentPdf(pdfJson, uriTilsagnDeltaker);
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



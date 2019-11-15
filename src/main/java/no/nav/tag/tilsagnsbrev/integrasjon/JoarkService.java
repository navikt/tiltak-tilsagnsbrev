package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.journalpost.JournalpostResponse;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.konfigurasjon.JoarkKonfig;
import no.nav.tag.tilsagnsbrev.mapper.journalpost.TilsagnTilJournalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Service
public class JoarkService {

    @Autowired
    private TilsagnTilJournalpost tilsagnTilJournalpost;

    static final String PATH = "/rest/journalpostapi/v1/journalpost";
    static final String QUERY_PARAM = "forsoekFerdigstill=true";
    private URI uri;
    private final HttpHeaders headers = new HttpHeaders();

    public JoarkService(JoarkKonfig joarkKonfig) {
        uri = UriComponentsBuilder.fromUri(joarkKonfig.getUri())
                .path(PATH)
                .query(QUERY_PARAM)
                .build()
                .toUri();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType((MediaType.APPLICATION_JSON));
    }

    public String opprettOgSendJournalpost(final String token, final Tilsagn tilsagn, byte[] pdf) {
        Journalpost journalpost = tilsagnTilJournalpost.konverterTilJournalpost(tilsagn, pdf);
        headers.setBearerAuth(token);
        HttpEntity<Journalpost> entity = new HttpEntity<>(journalpost, headers);
        JournalpostResponse response;
        try {
            response = new RestTemplate().postForObject(uri, entity, JournalpostResponse.class);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Kall til Joark feilet: " + e.getMessage());
        }
        return response.getJournalpostId();
    }
}

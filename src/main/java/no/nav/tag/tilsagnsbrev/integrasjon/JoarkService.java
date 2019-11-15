package no.nav.tag.tilsagnsbrev.integrasjon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.integrasjon.sts.StsService;
import no.nav.tag.tilsagnsbrev.konfigurasjon.JoarkKonfig;
import no.nav.tag.tilsagnsbrev.mapper.journalpost.TilsagnJournalpostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Service
public class JoarkService {

    @Autowired
    private TilsagnJournalpostMapper tilsagnJournalpostMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StsService stsService;

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

    public void sendJournalpost(final Journalpost journalpost) {
        debugLogJournalpost(journalpost);
        String response = null; //TODO Bruke noe annet
        try {
            log.info("Forsøker å journalføre tilsagnsbrev {}", journalpost.getEksternReferanseId());
            restTemplate.postForObject(uri, entityMedStsToken(journalpost), String.class);
        } catch (Exception e1) {
            stsService.evict();
            log.warn("Feil ved kommunikasjon mot journalpost-API. Henter nytt sts-token og forsøker igjen");
            try {
                response = restTemplate.postForObject(uri, entityMedStsToken(journalpost), String.class);
            } catch (Exception e2) {
                log.error("Kall til Joark feilet: {}", response != null ? response : "", e2);
                throw new RuntimeException("Kall til Joark feilet: " + e2);
            }
        }
        log.info("Journalført tilsagnsbrev {}", journalpost.getEksternReferanseId());
    }

    private HttpEntity<Journalpost> entityMedStsToken(final Journalpost journalpost) {
        headers.setBearerAuth(stsService.hentToken());
        HttpEntity<Journalpost> entity = new HttpEntity<>(journalpost, headers);
        return entity;
    }

    private void debugLogJournalpost(Journalpost journalpost) {
        if (log.isDebugEnabled()) {
            try {
                log.info("JSON REQ: {}", new ObjectMapper().writeValueAsString(journalpost));
            } catch (JsonProcessingException e) {
            }
        }
    }

}

package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.journalpost.*;
import no.nav.tag.tilsagnsbrev.integrasjon.sts.StsService;
import no.nav.tag.tilsagnsbrev.konfigurasjon.JoarkKonfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

import static no.nav.tag.tilsagnsbrev.integrasjon.JoarkService.PATH;
import static no.nav.tag.tilsagnsbrev.integrasjon.JoarkService.QUERY_PARAM;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JoarkServiceTest {

    private final URI uri = URI.create("http://localhost:8090");
    private final URI expUri = UriComponentsBuilder.fromUri(uri).path(PATH).query(QUERY_PARAM).build().toUri();

    private final Journalpost journalpost =
            new Journalpost("Tittel", new Bruker("bid"), new Mottaker("mid", "navn"), new Sak("fagsakId"), Arrays.asList());

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StsService stsService;

    @InjectMocks
    private JoarkService joarkService = new JoarkService(new JoarkKonfig(uri));

    @Test
    public void kall_mot_joark_ok_skal_returnere_journalpostid() {
        JournalpostResponse joarkResponse = new JournalpostResponse();
        joarkResponse.setJournalpostId("123");
        when(restTemplate.postForObject(eq(expUri), any(HttpEntity.class), any())).thenReturn(joarkResponse);
        joarkService.sendJournalpost(journalpost);
        verify(restTemplate, times(1)).postForObject(eq(expUri), any(HttpEntity.class), any());
    }

    @Test
    public void oppretterJournalpost_status_500() {
        assertThrows(RuntimeException.class, () -> {
            when(restTemplate.postForObject(eq(expUri), any(HttpEntity.class), any())).thenThrow(RuntimeException.class);
            joarkService.sendJournalpost(null);
        });
    }

    @Test
    public void feil_mot_tjeneste_skal_hente_nytt_sts_token_og_forsøke_på_nytt() {
        when(restTemplate.postForObject(eq(expUri), any(HttpEntity.class), any())).thenThrow(RuntimeException.class);
        try {
            joarkService.sendJournalpost(journalpost);
        } catch (RuntimeException re) {
            verify(stsService).evict();
            verify(stsService, times(2)).hentToken();
            verify(restTemplate, times(2)).postForObject(eq(expUri), any(HttpEntity.class), any());
        }
    }

}

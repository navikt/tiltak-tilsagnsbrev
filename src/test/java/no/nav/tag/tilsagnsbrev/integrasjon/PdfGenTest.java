package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("dev")
public class PdfGenTest {

    @Mock
    private RestTemplate restTemplate;

    PdfGenKonfig pdfGenKonfig = new PdfGenKonfig("");

    @InjectMocks
    PdfGenService pdfGenService = new PdfGenService(pdfGenKonfig);

    private static final String PATH_DELTAKER = "/template/tilsagnsbrev-deltaker/create-pdf";
    private static final String PATH_GRUPPE = "/template/tilsagnsbrev-gruppe/create-pdf";

    @Test
    public void returnererPdfBytesForDeltaker(){
        URI forvURI = UriComponentsBuilder.fromUri(URI.create(pdfGenKonfig.getUri())).path(PATH_DELTAKER).build().toUri();

        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().tilsagn(Testdata.tilsagnEnDeltaker()).build();
        pdfGenService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling);
        verify(restTemplate).postForObject(eq(forvURI), any(HttpEntity.class), any());
    }

    @Test
    public void returnererPdfBytesForGruppe(){
        URI forvURI = UriComponentsBuilder.fromUri(URI.create(pdfGenKonfig.getUri())).path(PATH_GRUPPE).build().toUri();

        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().tilsagn(Testdata.gruppeTilsagn()).build();
        pdfGenService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling);
        verify(restTemplate).postForObject(eq(forvURI), any(HttpEntity.class), any());
    }
}

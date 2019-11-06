package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnTilAltinnXml;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnsbehandlerTest {

    @Mock
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Mock
    private TilsagnTilAltinnXml tilsagnTilAltinnXml;

    @Mock
    private AltInnService altInnService;

    @Mock
    private PdfGenService pdfGenService;

    @InjectMocks
    private Tilsagnsbehandler tilsagnsbehandler;

    @Test
    public void behandlerTilsagn(){
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        String tilsagnJson = Testdata.tilsagnTilJSON(tilsagn);

        when(tilsagnJsonMapper.goldengateMeldingTilTilsagn(anyString())).thenReturn(tilsagn);
        when(tilsagnJsonMapper.tilsagnTilPdfJson(eq(tilsagn))).thenReturn(tilsagnJson);

        when(pdfGenService.tilsagnTilPdfBrev(tilsagnJson)).thenReturn("pdf".getBytes());

        when(tilsagnTilAltinnXml.tilAltinnMelding(eq(tilsagn), any())).thenCallRealMethod();

        tilsagnsbehandler.behandleTilsagn("");

        verify(altInnService, atLeastOnce()).sendTilsagnsbrev(anyString());

    }
}
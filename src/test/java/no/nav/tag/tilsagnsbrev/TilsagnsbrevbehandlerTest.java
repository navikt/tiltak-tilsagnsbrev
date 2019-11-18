package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnsbrevbehandlerTest {



    @Mock
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Mock
    private AltInnService altInnService;

    @Mock
    private PdfGenService pdfGenService;

    @InjectMocks
    private Tilsagnsbrevbehandler tilsagnsbrevbehandler;

    @Test
    @Ignore
    public void behandlerTilsagn(){
//        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
//        String tilsagnJson = Testdata.tilsagnTilJSON(tilsagn);
//
//        when(tilsagnJsonMapper.goldengateMeldingTilTilsagn(anyString())).thenReturn(tilsagn);
//        when(tilsagnJsonMapper.tilsagnTilJson(eq(tilsagn))).thenReturn(tilsagnJson);
//
//        when(pdfGenService.tilsagnsbrevTilBase64EncodedPdfBytes(tilsagnJson)).thenReturn("pdf".getBytes());
//
//        when(tilsagnTilAltinnMapper.tilAltinnMelding(eq(tilsagn), any())).thenCallRealMethod();
//
//        tilsagnsbrevbehandler.behandleTilsagn("");

//       verify(altInnService, atLeastOnce()).sendTilsagnsbrev(anyString());

    }
}

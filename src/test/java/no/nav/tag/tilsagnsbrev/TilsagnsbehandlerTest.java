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
public class TilsagnsbehandlerTest {



    @Mock
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Mock
    private AltInnService altInnService;

    @Mock
    private PdfGenService pdfGenService;

    @InjectMocks
    private Tilsagnsbehandler tilsagnsbehandler;

    @Test
    @Ignore
    public void behandlerTilsagn(){
//        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
//        String tilsagnJson = Testdata.tilsagnTilJSON(tilsagn);
//
//        when(tilsagnJsonMapper.goldengateMeldingTilTilsagn(anyString())).thenReturn(tilsagn);
//        when(tilsagnJsonMapper.tilsagnTilPdfJson(eq(tilsagn))).thenReturn(tilsagnJson);
//
//        when(pdfGenService.tilsagnTilPdfBrev(tilsagnJson)).thenReturn("pdf".getBytes());
//
//        when(tilsagnTilAltinnMapper.tilAltinnMelding(eq(tilsagn), any())).thenCallRealMethod();
//
//        tilsagnsbehandler.behandleTilsagn("");

//       verify(altInnService, atLeastOnce()).sendTilsagnsbrev(anyString());

    }
}

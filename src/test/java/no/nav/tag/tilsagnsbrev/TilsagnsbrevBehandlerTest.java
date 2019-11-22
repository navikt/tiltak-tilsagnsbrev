package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnsbrevBehandlerTest {

    @Mock
    private Oppgaver oppgaver;

    @Mock
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    @Mock
    private PdfGenService pdfGenService;

    @InjectMocks
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Test
    public void senderTilAltinnSelvOmJournalforingFeiler() {

        final byte[] pdf = "pdf".getBytes();
        String arenaMelding = Testdata.hentFilString("arenaMelding.json");
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().cid(UUID.randomUUID()).tilsagn(tilsagn).json(arenaMelding).build();

        doNothing().when(oppgaver).arenaMeldingTilTilsagnData(tilsagnUnderBehandling);
        when(pdfGenService.tilsagnsbrevTilBase64EncodedPdfBytes(tilsagnUnderBehandling.getJson())).thenReturn(pdf);
        doThrow(SystemException.class).when(oppgaver).journalfoerTilsagnsbrev(tilsagnUnderBehandling, pdf);
        when(feiletTilsagnBehandler.lagreEllerOppdaterFeil(eq(tilsagnUnderBehandling), any(SystemException.class))).thenReturn(true);

        doNothing().when(oppgaver).sendTilAltinn(tilsagnUnderBehandling, pdf);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        verify(oppgaver, times(1)).sendTilAltinn(tilsagnUnderBehandling, pdf);
    }
}

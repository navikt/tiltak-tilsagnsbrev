package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.behandler.Oppgaver;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggRepository;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnsbrevBehandlerTest {

    @Mock
    private Oppgaver oppgaver;

    @Mock
    private PdfGenService pdfGenService;

    @Mock
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Mock
    private TilsagnJsonMapper tilsagnJsonMapper;

    @InjectMocks
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Test
    public void senderTilAltinnSelvOmJournalforingFeiler() {

        final byte[] pdf = "pdf".getBytes();
        String arenaMelding = Testdata.hentFilString("arenaMelding.json");
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().cid(UUID.randomUUID()).tilsagn(tilsagn).json(arenaMelding).build();

        doNothing().when(tilsagnJsonMapper).pakkUtArenaMelding(tilsagnUnderBehandling);
        doNothing().when(tilsagnJsonMapper).opprettTilsagn(tilsagnUnderBehandling);
        //when(pdfGenService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling)).thenReturn(pdf);
        doThrow(SystemException.class).when(oppgaver).journalfoerTilsagnsbrev(tilsagnUnderBehandling, pdf);

        doNothing().when(oppgaver).sendTilAltinn(tilsagnUnderBehandling, pdf);
        when(tilsagnLoggRepository.lagretIdHvisNyMelding(any(TilsagnUnderBehandling.class))).thenReturn(true);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        verify(oppgaver, times(1)).sendTilAltinn(tilsagnUnderBehandling, pdf);
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tilsagnUnderBehandling), any(SystemException.class));
    }


}

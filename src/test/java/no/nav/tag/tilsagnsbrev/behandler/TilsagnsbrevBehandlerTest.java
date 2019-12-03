package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
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
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Mock
    private TilsagnJsonMapper tilsagnJsonMapper;

    @InjectMocks
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Test
    public void behandlerTilsagn() {
        ArenaMelding arenaMelding = Testdata.arenaMelding();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(arenaMelding).build();

        when(tilsagnLoggRepository.lagretIdHvisNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);
        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(2)).lagretIdHvisNyMelding(tub);
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
    }

    @Test
    public void senderTilAltinnSelvOmJournalforingFeiler() {

        final byte[] pdf = "pdf".getBytes();
        String arenaMelding = Testdata.hentFilString("arenaMelding.json");
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().cid(UUID.randomUUID()).tilsagn(tilsagn).json(arenaMelding).build();

        doNothing().when(tilsagnJsonMapper).pakkUtArenaMelding(tub);

        doNothing().when(tilsagnJsonMapper).opprettTilsagn(tub);
        when(oppgaver.opprettPdfDok(tub)).thenReturn(pdf);
        doThrow(SystemException.class).when(oppgaver).journalfoerTilsagnsbrev(tub, pdf);
        doNothing().when(oppgaver).sendTilAltinn(tub, pdf);
        when(tilsagnLoggRepository.lagretIdHvisNyMelding(any(TilsagnUnderBehandling.class))).thenReturn(true);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);
        verify(oppgaver, times(1)).sendTilAltinn(tub, pdf);
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tub), any(SystemException.class));
        verify(tilsagnLoggRepository, times(2)).lagretIdHvisNyMelding(eq(tub));
    }

    @Test
    public void parserIkkeArenaMeldingOgAvbryter() {
        ArenaMelding feiler = Testdata.arenaMeldingMedFeil();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().arenaMelding(feiler).build();

        doThrow(DataException.class).when(tilsagnJsonMapper).pakkUtArenaMelding(tilsagnUnderBehandling);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        verify(tilsagnLoggRepository, atLeastOnce()).lagretIdHvisNyMelding(tilsagnUnderBehandling);
        verify(tilsagnJsonMapper, never()).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, never()).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tilsagnUnderBehandling), any(TilsagnException.class));
    }

    @Test
    public void avbryterHvisOpprettTilsagnFeiler() {
        ArenaMelding feiler = Testdata.arenaMeldingMedFeil();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(feiler).build();

        doNothing().when(tilsagnJsonMapper).pakkUtArenaMelding(tub);
        doThrow(DataException.class).when(tilsagnJsonMapper).opprettTilsagn(tub);
        when(tilsagnLoggRepository.lagretIdHvisNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);

        verify(tilsagnLoggRepository, atLeastOnce()).lagretIdHvisNyMelding(tub);
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, never()).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tub), any(TilsagnException.class));
    }

    @Test
    public void avbryterHvisOpprettPdfDokFeiler() {
        ArenaMelding arenaMelding = Testdata.arenaMelding();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(arenaMelding).build();

        doNothing().when(tilsagnJsonMapper).pakkUtArenaMelding(tub);
        doNothing().when(tilsagnJsonMapper).opprettTilsagn(tub);
        when(tilsagnLoggRepository.lagretIdHvisNyMelding(tub)).thenReturn(true);
        doThrow(DataException.class).when(oppgaver).opprettPdfDok(tub);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);

        verify(tilsagnLoggRepository, atLeastOnce()).lagretIdHvisNyMelding(tub);
        verify(oppgaver, times(1)).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, never()).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tub), any(TilsagnException.class));
    }
}

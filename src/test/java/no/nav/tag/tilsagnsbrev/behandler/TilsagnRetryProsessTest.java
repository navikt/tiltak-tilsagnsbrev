package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.Testdata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnRetryProsessTest {

    @Mock
    private Oppgaver oppgaver;

    @Mock
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Mock
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Mock
    FeiletTilsagnBehandler feiletTilsagnBehandler;

    @InjectMocks
    private TilsagnRetryProsess tilsagnRetryProsess;

    private final static String tilsagnData = Testdata.hentFilString("TILSAGN_DATA.json");
    private final static ArenaMelding arenaMelding = Testdata.arenaMelding();


    @Test
    public void behandlerTilsagnEtterEnManuellBehandlingAvArenaMappingFeil() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(arenaMelding).build();
        when(feiletTilsagnBehandler.hentAlleTilRekjoring()).thenReturn(Arrays.asList(tub));

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(eq(tub));
        verify(tilsagnLoggRepository, never()).lagretIdHvisNyMelding(tub);
        verify(oppgaver, times(1)).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(feiletTilsagnBehandler, times(1)).oppdater(any(TilsagnUnderBehandling.class));
    }

    @Test
    public void behandlerTilsagnEtterFeiletJournalforing() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).altinnKittering(002).build();
        when(feiletTilsagnBehandler.hentAlleTilRekjoring()).thenReturn(Arrays.asList(tub));

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, never()).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(feiletTilsagnBehandler, times(1)).oppdater(any(TilsagnUnderBehandling.class));
    }


    @Test
    public void behandlerTilsagnEtterFeiletAltinnSending() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1").build();
        when(feiletTilsagnBehandler.hentAlleTilRekjoring()).thenReturn(Arrays.asList(tub));

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(feiletTilsagnBehandler, times(1)).oppdater(any(TilsagnUnderBehandling.class));
    }

    @Test
    public void feilerIgjenEtterFeiletAltinnSending() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1").build();
        when(feiletTilsagnBehandler.hentAlleTilRekjoring()).thenReturn(Arrays.asList(tub));

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();
        verify(tilsagnJsonMapper, times(1)).opprettTilsagn(any(TilsagnUnderBehandling.class));
        verify(oppgaver, times(1)).opprettPdfDok(any(TilsagnUnderBehandling.class));
        verify(oppgaver, never()).journalfoerTilsagnsbrev(any(TilsagnUnderBehandling.class), any());
        verify(oppgaver, times(1)).sendTilAltinn(any(TilsagnUnderBehandling.class), any());
        verify(feiletTilsagnBehandler, times(1)).oppdater(any(TilsagnUnderBehandling.class));
    }
}

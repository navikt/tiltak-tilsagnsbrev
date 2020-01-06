package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnRetryProsessTest {

    @Mock
    private Oppgaver oppgaver;

    @Mock
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    @InjectMocks
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Test
    public void behandlerTilsagnPaNytt() {
        TilsagnUnderBehandling tilsagnUnderBehandling = new TilsagnUnderBehandling();
        when(feiletTilsagnBehandler.hentAlleTilRekjoring()).thenReturn(Arrays.asList(tilsagnUnderBehandling));
        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        verify(oppgaver, times(1)).utfoerOppgaver(tilsagnUnderBehandling);
    }

}

package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnBehandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TilsagnRetryProsessTest {

    @Mock
    private Oppgaver oppgaver;

    @Mock
    private TilsagnBehandler tilsagnBehandler;

    @InjectMocks
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Test
    public void behandlerTilsagnPaNytt() {
        TilsagnUnderBehandling tilsagnUnderBehandling = new TilsagnUnderBehandling();
        when(tilsagnBehandler.hentAlleTilRekjoring()).thenReturn(Arrays.asList(tilsagnUnderBehandling));
        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        verify(oppgaver, times(1)).utfoerOppgaver(tilsagnUnderBehandling);
    }

}

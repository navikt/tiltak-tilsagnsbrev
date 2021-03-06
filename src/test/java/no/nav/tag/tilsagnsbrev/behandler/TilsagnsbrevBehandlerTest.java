package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(Testdata.arenaMelding()).build();

        when(tilsagnLoggRepository.lagretIdHvisNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);
        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).lagretIdHvisNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void parserIkkeArenaMeldingOgAvbryter() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            ArenaMelding feiler = Testdata.arenaMeldingMedFeil();
            TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().arenaMelding(feiler).build();

            doThrow(RuntimeException.class).when(tilsagnJsonMapper).pakkUtArenaMelding(tilsagnUnderBehandling);
            try {
                tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
            } finally {
                verify(tilsagnLoggRepository, never()).lagretIdHvisNyMelding(tilsagnUnderBehandling);
                verify(tilsagnJsonMapper, never()).opprettTilsagn(any(TilsagnUnderBehandling.class));
                verify(oppgaver, never()).utfoerOppgaver(any(TilsagnUnderBehandling.class));
            }
        });
    }

}

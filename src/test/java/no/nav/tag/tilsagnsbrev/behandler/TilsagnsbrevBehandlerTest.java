package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void behandler_tilsagn() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(Testdata.arenaMelding()).build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void oppretter_failet_tilsagn_og_avbryter_dersom_parsing_av_melding_failer() {
        ArenaMelding feiler = Testdata.arenaMeldingMedFeil();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().arenaMelding(feiler).build();

        doThrow(new no.nav.tag.tilsagnsbrev.exception.DataException("Parse error")).when(tilsagnJsonMapper).pakkUtArenaMelding(tilsagnUnderBehandling);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tilsagnUnderBehandling);
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tilsagnUnderBehandling), any(no.nav.tag.tilsagnsbrev.exception.TilsagnException.class));
        verify(tilsagnLoggRepository, never()).registrerNyMelding(tilsagnUnderBehandling);
        verify(oppgaver, never()).utfoerOppgaver(any(TilsagnUnderBehandling.class));
    }

}

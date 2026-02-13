package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TiltakType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

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

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(Instant.now(), tub);
        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void parserIkkeArenaMeldingOgAvbryter() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            ArenaMelding feiler = Testdata.arenaMeldingMedFeil();
            TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().arenaMelding(feiler).build();

            doThrow(RuntimeException.class).when(tilsagnJsonMapper).pakkUtArenaMelding(tilsagnUnderBehandling);
            try {
                tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(Instant.now(), tilsagnUnderBehandling);
            } finally {
                verify(tilsagnLoggRepository, never()).registrerNyMelding(tilsagnUnderBehandling);
                verify(tilsagnJsonMapper, never()).opprettTilsagn(any(TilsagnUnderBehandling.class));
                verify(oppgaver, never()).utfoerOppgaver(any(TilsagnUnderBehandling.class));
            }
        });
    }

    @Test
    public void avbryterBehandlingAvEkspertbistandEtterCutoffDato() {
        Instant etterCutoff = Instant.parse("2026-02-24T10:00:00.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode(TiltakType.EKSPEBIST.getTiltakskode()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(etterCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, never()).utfoerOppgaver(tub);
    }

    @Test
    public void behandlerEkspertbistandForCutoffDato() {
        Instant forCutoff = Instant.parse("2026-02-23T09:59:59.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode(TiltakType.EKSPEBIST.getTiltakskode()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(forCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void behandlerEkspertbistandNoyaktigPaCutoffDato() {
        Instant cutoff = Instant.parse("2026-02-23T10:00:00.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode(TiltakType.EKSPEBIST.getTiltakskode()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(cutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, never()).utfoerOppgaver(tub);
    }

    @Test
    public void behandlerAndreTiltakTyperUavhengigAvDato() {
        Instant etterCutoff = Instant.parse("2026-02-24T10:00:00.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode("MIDLONTIL").build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(etterCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void behandlerNarTilsagnErNull() {
        Instant etterCutoff = Instant.parse("2026-02-24T10:00:00.00Z");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(null)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(etterCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

}

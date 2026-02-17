package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TiltakType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

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
    public void behandler_tilsagn() {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(Testdata.arenaMelding()).build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        when(tilsagnJsonMapper.hentTiltakType(tub)).thenReturn(Optional.empty());

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(Instant.now(), tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void oppretter_failet_tilsagn_og_avbryter_dersom_parsing_av_melding_failer() {
        ArenaMelding feiler = Testdata.arenaMeldingMedFeil();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().arenaMelding(feiler).build();

        doThrow(new no.nav.tag.tilsagnsbrev.exception.DataException("Parse error")).when(tilsagnJsonMapper).pakkUtArenaMelding(tilsagnUnderBehandling);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(Instant.now(), tilsagnUnderBehandling);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tilsagnUnderBehandling);
        verify(oppgaver, times(1)).oppdaterFeiletTilsagn(eq(tilsagnUnderBehandling), any(no.nav.tag.tilsagnsbrev.exception.TilsagnException.class));
        verify(tilsagnLoggRepository, never()).registrerNyMelding(tilsagnUnderBehandling);
        verify(oppgaver, never()).utfoerOppgaver(any(TilsagnUnderBehandling.class));
    }

    @Test
    public void avslutter_behandling_av_ekspertbistand_dersom_melding_kom_etter_cut_off_dato() {
        Instant etterCutoff = Instant.parse("2026-02-24T10:00:00.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode(TiltakType.EKSPEBIST.getTiltakskode()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        when(tilsagnJsonMapper.hentTiltakType(tub)).thenReturn(Optional.of(TiltakType.EKSPEBIST));

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(etterCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, never()).utfoerOppgaver(tub);
    }

    @Test
    public void behandler_ekspertbistand_før_cut_off_date_som_vanlig() {
        Instant forCutoff = Instant.parse("2026-02-23T09:59:59.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode(TiltakType.EKSPEBIST.getTiltakskode()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        when(tilsagnJsonMapper.hentTiltakType(tub)).thenReturn(Optional.of(TiltakType.EKSPEBIST));

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(forCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void benahdler_ekspert_bistand_dersom_melding_kommer_nøyaktig_på_tidspunktet() {
        Instant cutoff = Instant.parse("2026-02-23T10:00:00.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode(TiltakType.EKSPEBIST.getTiltakskode()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        when(tilsagnJsonMapper.hentTiltakType(tub)).thenReturn(Optional.of(TiltakType.EKSPEBIST));

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(cutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, never()).utfoerOppgaver(tub);
    }

    @Test
    public void behandler_alle_andre_tiltakstyper_som_vanlig_etter_cut_off_date() {
        Instant etterCutoff = Instant.parse("2026-02-24T10:00:00.00Z");
        Tilsagn tilsagn = Tilsagn.builder().tiltakKode("MIDLONTIL").build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(tilsagn)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        when(tilsagnJsonMapper.hentTiltakType(tub)).thenReturn(Optional.of(TiltakType.MIDLONTIL));

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(etterCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

    @Test
    public void fortsetter_behandling_dersom_tiltakstype_ikke_kan_parses() {
        Instant etterCutoff = Instant.parse("2026-02-24T10:00:00.00Z");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder()
                .arenaMelding(Testdata.arenaMelding())
                .tilsagn(null)
                .build();

        when(tilsagnLoggRepository.registrerNyMelding(tub)).thenReturn(true);
        when(tilsagnJsonMapper.hentTiltakType(tub)).thenReturn(Optional.empty());

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(etterCutoff, tub);

        verify(tilsagnJsonMapper, times(1)).pakkUtArenaMelding(tub);
        verify(tilsagnLoggRepository, times(1)).registrerNyMelding(tub);
        verify(oppgaver, times(1)).utfoerOppgaver(tub);
    }

}

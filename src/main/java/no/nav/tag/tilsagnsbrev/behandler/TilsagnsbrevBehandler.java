package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.TilsagnException;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TiltakType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
public class TilsagnsbrevBehandler {
    /**
     * Fra dette tidspunktet tar Team Fager over behandlingen av ekspertbistand.
     */
    private static final Instant STOPP_BEHANDLING_AV_EKSPERTBISTAND = Instant.parse("2026-02-23T10:00:00.00Z");

    @Autowired
    private Oppgaver oppgaver;

    @Autowired
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    public void behandleOgVerifisereTilsagn(Instant kafkaRecordTidspunkt, TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            behandleTilsagn(kafkaRecordTidspunkt, tilsagnUnderBehandling);
        } catch (TilsagnException e) {
            oppgaver.oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        }
    }

    private void behandleTilsagn(Instant kafkaRecordTidspunkt, TilsagnUnderBehandling tilsagnUnderBehandling) {
        tilsagnJsonMapper.pakkUtArenaMelding(tilsagnUnderBehandling);

        if (skalBehandles(kafkaRecordTidspunkt, tilsagnUnderBehandling)) {
            oppgaver.utfoerOppgaver(tilsagnUnderBehandling);
        }
    }

    private boolean skalBehandles(
        Instant kafkaRecordTidspunkt,
        TilsagnUnderBehandling tilsagnUnderBehandling
    ) {
        boolean erNyMelding = tilsagnLoggRepository.registrerNyMelding(tilsagnUnderBehandling);
        if (!erNyMelding) {
            log.warn(
                "Melding med tilsagnsbrev-id {} er blitt prosessert tidligere. Avbryter videre behandling.",
                tilsagnUnderBehandling.getTilsagnsbrevId()
            );
            return false;
        }

        boolean erEkspertbistand = tilsagnJsonMapper.hentTiltakType(tilsagnUnderBehandling).map(TiltakType::erEkspertbistand).orElse(false);
        boolean erEtterFagerHarTattOverEksperbistand = !kafkaRecordTidspunkt.isBefore(STOPP_BEHANDLING_AV_EKSPERTBISTAND);
        if (erEkspertbistand && erEtterFagerHarTattOverEksperbistand) {
            log.info(
                "Melding med tilsagnsbrev-id {} er av type ekspertbistand og er mottatt etter at vi har sluttet å behandle denne typen. Avbryter videre behandling.",
                tilsagnUnderBehandling.getTilsagnsbrevId()
            );
            return false;
        }

        return true;
    }
}

package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TilsagnsbrevBehandler {

    @Autowired
    private Oppgaver oppgaver;



    @Autowired
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    public void behandleOgVerifisereTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            behandleTilsagn(tilsagnUnderBehandling);
        } catch (Exception e) {
            oppgaver.oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        } finally {
            tilsagnLoggRepository.lagretIdHvisNyMelding(tilsagnUnderBehandling); //TODO Trengs denne?
        }
    }

    private void behandleTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        tilsagnJsonMapper.pakkUtArenaMelding(tilsagnUnderBehandling);

        if(!lagreNyMeldingILogg(tilsagnUnderBehandling)) {
            return;
        }

        tilsagnJsonMapper.opprettTilsagn(tilsagnUnderBehandling);
        final byte[] pdf = oppgaver.opprettPdfDok(tilsagnUnderBehandling);

        try {
            oppgaver.journalfoerTilsagnsbrev(tilsagnUnderBehandling, pdf);
        } catch (Exception e) {
            oppgaver.oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        }
        oppgaver.sendTilAltinn(tilsagnUnderBehandling, pdf);
    }

    private boolean lagreNyMeldingILogg(TilsagnUnderBehandling tilsagnUnderBehandling){
        if (!tilsagnLoggRepository.lagretIdHvisNyMelding(tilsagnUnderBehandling)) {
            log.warn("Melding med tilsagnsbrev-id {} er blitt prosessert tidligere. Avbryter videre behandling.");
            return false;
        }
        return true;
    }
}

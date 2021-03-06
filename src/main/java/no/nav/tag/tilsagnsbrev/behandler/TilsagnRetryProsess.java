package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnBehandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling.MAX_RETRIES;

@Slf4j
@Component
@EnableScheduling
public class TilsagnRetryProsess {

    @Autowired
    private Oppgaver oppgaver;

    @Autowired
    private TilsagnBehandler tilsagnBehandler;

    @Autowired
    private FeilRapport feilRapport;

    @Scheduled(cron = "${tilsagnsbrev.retry.cron}")
    public void finnOgRekjoerFeiletTilsagn() {

        log.info("Starter retry");
        tilsagnBehandler.hentAlleTilRekjoring()
                .forEach(feiletTilsagnsbrev -> {
                    feiletTilsagnsbrev.increaseRetry();
                    log.info("Tilsagnsbrev {} retry no. {}", feiletTilsagnsbrev.getTilsagnsbrevId(), feiletTilsagnsbrev.getRetry());
                    oppgaver.utfoerOppgaver(feiletTilsagnsbrev);
                    evaluerRekjørtTilsagn(feiletTilsagnsbrev);
                });
    }


    private void evaluerRekjørtTilsagn(TilsagnUnderBehandling feiletTilsagnsbrev) {
        if (MAX_RETRIES == feiletTilsagnsbrev.getRetry() && !feiletTilsagnsbrev.isBehandlet()) {
            feilRapport.varsleProsessfeil(feiletTilsagnsbrev);
        }
    }
}

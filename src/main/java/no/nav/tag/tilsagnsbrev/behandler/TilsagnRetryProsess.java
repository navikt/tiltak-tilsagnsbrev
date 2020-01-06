package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class TilsagnRetryProsess {

    @Autowired
    Oppgaver oppgaver;

    @Autowired
    FeiletTilsagnBehandler feiletTilsagnBehandler;

//    @Scheduled(cron = "${tilsagnsbrev.retry.cron}")       //TODO Disablet ifbm. test av prodsetting
    public void finnOgRekjoerFeiletTilsagn() {
        log.info("Starter retry");
        feiletTilsagnBehandler.hentAlleTilRekjoring()
                .forEach(feiletTilsagnsbrev -> {
                    log.info("Tilsagnsbrev {} retry no. {}", feiletTilsagnsbrev.getTilsagnsbrevId(), feiletTilsagnsbrev.getRetry()); //TODO ordne på telling av retries
                    oppgaver.utfoerOppgaver(feiletTilsagnsbrev);
                });
    }

}

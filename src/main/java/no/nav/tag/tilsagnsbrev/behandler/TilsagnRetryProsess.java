package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class TilsagnRetryProsess {

    @Autowired
    private Oppgaver oppgaver;

    @Autowired
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Scheduled(cron = "${tilsagnsbrev.retry.cron}")
    public void finnOgRekjoerFeiletTilsagn() {

        log.info("Starter retry");
        feiletTilsagnBehandler.hentAlleTilRekjoring()
                .forEach(feiletTilsagnsbrev -> {
                    try {
                        feiletTilsagnsbrev.increaseRetry();
                        log.info("Tilsagnsbrev {} retry no. {}", feiletTilsagnsbrev.getTilsagnsbrevId(), feiletTilsagnsbrev.getRetry());
                        rekjoerTilsagn(feiletTilsagnsbrev);
                        log.info("Fullført retry på tilsagnsbrev {}.", feiletTilsagnsbrev.getTilsagnsbrevId());
                    } catch (Exception e) {
                        oppgaver.oppdaterFeiletTilsagn(feiletTilsagnsbrev, e);
                    }
                });
    }

    private void rekjoerTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {

        tilsagnJsonMapper.opprettTilsagn(tilsagnUnderBehandling);

        if(tilsagnUnderBehandling.manglerPdf()) {
            oppgaver.opprettPdfDok(tilsagnUnderBehandling);
        }

        if (tilsagnUnderBehandling.skaljournalfoeres()) {
            oppgaver.journalfoerTilsagnsbrev(tilsagnUnderBehandling);
        }

        if (tilsagnUnderBehandling.skalTilAltinn()) {
            oppgaver.sendTilAltinn(tilsagnUnderBehandling);
        }
        tilsagnUnderBehandling.setBehandlet(true);
        feiletTilsagnBehandler.oppdater(tilsagnUnderBehandling);
    }
}

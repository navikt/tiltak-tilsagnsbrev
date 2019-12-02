package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TilsagnRetryProsess {

    @Autowired
    Oppgaver oppgaver;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    FeiletTilsagnBehandler feiletTilsagnBehandler;

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;


    //@Scheduled(cron = "${prosess.cron}")
    public void finnOgRekjoerFeiletTilsagn() {
        feiletTilsagnBehandler.hentAlleTilRekjoring()
                .forEach(feiletTilsagnsbrev -> {
                    try {
                        rekjoerTilsagn(feiletTilsagnsbrev);
                    } catch (Exception e) {
                        oppgaver.oppdaterFeiletTilsagn(feiletTilsagnsbrev, e);
                    }
                });
    }

    private void rekjoerTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {

        tilsagnJsonMapper.opprettTilsagn(tilsagnUnderBehandling);
        final byte[] pdf = null;//pdfService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling);

        if (tilsagnUnderBehandling.skaljournalfoeres()) {
            oppgaver.journalfoerTilsagnsbrev(tilsagnUnderBehandling, pdf);
        }

        if (tilsagnUnderBehandling.skalTilAltinn()) {
            oppgaver.sendTilAltinn(tilsagnUnderBehandling, pdf);
        }
        tilsagnUnderBehandling.setBehandlet(true);
        feiletTilsagnBehandler.oppdater(tilsagnUnderBehandling);
    }
}

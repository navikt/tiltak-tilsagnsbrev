package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TilsagnsbrevBehandler {

    @Autowired
    private Oppgaver oppgaver;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    @Autowired
    private TilsagnLoggReository tilsagnLoggReository;

    public void behandleOgVerifisereTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            behandleTilsagn(tilsagnUnderBehandling);
        } catch (Exception e) {
            oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        } finally {
            //Lagre id
        }
    }

    private void behandleTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        oppgaver.arenaMeldingTilTilsagnData(tilsagnUnderBehandling);

        if(erHentetTidligere(tilsagnUnderBehandling)){
            log.info("Melding med tilsagnsbrev-id {} er blitt prosessert tidligere. Avbryter prosesseing av melding.");
            return;
        }



        log.info("Oppretter pdf av tilsagnsbrev for bedrift {}", tilsagnUnderBehandling.getTilsagn().getTiltakArrangor().getOrgNummer());
        final byte[] pdf = pdfService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling);

        try {
            oppgaver.journalfoerTilsagnsbrev(tilsagnUnderBehandling, pdf);
        } catch (Exception e) {
            oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        }
        oppgaver.sendTilAltinn(tilsagnUnderBehandling, pdf);
    }

    private boolean erHentetTidligere(TilsagnUnderBehandling tilsagnUnderBehandling) {
        tilsagnLoggReository.existsById(tilsagnUnderBehandling.)
    }

    private void oppdaterFeiletTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (!feiletTilsagnBehandler.lagreEllerOppdaterFeil(tilsagnUnderBehandling, e)) {
            log.error("Feil ble ikke lagret! Melding: {}", tilsagnUnderBehandling.getJson(), e.getMessage());
        }
    }
}

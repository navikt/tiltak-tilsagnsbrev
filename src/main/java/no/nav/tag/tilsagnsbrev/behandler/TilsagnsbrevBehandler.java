package no.nav.tag.tilsagnsbrev.behandler;

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
    private TilsagnLoggRepository tilsagnLoggRepository;

    public void behandleOgVerifisereTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            behandleTilsagn(tilsagnUnderBehandling);
        } catch (Exception e) {
            oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        } finally {
            tilsagnLoggRepository.lagretIdHvisNyMelding(tilsagnUnderBehandling);
        }
    }

    private void behandleTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        oppgaver.arenaMeldingTilTilsagnData(tilsagnUnderBehandling);

        if(tilsagnUnderBehandling.isDuplikat()){
            log.warn("Melding med tilsagnsbrev-id {} er blitt prosessert tidligere. Avbryter videre behandling.");
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

    private void oppdaterFeiletTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (!feiletTilsagnBehandler.lagreEllerOppdaterFeil(tilsagnUnderBehandling, e)) {
            log.error("Feil ble ikke lagret! Melding: {}", tilsagnUnderBehandling.getJson(), e.getMessage());
        }
    }
}

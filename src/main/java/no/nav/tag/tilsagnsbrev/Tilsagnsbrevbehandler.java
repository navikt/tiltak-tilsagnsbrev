package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Tilsagnsbrevbehandler {

    @Autowired
    Oppgaver oppgaver;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    public void behandleOgVerifisereTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            behandleTilsagn(tilsagnUnderBehandling);
        } catch (Exception e) {
            lagreFeiletTilsagn(tilsagnUnderBehandling, e);
        }
    }

    private void behandleTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        oppgaver.arenaMeldingTilTilsagnData(tilsagnUnderBehandling);
        final byte[] pdf = pdfService.tilsagnsbrevTilBase64EncodedPdfBytes(tilsagnUnderBehandling.getJson());
        oppgaver.journalfoerTilsagnsbrev(tilsagnUnderBehandling, pdf);
        oppgaver.sendTilAltinn(tilsagnUnderBehandling, pdf);
    }

    private void lagreFeiletTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (!feiletTilsagnBehandler.lagreFeil(tilsagnUnderBehandling, e)) {
            log.error("Feil ble ikke lagret! Melding fra Arena: {}", tilsagnUnderBehandling.getJson(), e.getMessage());
        }
    }
}

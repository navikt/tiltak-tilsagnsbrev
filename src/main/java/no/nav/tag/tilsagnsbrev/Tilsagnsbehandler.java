package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnXmlMapper;
import no.nav.tag.tilsagnsbrev.mapper.journalpost.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.TIL_ALTINN;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    private FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private TilsagnXmlMapper tilsagnXmlMapper;

    @Autowired
    private TilsagnJournalpostMapper tilsagnJournalpostMapper;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    @Autowired
    private JoarkService joarkService;

    public void behandleOgVerifisereTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            behandleTilsagn(tilsagnUnderBehandling);
        } catch (Exception e) {
            if (!feiletTilsagnBehandler.lagreFeil(tilsagnUnderBehandling, e)) {
                log.error("Feil ble ikke lagret! Melding fra Arena: {}", tilsagnUnderBehandling.getJson(), e.getMessage());
            }
        }
    }

    private void behandleTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);
        tilsagnJsonMapper.tilsagnTilPdfJson(tilsagnUnderBehandling);

        final byte[] pdf = pdfService.tilsagnTilPdfBrev(tilsagnUnderBehandling.getJson());

        Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tilsagnUnderBehandling.getTilsagn(), pdf);
        sendJournalpost(journalpost, tilsagnUnderBehandling);

        final String tilsagnXml = tilsagnXmlMapper.tilAltinnMelding(tilsagnUnderBehandling.getTilsagn(), pdf);
       // sendTilAltinn(tilsagnXml, tilsagn);
    }

    private void sendJournalpost(Journalpost journalpost, TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            joarkService.sendJournalpost(journalpost);
            tilsagnUnderBehandling.setNesteSteg(TIL_ALTINN);
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    private void sendTilAltinn(String tilsagnXml, Tilsagn tilsagn) {
        try {
            altInnService.sendTilsagnsbrev(tilsagnXml);
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }
}

package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrev;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnTilAltinnXml;
import no.nav.tag.tilsagnsbrev.mapping.journalpost.TilsagnTilJournalpost;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    private FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private TilsagnTilAltinnXml tilsagnTilAltinnXml;

    @Autowired
    private TilsagnTilJournalpost tilsagnTilJournalpost;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private JoarkService joarkService;

    public void behandleOgVerifisere(String goldenGateJson){
        try {
            behandleTilsagn(goldenGateJson);
        } catch (Exception e){
            log.info("Legger tilsagnsbrev i feil-logg");
        }
    }

    public void behandleTilsagn(String goldenGateJson) {



        final Tilsagn tilsagn = hentTilsagn(goldenGateJson);



        final String tilsagnJson = tilsagnJsonMapper.tilsagnTilPdfJson(tilsagn);

        log.info("Tilsagnsbrev til pdfGen: {}", tilsagn.getTilsagnNummer());

        //TODO Til pdf-gen er klar
        final byte[] pdf = pdfService.tilsagnTilPdfBrev(tilsagnJson);

        final String tilsagnXml = tilsagnTilAltinnXml.tilAltinnMelding(tilsagn, pdf);

        altInnService.sendTilsagnsbrev(tilsagnXml);
        joarkService.sendJournalpost(tilsagnTilJournalpost.konverterTilJournalpost(tilsagn, pdf));

    }

        private Tilsagn hentTilsagn(String goldengateJson){
            Tilsagn tilsagn
            try {
               tilsagn  = tilsagnJsonMapper.goldengateMeldingTilTilsagn(goldengateJson);
            } catch (Exception e){
                FeiletTilsagnsbrev feiletTilsagnsbrev = new FeiletTilsagnsbrev()
            }
        }
}

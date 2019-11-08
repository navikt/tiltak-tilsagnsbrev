package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrev;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnTilAltinnXml;
import no.nav.tag.tilsagnsbrev.mapping.journalpost.TilsagnTilJournalpost;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.OK;
import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.TIL_ALTINN;


public class TilsagnRetryProsess {

    @Autowired
    FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    PdfGenService pdfGenService;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    TilsagnTilAltinnXml tilsagnTilAltinnXml;

    @Autowired
    private TilsagnTilJournalpost tilsagnTilJournalpost;

    //@Scheduled(cron = "${prosess.cron}")
    public void finnOgRekjoerFeiletTilsagn(){
        List<FeiletTilsagnsbrev> feilListe = feiletTilsagnsbrevRepository.findAll();

        feilListe.stream().filter(FeiletTilsagnsbrev::skalRekjoeres).forEach(feiletTilsagnsbrev -> rekjoerTilsagn(feiletTilsagnsbrev));

        //feiletTilsagnsbrevRepository.deleteById(feiletTilsagn.getId());
    }

    private void rekjoerTilsagn(FeiletTilsagnsbrev feiletTilsagnsbrev){
        Tilsagn tilsagn = tilsagnJsonMapper.tilsagnJsonTilTilsagn(feiletTilsagnsbrev.getTilsagnJson());
        byte[] pdf = pdfGenService.tilsagnTilPdfBrev(feiletTilsagnsbrev.getTilsagnJson());

        if(feiletTilsagnsbrev.skaljournalfoeres()){
            Journalpost journalpost = tilsagnTilJournalpost.konverterTilJournalpost(tilsagn, pdf);
            joarkService.sendJournalpost(journalpost);
            feiletTilsagnsbrev.setNesteSteg(TIL_ALTINN);
        }

        if(feiletTilsagnsbrev.skalTilAltinn()){
            String altInnXml = tilsagnTilAltinnXml.tilAltinnMelding(tilsagn, pdf);
            altInnService.sendTilsagnsbrev(altInnXml);
            feiletTilsagnsbrev.setNesteSteg(OK);
        }


    }
}

package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnTilAltinnXml;
import no.nav.tag.tilsagnsbrev.mapping.journalpost.TilsagnTilJournalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.OK;
import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.TIL_ALTINN;


public class FeiletTilsagnsBehandler {

    @Autowired
    FeiletTilsagnRepository feiletTilsagnRepository;

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
        List<FeiletTilsagn> feilListe = feiletTilsagnRepository.findAll();

        feilListe.stream().filter(FeiletTilsagn::skalRekjoeres).forEach(feiletTilsagn -> rekjoerTilsagn(feiletTilsagn));

        //feiletTilsagnRepository.deleteById(feiletTilsagn.getId());
    }

    private void rekjoerTilsagn(FeiletTilsagn feiletTilsagn){
        Tilsagn tilsagn = tilsagnJsonMapper.tilsagnJsonTilTilsagn(feiletTilsagn.getTilsagnJson());
        byte[] pdf = pdfGenService.tilsagnTilPdfBrev(feiletTilsagn.getTilsagnJson());

        if(feiletTilsagn.skaljournalfoeres()){
            Journalpost journalpost = tilsagnTilJournalpost.konverterTilJournalpost(tilsagn, pdf);
            joarkService.sendJournalpost(journalpost);
            feiletTilsagn.setNesteSteg(TIL_ALTINN);
        }

        if(feiletTilsagn.skalTilAltinn()){
            String altInnXml = tilsagnTilAltinnXml.tilAltinnMelding(tilsagn, pdf);
            altInnService.sendTilsagnsbrev(altInnXml);
            feiletTilsagn.setNesteSteg(OK);
        }


    }
}

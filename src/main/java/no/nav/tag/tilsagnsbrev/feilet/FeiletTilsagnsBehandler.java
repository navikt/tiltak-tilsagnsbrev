package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

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

    @Scheduled(cron = "${prosess.cron}")
    public void finnOgRekjoerFeiletTilsagn(){
        List<FeiletTilsagn> feilListe = feiletTilsagnRepository.finnTilsagnTilRekjoring();

        feilListe.forEach(feiletTilsagn -> rekjoerTilsagn(feiletTilsagn));
    }

    private void rekjoerTilsagn(FeiletTilsagn feiletTilsagn){
        byte[] pdf = pdfGenService.tilsagnTilPdfBrev(feiletTilsagn.getTilsagnJson());

        if(feiletTilsagn.skaljournalfoeres()){
            joarkService.opprettOgSendJournalpost(null, tilsagnJsonMapper.tilsagnJsonTilTilsagn(feiletTilsagn.getTilsagnJson()), pdf);
            feiletTilsagn.setNesteSteg(TIL_ALTINN);
        }

        if(feiletTilsagn.skalTilAltinn()){
            altInnService.sendTilsagnsbrev(null);
        }

        feiletTilsagnRepository.deleteById(feiletTilsagn.getId());

    }
}

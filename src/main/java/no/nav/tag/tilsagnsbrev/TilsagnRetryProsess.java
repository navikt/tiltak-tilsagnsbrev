package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnXmlMapper;
import no.nav.tag.tilsagnsbrev.mapper.journalpost.TilsagnJournalpostMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.START;
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
    TilsagnXmlMapper tilsagnXmlMapper;

    @Autowired
    private TilsagnJournalpostMapper tilsagnJournalpostMapper;

    //@Scheduled(cron = "${prosess.cron}")
    public void finnOgRekjoerFeiletTilsagn(){
        List<TilsagnUnderBehandling> feilListe = feiletTilsagnsbrevRepository.findAll();

        feilListe.stream().filter(TilsagnUnderBehandling::skalRekjoeres).forEach(feiletTilsagnsbrev -> rekjoerTilsagn(feiletTilsagnsbrev));

        //feiletTilsagnsbrevRepository.deleteById(feiletTilsagn.getId());
    }

    private void rekjoerTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling){

        if(tilsagnUnderBehandling.erDefualt()){
            tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);
        }

        byte[] pdf = pdfGenService.tilsagnTilPdfBrev(tilsagnUnderBehandling.getJson());

        if(tilsagnUnderBehandling.skaljournalfoeres()){
            Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tilsagnUnderBehandling.getTilsagn(), pdf);
            joarkService.sendJournalpost(journalpost);
            tilsagnUnderBehandling.setNesteSteg(TIL_ALTINN);
        }

        if(tilsagnUnderBehandling.skalTilAltinn()){
            String altInnXml = tilsagnXmlMapper.tilAltinnMelding(tilsagnUnderBehandling.getTilsagn(), pdf);
            altInnService.sendTilsagnsbrev(altInnXml);
            tilsagnUnderBehandling.setNesteSteg(START);
        }


    }


}

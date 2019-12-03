package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Oppgaver {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private PdfGenService pdfService;

    @Autowired
    private JoarkService joarkService;

    @Autowired
    private AltInnService altInnService;

    @Autowired
    private TilsagnJournalpostMapper tilsagnJournalpostMapper;

    @Autowired
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Autowired
    private FeiletTilsagnBehandler feiletTilsagnBehandler;

    public byte[] opprettPdfDok(TilsagnUnderBehandling tilsagnUnderBehandling){
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tilsagnUnderBehandling);
        return pdfService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling, pdfJson);
    }


    public void journalfoerTilsagnsbrev(TilsagnUnderBehandling tilsagnUnderBehandling, byte[] pdf) {
        try {
            Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tilsagnUnderBehandling.getTilsagn(), pdf);
            tilsagnUnderBehandling.setJournalpostId(joarkService.sendJournalpost(journalpost));
            log.info("Journalført tilsagnsbrev-id {}, journalpostId: {}", tilsagnUnderBehandling.getTilsagnsbrevId(), tilsagnUnderBehandling.getJournalpostId());
        }catch (DataException de){
            throw de;
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    public void sendTilAltinn(TilsagnUnderBehandling tilsagnUnderBehandling, byte[] pdf) {
        log.info("Sender tilsagnsbrev {} til Altinn", tilsagnUnderBehandling.getTilsagnsbrevId());
        InsertCorrespondenceBasicV2 wsRequest = mapTilWebserviceRequest(tilsagnUnderBehandling, pdf);
        sentWsRequest(tilsagnUnderBehandling, wsRequest);
        log.info("Tilsagnsbrev {} er sendt til Altinn. kvittering {}",tilsagnUnderBehandling.getTilsagnsbrevId(), tilsagnUnderBehandling.getAltinnKittering());
    }


    private InsertCorrespondenceBasicV2 mapTilWebserviceRequest(TilsagnUnderBehandling tilsagnUnderBehandling, byte[] pdf) {
        try {
            return tilsagnTilAltinnMapper.tilAltinnMelding(tilsagnUnderBehandling.getTilsagn(), pdf);
        } catch (Exception e) {
            throw new DataException(e.getMessage());
        }
    }

    private void sentWsRequest(TilsagnUnderBehandling tilsagnUnderBehandling, InsertCorrespondenceBasicV2 wsRequest) {
        try{
            int kvitteringId = altInnService.sendTilsagnsbrev(wsRequest);
            tilsagnUnderBehandling.setAltinnKittering(kvitteringId);
        } catch (Exception e) {
            log.error("Feil ved sending av tilsagnsbrev {} til Altinn", tilsagnUnderBehandling.getTilsagnsbrevId(), e);
            throw new SystemException(e.getMessage());
        }
    }

    public void oppdaterFeiletTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (!feiletTilsagnBehandler.lagreEllerOppdaterFeil(tilsagnUnderBehandling, e)) {
            log.error("Feil ble ikke lagret! Melding: {}", tilsagnUnderBehandling.getJson(), e.getMessage());
        }
    }
}

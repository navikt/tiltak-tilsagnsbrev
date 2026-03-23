package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
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
    private TilsagnBehandler tilsagnBehandler;

    private void opprettPdfDok(TilsagnUnderBehandling tilsagnUnderBehandling){
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tilsagnUnderBehandling);
        pdfService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling, pdfJson);
    }

    private void journalfoerTilsagnsbrev(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tilsagnUnderBehandling);
            tilsagnUnderBehandling.setJournalpostId(joarkService.sendJournalpost(journalpost));
            log.info("Journalført tilsagnsbrev-id {}, journalpostId: {}", tilsagnUnderBehandling.getTilsagnsbrevId(), tilsagnUnderBehandling.getJournalpostId());
        }catch (DataException de){
            throw de;
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    private void sendTilAltinn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        InsertCorrespondenceBasicV2 wsRequest = mapTilWebserviceRequest(tilsagnUnderBehandling);
        log.info("Sender tilsagnsbrev {} til Altinn. Ekstern-ref {}", tilsagnUnderBehandling.getTilsagnsbrevId(), wsRequest.getExternalShipmentReference());
        sentWsRequest(tilsagnUnderBehandling, wsRequest);
        log.info("Tilsagnsbrev {} er sendt til Altinn. Referanse {}",tilsagnUnderBehandling.getTilsagnsbrevId(), tilsagnUnderBehandling.getAltinnReferanse());
    }


    private InsertCorrespondenceBasicV2 mapTilWebserviceRequest(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            return tilsagnTilAltinnMapper.tilAltinnMelding(tilsagnUnderBehandling.getTilsagn(), tilsagnUnderBehandling.getPdf());
        } catch (Exception e) {
            log.error("Feil ved oppretterlse av Altinn melding fra Tilsagn id {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e);
            throw new DataException(e.getMessage());
        }
    }

    private void sentWsRequest(TilsagnUnderBehandling tilsagnUnderBehandling, InsertCorrespondenceBasicV2 wsRequest) {
        try{
            int kvitteringId = altInnService.sendTilsagnsbrev(wsRequest);
            tilsagnUnderBehandling.setAltinnReferanse(kvitteringId);
        } catch (Exception e) {
            log.error("Feil ved sending av tilsagnsbrev {} til Altinn", tilsagnUnderBehandling.getTilsagnsbrevId(), e);
            throw new SystemException(e.getMessage());
        }
    }

    public void oppdaterFeiletTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling, Exception e) {
        if (!tilsagnBehandler.lagreEllerOppdaterFeil(tilsagnUnderBehandling, e)) {
            log.error("Feil ble ikke lagret! Melding: {}", tilsagnUnderBehandling.getJson(), e.getMessage());
        }
    }

    public void utfoerOppgaver(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            tilsagnJsonMapper.opprettTilsagn(tilsagnUnderBehandling);

            if (tilsagnUnderBehandling.getTilsagn().getTiltakType().erEkspertbistand()) {
                log.info(
                    "Melding med tilsagnsbrev-id {} er av type ekspertbistand og er mottatt etter " +
                    "at Team-Fager har tatt over behandling av denne typen. Avbryter videre behandling.",
                    tilsagnUnderBehandling.getTilsagnsbrevId()
                );
                return;
            }

            if (tilsagnUnderBehandling.manglerPdf()) {
                opprettPdfDok(tilsagnUnderBehandling);
            }

            if (tilsagnUnderBehandling.skaljournalfoeres()) {
                journalfoerTilsagnsbrev(tilsagnUnderBehandling);
            }

            if (tilsagnUnderBehandling.skalTilAltinn()) {
                sendTilAltinn(tilsagnUnderBehandling);
            }
            tilsagnUnderBehandling.setBehandlet(true);
            tilsagnBehandler.lagreStatus(tilsagnUnderBehandling);
            log.info("Fullført behandling av tilsagnsbrev {}.", tilsagnUnderBehandling.getTilsagnsbrevId());
        } catch (Exception e) {
            oppdaterFeiletTilsagn(tilsagnUnderBehandling, e);
        }
    }
}

package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
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
import no.nav.tag.tilsagnsbrev.service.PersondataService;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
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

    @Autowired
    private PersondataService persondataService;

    private void opprettPdfDok(TilsagnUnderBehandling tilsagnUnderBehandling){
        Tilsagn tilsagn = tilsagnUnderBehandling.getTilsagn();
        Integer tilsagnsbrevId = tilsagnUnderBehandling.getTilsagnsbrevId();

        // Altinn PDF (ingen sladding)
        if (tilsagnUnderBehandling.getPdfAltinn() == null) {
            String pdfJsonAltinn = tilsagnJsonMapper.opprettPdfJson(tilsagnsbrevId, tilsagn);
            byte[] altinnPdf = pdfService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling, pdfJsonAltinn);
            tilsagnUnderBehandling.setPdfAltinn(altinnPdf);
        }
        // Joark/Gosys PDF (sladding av deltaker ved diskresjon)
        if (tilsagnUnderBehandling.getPdfJoark() == null) {
            Tilsagn tilsagnJoark = tilsagnUnderBehandling.getDiskresjonskode().erKode6Eller7() ? tilsagn.getSladdetVersjon() : tilsagn;
            String pdfJsonJoark = tilsagnJsonMapper.opprettPdfJson(tilsagnsbrevId, tilsagnJoark);
            byte[] pdfJoark = pdfService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling, pdfJsonJoark);
            tilsagnUnderBehandling.setPdfJoark(pdfJoark);
        }
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
            return tilsagnTilAltinnMapper.tilAltinnMelding(tilsagnUnderBehandling.getTilsagn(), tilsagnUnderBehandling.getPdfAltinn());
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
            log.error("Feil ble ikke lagret!", e);
        }
    }

    private void hentDiskresjonskode(TilsagnUnderBehandling tilsagnUnderBehandling) {
        if (tilsagnUnderBehandling.getTilsagn() == null || tilsagnUnderBehandling.getTilsagn().getDeltaker() == null) {
            tilsagnUnderBehandling.setDiskresjonskode(Diskresjonskode.UGRADERT);
            return;
        }

        String fnr = tilsagnUnderBehandling.getTilsagn().getDeltaker().getFodselsnr();
        if (fnr == null) {
            throw new SystemException("Klarte ikke utlede diskresjonskode for deltaker. Deltaker fnr er null");
        }

        Diskresjonskode diskresjonskode = persondataService.hentDiskresjonskode(fnr);
        tilsagnUnderBehandling.setDiskresjonskode(diskresjonskode);
    }

    public void utfoerOppgaver(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            tilsagnJsonMapper.opprettTilsagn(tilsagnUnderBehandling);

            if (tilsagnUnderBehandling.manglerDiskresjonskode()) {
                hentDiskresjonskode(tilsagnUnderBehandling);
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

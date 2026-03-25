package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnAttachmentInitRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceRequest;
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

import java.util.UUID;

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
        log.info("Laster opp vedlegg for tilsagnsbrev {} til Altinn", tilsagnUnderBehandling.getTilsagnsbrevId());
        AltinnAttachmentInitRequest vedlegg = mapTilVedlegg(tilsagnUnderBehandling);
        UUID vedleggId = sendVedlegg(tilsagnUnderBehandling, vedlegg);
        log.info("Vedlegg for tilsagnsbrev {} er lastet oppt til Altinn med id {}", tilsagnUnderBehandling.getTilsagnsbrevId(), vedleggId);
        log.info("Sender tilsagnsbrev {} til Altinn", tilsagnUnderBehandling.getTilsagnsbrevId());
        AltinnCorrespondenceRequest korrespondanse = mapTilKorrespondanse(tilsagnUnderBehandling, vedleggId);
        sendKorrespondanse(tilsagnUnderBehandling, korrespondanse);
        log.info("Tilsagnsbrev {} er sendt til Altinn. Referanse {}", tilsagnUnderBehandling.getTilsagnsbrevId(), tilsagnUnderBehandling.getAltinnReferanse());
    }


    private AltinnCorrespondenceRequest mapTilKorrespondanse(TilsagnUnderBehandling tilsagnUnderBehandling, UUID vedleggId) {
        try {
            return tilsagnTilAltinnMapper.tilAltinnKorrespondanse(tilsagnUnderBehandling.getTilsagn(), vedleggId);
        } catch (Exception e) {
            log.error("Feil ved opprettelse av Altinn melding fra Tilsagn id {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e);
            throw new DataException(e.getMessage());
        }
    }

    private AltinnAttachmentInitRequest mapTilVedlegg(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            return tilsagnTilAltinnMapper.tilAltinnVedlegg(tilsagnUnderBehandling.getTilsagn());
        } catch (Exception e) {
            log.error("Feil ved opprettelse av Altinn vedlegg fra Tilsagn id {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e);
            throw new DataException(e.getMessage());
        }
    }

    private UUID sendVedlegg(TilsagnUnderBehandling tilsagnUnderBehandling, AltinnAttachmentInitRequest vedlegg) {
        try {
            return altInnService.sendVedlegg(vedlegg, tilsagnUnderBehandling.getPdf());
        } catch (Exception e) {
            log.error("Feil ved opplasting av vedlegg {} til Altinn", tilsagnUnderBehandling.getTilsagnsbrevId(), e);
            throw new SystemException(e.getMessage());
        }
    }

    private void sendKorrespondanse(TilsagnUnderBehandling tilsagnUnderBehandling, AltinnCorrespondenceRequest korrespondanse) {
        try {
            String correspondenceId = altInnService.sendTilsagnsbrev(korrespondanse);
            tilsagnUnderBehandling.setAltinnReferanse(correspondenceId);
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

            if(tilsagnUnderBehandling.manglerPdf()) {
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

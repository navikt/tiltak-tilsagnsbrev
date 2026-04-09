package no.nav.tag.tilsagnsbrev.behandler;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnAttachmentInitRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceRequest;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.exception.SystemException;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnBehandler;
import no.nav.tag.tilsagnsbrev.integrasjon.AltinnService;
import no.nav.tag.tilsagnsbrev.integrasjon.JoarkService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.integrasjon.persondata.PersondataService;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
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
    private AltinnService altInnService;

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
            throw new SystemException(e.getMessage(), e);
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
            return tilsagnTilAltinnMapper.tilAltinnKorrespondanse(tilsagnUnderBehandling.getTilsagn(), vedleggId, tilsagnUnderBehandling.getCid());
        } catch (Exception e) {
            log.error("Feil ved opprettelse av Altinn melding fra tilsagn-id {} og melding: {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
    }

    private AltinnAttachmentInitRequest mapTilVedlegg(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            return tilsagnTilAltinnMapper.tilAltinnVedlegg(tilsagnUnderBehandling.getTilsagn(), tilsagnUnderBehandling.getPdfAltinn());
        } catch (Exception e) {
            log.error("Feil ved opprettelse av Altinn vedlegg fra tilsagn-id {} og melding: {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e.getMessage(), e);
            throw new DataException(e.getMessage(), e);
        }
    }

    private UUID sendVedlegg(TilsagnUnderBehandling tilsagnUnderBehandling, AltinnAttachmentInitRequest vedlegg) {
        try {
            return altInnService.sendVedlegg(vedlegg, tilsagnUnderBehandling.getPdfAltinn());
        } catch (Exception e) {
            log.error("Feil ved opplasting av vedlegg {} til Altinn med melding: {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e.getMessage(), e);
            throw new SystemException(e.getMessage(), e);
        }
    }

    private void sendKorrespondanse(TilsagnUnderBehandling tilsagnUnderBehandling, AltinnCorrespondenceRequest korrespondanse) {
        try {
            String correspondenceId = altInnService.sendTilsagnsbrev(korrespondanse);
            tilsagnUnderBehandling.setAltinnReferanse(correspondenceId);
        } catch (Exception e) {
            log.error("Feil ved sending av tilsagnsbrev {} til Altinn med melding: {}", tilsagnUnderBehandling.getTilsagnsbrevId(), e.getMessage(), e);
            throw new SystemException(e.getMessage(), e);
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

            if (tilsagnUnderBehandling.getTilsagn().getTiltakType().erEkspertbistand()) {
                log.info(
                    "Melding med tilsagnsbrev-id {} er av type ekspertbistand og er mottatt etter " +
                    "at Team-Fager har tatt over behandling av denne typen. Avbryter videre behandling.",
                    tilsagnUnderBehandling.getTilsagnsbrevId()
                );
                return;
            }

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

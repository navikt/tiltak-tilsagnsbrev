package no.nav.tag.tilsagnsbrev.mapper;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnNummer;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static no.nav.tag.tilsagnsbrev.mapper.TiltakType.*;

@Slf4j
@Component
public class TilsagnJournalpostMapper {

    private Map<String, TiltakType> tiltakType = new HashMap();

    public TilsagnJournalpostMapper() {
        initTiltakTyper();
    }

    public Journalpost tilsagnTilJournalpost(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            return opprettJournalpost(tilsagnUnderBehandling.getTilsagn(), tilsagnUnderBehandling.getPdf(), tilsagnUnderBehandling.getTilsagnsbrevId());
        } catch (Exception e) {
            log.error("Feil ved mapping til Journalpost", e);
            throw new DataException(e.getMessage());
        }
    }

    private Journalpost opprettJournalpost(Tilsagn tilsagnsbrev, final byte[] pdfBytes, Integer tilsagnsbrevId) {
        String eksternReferanseId = tilsagnsbrevId + "-tilsagnsbrev";
        Sak sak = new Sak(opprettArkivsaknr(tilsagnsbrev.getTilsagnNummer()));
        Bruker bruker = new Bruker(tilsagnsbrev.getTiltakArrangor().getOrgNummer());
        Mottaker mottaker = new Mottaker(tilsagnsbrev.getTiltakArrangor().getOrgNummer(), tilsagnsbrev.getTiltakArrangor().getArbgiverNavn());
        final String base64EncodetPdf = Base64.getEncoder().encodeToString(pdfBytes);
        TiltakType tiltakType = getTiltakType(tilsagnsbrev.getTiltakKode());
        Dokument dokument = new Dokument(tiltakType.getTittel(), tiltakType.getBrevkode(), Collections.singletonList(new DokumentVariant(base64EncodetPdf)));
        Journalpost journalpost = new Journalpost(tiltakType.getTittel(), bruker, mottaker, sak, Collections.singletonList(dokument), eksternReferanseId);
        return journalpost;
    }

    private String opprettArkivsaknr(TilsagnNummer tilsagnNr) {
        return new StringBuilder(tilsagnNr.getAar()).append(tilsagnNr.getLoepenrSak()).toString();
    }

    private void initTiltakTyper() {
        tiltakType.put(ARBFORB.getTiltakskode(), ARBFORB);
        tiltakType.put(EKSPEBIST.getTiltakskode(), EKSPEBIST);
        tiltakType.put(FUNKSJASS.getTiltakskode(), FUNKSJASS);
        tiltakType.put(INKLUTILS.getTiltakskode(), INKLUTILS);
        tiltakType.put(MENTOR.getTiltakskode(), MENTOR);
        tiltakType.put(MIDLONTIL.getTiltakskode(), MIDLONTIL);
        tiltakType.put(ENKELAMO.getTiltakskode(), ENKELAMO);
        tiltakType.put(ENKFAGYRKE.getTiltakskode(), ENKFAGYRKE);
        tiltakType.put(GRUFAGYRKE.getTiltakskode(), GRUFAGYRKE);
        tiltakType.put(HOYEREUTD.getTiltakskode(), HOYEREUTD);
        tiltakType.put(VARLONTIL.getTiltakskode(), VARLONTIL);
        tiltakType.put(VASV.getTiltakskode(), VASV);
        tiltakType.put(VATIAROR.getTiltakskode(), VATIAROR);
        tiltakType.put(FORSAMOENK.getTiltakskode(), FORSAMOENK);
        tiltakType.put(FORSFAGGRU.getTiltakskode(), FORSFAGGRU);
        tiltakType.put(FORSFAGENK.getTiltakskode(), FORSFAGENK);
    }

    private TiltakType getTiltakType(String tiltakKode) {
        TiltakType tiltakType = this.tiltakType.get(tiltakKode);
        if (tiltakType == null) {
            throw new DataException("Ukjent tiltak-kode: " + tiltakKode);
        }
        return tiltakType;
    }
}
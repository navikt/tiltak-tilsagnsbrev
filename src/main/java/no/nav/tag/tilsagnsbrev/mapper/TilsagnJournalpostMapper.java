package no.nav.tag.tilsagnsbrev.mapper;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Bruker;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Dokument;
import no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Mottaker;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Sak;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnNummer;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;

@Slf4j
@Component
public class TilsagnJournalpostMapper {

    public Journalpost tilsagnTilJournalpost(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            return opprettJournalpost(tilsagnUnderBehandling.getTilsagn(), tilsagnUnderBehandling.getPdfJoark(), tilsagnUnderBehandling.getTilsagnsbrevId());
        } catch (Exception e) {
            log.error("Feil ved mapping til Journalpost", e);
            throw new DataException(e.getMessage());
        }
    }

    private Journalpost opprettJournalpost(Tilsagn tilsagnsbrev, final byte[] pdfBytes, Integer tilsagnsbrevId) {
        Sak sak = new Sak(opprettArkivsaknr(tilsagnsbrev.getTilsagnNummer()));
        Bruker bruker = new Bruker(tilsagnsbrev.getTiltakArrangor().getOrgNummer());
        Mottaker mottaker = new Mottaker(tilsagnsbrev.getTiltakArrangor().getOrgNummer(), tilsagnsbrev.getTiltakArrangor().getArbgiverNavn());
        final String base64EncodetPdf = Base64.getEncoder().encodeToString(pdfBytes);
        TiltakType tiltakType = TiltakType.parse(tilsagnsbrev.getTiltakKode());
        Dokument dokument = new Dokument(tiltakType.getTittel(), tiltakType.getBrevkode(), Collections.singletonList(new DokumentVariant(base64EncodetPdf)));
        String eksternReferanseId = tilsagnsbrevId + "-tilsagnsbrev";
        Journalpost journalpost = new Journalpost(tiltakType.getTittel(), bruker, mottaker, sak, Collections.singletonList(dokument), eksternReferanseId);
        return journalpost;
    }

    private String opprettArkivsaknr(TilsagnNummer tilsagnNr) {
        return new StringBuilder(tilsagnNr.getAar()).append(tilsagnNr.getLoepenrSak()).toString();
    }
}

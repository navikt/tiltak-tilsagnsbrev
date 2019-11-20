package no.nav.tag.tilsagnsbrev.mapper;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnNummer;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class TilsagnJournalpostMapper {

    public Journalpost tilsagnTilJournalpost(Tilsagn tilsagn,  final byte[] pdfBytes){
        try {
            return opprettJournalpost(tilsagn, pdfBytes);
        } catch (Exception e){
            log.error("Feil ved mapping til Journalpost", e);
            throw new DataException(e.getMessage());
        }
    }

    private Journalpost opprettJournalpost(Tilsagn tilsagnsbrev, final byte[] pdfBytes) { //TODO Utlede tittel fra tilsagnsbrevet
        Sak sak = new Sak(opprettArkivsaknr(tilsagnsbrev.getTilsagnNummer()));
        Bruker bruker = new Bruker(tilsagnsbrev.getTiltakArrangor().getOrgNummer());
        Mottaker mottaker = new Mottaker(tilsagnsbrev.getTiltakArrangor().getOrgNummer(), tilsagnsbrev.getTiltakArrangor().getArbgiverNavn());
        Dokument dokument = new Dokument(Journalpost.TITTEL, Collections.singletonList(new DokumentVariant(new String(pdfBytes))));
        Journalpost journalpost = new Journalpost(Journalpost.TITTEL, bruker, mottaker, sak, Collections.singletonList(dokument));
        return journalpost;
    }

    private String opprettArkivsaknr(TilsagnNummer tilsagnNr){
        return new StringBuilder(tilsagnNr.getAar()).append(tilsagnNr.getLoepenrSak()).append(tilsagnNr.getLoepenrTilsagn()).toString();
    }
}
package no.nav.tag.tilsagnsbrev.mapper.journalpost;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Bruker;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Dokument;
import no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;

import static no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant.FILTYPE_PDF;
import static no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant.VARIANFORMAT_PDF;

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

    private Journalpost opprettJournalpost(Tilsagn tilsagnsbrev, final byte[] pdfBytes) {

        Bruker bruker = new Bruker();
        bruker.setId(tilsagnsbrev.getTiltakArrangor().getOrgNummer());
        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Collections.singletonList(
                new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, Base64.getEncoder().encodeToString(pdfBytes)))
        );
        journalpost.setDokumenter(Collections.singletonList(dokument));
        return journalpost;
    }
}

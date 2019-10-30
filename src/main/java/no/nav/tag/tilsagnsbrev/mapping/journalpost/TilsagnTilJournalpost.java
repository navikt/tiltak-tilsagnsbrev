package no.nav.tag.tilsagnsbrev.mapping.journalpost;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Bruker;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Dokument;
import no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant.*;

@Slf4j
@Component
public class TilsagnTilJournalpost {

    public Journalpost konverterTilJournalpost(Tilsagn tilsagnsbrev, final byte[] pdfBytes) {

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

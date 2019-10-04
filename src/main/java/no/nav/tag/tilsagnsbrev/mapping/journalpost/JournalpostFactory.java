package no.nav.tag.tilsagnsbrev.mapping.journalpost;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.domene.Tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.domene.journalpost.Bruker;
import no.nav.tag.tilsagnsbrev.domene.journalpost.Dokument;
import no.nav.tag.tilsagnsbrev.domene.journalpost.DokumentVariant;
import no.nav.tag.tilsagnsbrev.domene.journalpost.Journalpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

import static no.nav.tag.tilsagnsbrev.domene.journalpost.DokumentVariant.*;

@Slf4j
@Component
public class JournalpostFactory {

    @Autowired
    private TilsagnsbrevTilXml tilsagnsbrevTilXml;

    public Journalpost konverterTilJournalpost(Tilsagnsbrev tilsagnsbrev, final byte[] pdfBytes) {

        Bruker bruker = new Bruker();
        bruker.setId(tilsagnsbrev.getOrgNummer());
        Journalpost journalpost = new Journalpost();
        journalpost.setBruker(bruker);

        final String dokumentXml = tilsagnsbrevTilXml.genererXml(tilsagnsbrev);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(
                new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML ,encodeToBase64(dokumentXml.getBytes())),
                new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, encodeToBase64(pdfBytes)))
        );
        journalpost.setDokumenter(Collections.singletonList(dokument));
        return journalpost;
    }

    private String encodeToBase64(final byte[] dokumentBytes) {
        return Base64.getEncoder().encodeToString(dokumentBytes);
    }
}

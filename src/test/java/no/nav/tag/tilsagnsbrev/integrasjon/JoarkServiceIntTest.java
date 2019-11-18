package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.journalpost.Bruker;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Dokument;
import no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static no.nav.tag.tilsagnsbrev.dto.journalpost.DokumentVariant.*;
import static org.junit.Assert.assertEquals;

@Ignore("Ikke klar")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class JoarkServiceIntTest {

    @Autowired
    private JoarkService joarkService;

    @Test
    public void oppretterJournalpost() {
        Journalpost journalpost = new Journalpost();

        Bruker bruker  = new Bruker();
        bruker.setId("88888899999");
        journalpost.setBruker(bruker);

        Dokument dokument = new Dokument();
        dokument.setDokumentVarianter(Arrays.asList(new DokumentVariant(FILTYPE_XML, VARIANFORMAT_XML, "xmlxmlxml"), new DokumentVariant(FILTYPE_PDF, VARIANFORMAT_PDF, "pdfpdfpdf")));
        journalpost.setDokumenter(Arrays.asList(dokument));

    //    String jounalpostId = joarkService.journalfoerTilsagnsbrev(journalpost);
    //    assertEquals("001", jounalpostId);
    }

}

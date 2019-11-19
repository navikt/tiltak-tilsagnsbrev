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

}

package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@DirtiesContext
@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
public class FeiletTilsagnsbrevBehandlerIntTest {

    @Autowired
    private FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    private FeiletTilsagnsBehandler feiletTilsagnsBehandler;

    @Before
    public void setUp(){
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        String tilsagnJson = Testdata.tilsagnTilJSON(tilsagn);
        FeiletTilsagnsbrev ikkeJournalfoert = new FeiletTilsagnsbrev(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.JOURNALFORES, 0, tilsagnJson);
        FeiletTilsagnsbrev hellerIkkeJournalfoert = new FeiletTilsagnsbrev(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.JOURNALFORES, 1, tilsagnJson);
        FeiletTilsagnsbrev ikkesendtTilAltInn = new FeiletTilsagnsbrev(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.JOURNALFORES, 2, tilsagnJson);
        FeiletTilsagnsbrev skalIkkeBehandles = new FeiletTilsagnsbrev(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.TIL_ALTINN, 3, tilsagnJson);
        feiletTilsagnsbrevRepository.saveAll(Arrays.asList(ikkeJournalfoert, hellerIkkeJournalfoert, ikkesendtTilAltInn, skalIkkeBehandles));
    }

    @Test
    public void behandlerIkkeJournalfoert(){
        feiletTilsagnsBehandler.finnOgRekjoerFeiletTilsagn();

        //List<FeiletTilsagnsbrev> feilListe = feiletTilsagnsbrevRepository.findAll().;


    }
}

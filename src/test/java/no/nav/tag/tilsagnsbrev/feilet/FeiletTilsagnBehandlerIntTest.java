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
public class FeiletTilsagnBehandlerIntTest {

    @Autowired
    private FeiletTilsagnRepository feiletTilsagnRepository;

    @Autowired
    private FeiletTilsagnsBehandler feiletTilsagnsBehandler;

    @Before
    public void setUp(){
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
        String tilsagnJson = Testdata.tilsagnTilJSON(tilsagn);
        FeiletTilsagn ikkeJournalfoert = new FeiletTilsagn(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.JOURNALFORES, 0, tilsagnJson);
        FeiletTilsagn hellerIkkeJournalfoert = new FeiletTilsagn(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.JOURNALFORES, 1, tilsagnJson);
        FeiletTilsagn ikkesendtTilAltInn = new FeiletTilsagn(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.JOURNALFORES, 2, tilsagnJson);
        FeiletTilsagn skalIkkeBehandles = new FeiletTilsagn(UUID.randomUUID(), LocalDateTime.now(), NesteSteg.TIL_ALTINN, 3, tilsagnJson);
        feiletTilsagnRepository.saveAll(Arrays.asList(ikkeJournalfoert, hellerIkkeJournalfoert, ikkesendtTilAltInn, skalIkkeBehandles));
    }

    @Test
    public void behandlerIkkeJournalfoert(){
        feiletTilsagnsBehandler.finnOgRekjoerFeiletTilsagn();

        //List<FeiletTilsagn> feilListe = feiletTilsagnRepository.findAll().;


    }
}

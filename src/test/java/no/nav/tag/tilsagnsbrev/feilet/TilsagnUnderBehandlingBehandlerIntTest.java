package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.TilsagnRetryProsess;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Before;
import org.junit.Ignore;
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

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.JOURNALFOER;

@Ignore

@SpringBootTest
@DirtiesContext
@ActiveProfiles({"dev"})
@RunWith(SpringRunner.class)
public class TilsagnUnderBehandlingBehandlerIntTest {

    @Autowired
    private FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Before
    public void setUp(){
        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
//        String json = Testdata.tilsagnTilJSON(tilsagn);
//        TilsagnUnderBehandling ikkeJournalfoert = TilsagnUnderBehandling.builder().id(UUID.randomUUID()).opprettet(LocalDateTime.now()).nesteSteg(JOURNALFOER).retry(0).build();
//        TilsagnUnderBehandling hellerIkkeJournalfoert = TilsagnUnderBehandling.builder().id(UUID.randomUUID()).feilmelding("Feil").opprettet(LocalDateTime.now()).setNesteSteg(1).setRetry(json).createFeiletTilsagnsbrev();
//        TilsagnUnderBehandling ikkesendtTilAltInn = new TilsagnUnderBehandlingBuilder().setId(UUID.randomUUID()).setFeilmelding(LocalDateTime.now()).setOpprettet(NesteSteg.JOURNALFORES).setNesteSteg(2).setRetry(json).createFeiletTilsagnsbrev();
//        TilsagnUnderBehandling skalIkkeBehandles = new TilsagnUnderBehandlingBuilder().setId(UUID.randomUUID()).setFeilmelding(LocalDateTime.now()).setOpprettet(NesteSteg.TIL_ALTINN).setNesteSteg(3).setRetry(json).createFeiletTilsagnsbrev();
//        feiletTilsagnsbrevRepository.saveAll(Arrays.asList(ikkeJournalfoert, hellerIkkeJournalfoert, ikkesendtTilAltInn, skalIkkeBehandles));
    }

    @Test
    public void behandlerIkkeJournalfoert(){
        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        //List<TilsagnUnderBehandling> feilListe = feiletTilsagnsbrevRepository.findAll().;


    }
}

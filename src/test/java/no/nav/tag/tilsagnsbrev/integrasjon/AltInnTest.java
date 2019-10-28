package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class AltInnTest {

    @Autowired
    AltInn altInn;

    @Test
    public void senderTilsagnsbrev(){
        Tilsagn tilsagn = new Tilsagn();
       altInn.sendTilsagnsbrev(tilsagn.toString());



    }

}

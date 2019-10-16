package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AltUtTest {

    AltUt altUt = new AltUt();

    @Test
    public void senderTilsagnsbrev(){

        Tilsagn tilsagn = new Tilsagn();
       altUt.sendTilsagnsbrev(tilsagn);



    }

}

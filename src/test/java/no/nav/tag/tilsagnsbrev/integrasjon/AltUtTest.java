package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AltUtTest {

    @Autowired
    AltUt altUt;

    @Test
    public void senderTilsagnsbrev(){

        Tilsagn tilsagn = new Tilsagn();
       altUt.sendTilsagnsbrev(tilsagn);



    }

}

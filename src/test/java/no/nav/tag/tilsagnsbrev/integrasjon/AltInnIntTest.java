package no.nav.tag.tilsagnsbrev.integrasjon;

import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class AltInnIntTest {

    @Autowired
    AltInnService altInnService;

    @Autowired
    TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Test
    public void senderTilsagnsbrev(){

        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
     //  byte[] pdf = Testdata.hentFilBytes("dummy.pdf");
    byte[] pdf = "pdf".getBytes();

        final byte[] base64Pdf = Base64.getEncoder().encode(pdf);
        InsertCorrespondenceBasicV2 insertCorrespondenceBasicV2  = tilsagnTilAltinnMapper.tilAltinnMelding(tilsagn, base64Pdf);

       altInnService.sendTilsagnsbrev(insertCorrespondenceBasicV2);



    }

}

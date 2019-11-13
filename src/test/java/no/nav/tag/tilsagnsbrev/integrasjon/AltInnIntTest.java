package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.altinn.InsertCorrespondenceBasicV2;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnXmlMapper;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class AltInnIntTest {

    @Autowired
    AltInnService altInnService;

    @Autowired
    TilsagnXmlMapper tilsagnXmlMapper;

    @Test
    @Ignore
    public void senderTilsagnsbrev(){

        Tilsagn tilsagn = Testdata.tilsagnEnDeltaker();
       byte[] pdf = Testdata.hentFilBytes("dummy.pdf");
   // byte[] pdf = "pdf".getBytes();

        InsertCorrespondenceBasicV2 soapEnvelope  = tilsagnXmlMapper.tilAltinnMelding(tilsagn, pdf);

       altInnService.sendTilsagnsbrev(soapEnvelope);



    }

}

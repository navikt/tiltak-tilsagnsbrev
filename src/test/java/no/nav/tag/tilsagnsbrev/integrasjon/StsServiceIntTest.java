package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.konfigurasjon.StsProperties;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class StsServiceIntTest {

    private StsService stsService;

    @Autowired
    StsProperties stsProperties;

    @Autowired
    public void setStsService(StsService stsService){
        this.stsService = stsService;
    }

    @Test
    public void henter_token() {
        String token = stsService.hentToken();
        assertEquals("eyxXxx", token);
    }

}

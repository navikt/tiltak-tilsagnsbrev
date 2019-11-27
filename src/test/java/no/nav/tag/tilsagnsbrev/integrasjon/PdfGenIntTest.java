package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class PdfGenIntTest {

    @Autowired
    IntegrasjonerMockServer mockServer;

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    PdfGenService pdfGenService;

    @Before
    public void setUp(){
        mockServer.stubForAltOk();
    }

    @Test
    public void returnererPdfBytes(){
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().tilsagn(Testdata.tilsagnEnDeltaker()).build();
        tilsagnJsonMapper.tilsagnTilJson(tilsagnUnderBehandling);
        System.out.println(tilsagnUnderBehandling.getJson());
        byte[] pdfBytes = pdfGenService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling);
        assertFalse(pdfBytes.length == 0);
    }

    @Test
    public void returnererPdfBytesForGruppe(){
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().tilsagn(Testdata.gruppeTilsagn()).build();
        tilsagnJsonMapper.tilsagnTilJson(tilsagnUnderBehandling);
        System.out.println(tilsagnUnderBehandling.getJson());
        byte[] pdfBytes = pdfGenService.tilsagnsbrevTilPdfBytes(tilsagnUnderBehandling);
        assertFalse(pdfBytes.length == 0);
    }
}

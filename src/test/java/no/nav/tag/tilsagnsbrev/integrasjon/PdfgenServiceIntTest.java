package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class PdfgenServiceIntTest {

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    PdfGenService pdfGenService;

    @Ignore("For manuell sjekk av pdf dokument")
    @Test
    public void oppretterRiktigPdf() throws IOException {
//        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(Testdata.tilsagnEnDeltaker()).build();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(Testdata.gruppeTilsagn()).build();
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tub);
        System.out.println(pdfJson);
        byte[] pdfBytes = pdfGenService.tilsagnsbrevTilPdfBytes(tub, pdfJson);

        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(pdfBytes));
        pdf.save("src/test/resources/Resultat.pdf");
        pdf.close();
    }
}

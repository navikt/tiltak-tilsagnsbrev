package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.Testdata;
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

/*
    Sjekk ut og kjør dockerimg til tag-dogGen. Kjør testene mot http://localhost:5913 i stedet for mockserver.
 */

public class PdfgenServiceIntTest {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private PdfGenService pdfGenService;

    @Ignore("For manuell sjekk av pdf dokument")
    @Test
    public void oppretterRiktigPdfForGruppe() throws IOException {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(Testdata.gruppeTilsagn()).build();
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tub);
        System.out.println(pdfJson);
        pdfGenService.tilsagnsbrevTilPdfBytes(tub, pdfJson);

        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(tub.getPdf()));
        pdf.save("src/test/resources/ResultatGrp.pdf");
        pdf.close();
    }

    @Ignore("For manuell sjekk av pdf dokument.")
    @Test
    public void oppretterRiktigPdfForDeltaker() throws IOException {
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(Testdata.tilsagnEnDeltaker()).build();
        tub.getTilsagn().setTiltakNavn("Ekspertbistand");
        tub.getTilsagn().setTiltakKode("EKSPEBIST");
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tub);
        System.out.println(pdfJson);
        pdfGenService.tilsagnsbrevTilPdfBytes(tub, pdfJson);

        PDDocument pdf = PDDocument.load(new ByteArrayInputStream(tub.getPdf()));
        pdf.save("src/test/resources/ResultatDelt.pdf");
        pdf.close();
    }
}

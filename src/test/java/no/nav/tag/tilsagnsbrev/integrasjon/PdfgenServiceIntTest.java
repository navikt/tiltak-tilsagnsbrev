package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
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
@Ignore("For manuell sjekk av pdf dokument")
public class PdfgenServiceIntTest {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private PdfGenService pdfGenService;

    private Tilsagn tilsagnDeltaker = Testdata.tilsagnEnDeltaker();
    private Tilsagn tilsagnGruppe = Testdata.gruppeTilsagn();

    @Test
    public void ARBFORB() {
        tilsagnGruppe.setTiltakNavn("Arbeidsforberedende trening");
        tilsagnGruppe.setTiltakKode("ARBFORB");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf("ARBFORB", tub);
    }

    @Test
    public void EKSPEBIST() {
        tilsagnDeltaker.setTiltakNavn("Ekspertbistand");
        tilsagnDeltaker.setTiltakKode("EKSPEBIST");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("EKSPEBIST", tub);
    }

    @Test
    public void FUNKSJASS() {
        tilsagnDeltaker.setTiltakNavn("funksjonsassistanse");
        tilsagnDeltaker.setTiltakKode("FUNKSJASS");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("FUNKSJASS", tub);
    }

    @Test
    public void INKLUTILS() {
        tilsagnDeltaker.setTiltakNavn("Inkluderingstilskudd");
        tilsagnDeltaker.setTiltakKode("INKLUTILS");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("INKLUTILS", tub);
    }

    @Test
    public void MENTOR() {
        tilsagnDeltaker.setTiltakNavn("Mentor");
        tilsagnDeltaker.setTiltakKode("MENTOR");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("MENTOR", tub);
    }

    @Test
    public void MIDLONTIL() {
        tilsagnDeltaker.setTiltakNavn("Midlertidig lønnstilskudd");
        tilsagnDeltaker.setTiltakKode("MIDLONTIL");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("MIDLONTIL", tub);
    }

    @Test
    public void ENKELAMO() {
        tilsagnDeltaker.setTiltakNavn("AOpplæring AMO");
        tilsagnDeltaker.setTiltakKode("ENKELAMO");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("ENKELAMO", tub);
    }

    @Test
    public void ENKFAGYRKE() {
        tilsagnDeltaker.setTiltakNavn("Enkeltplass fag og yrkesopplæring");
        tilsagnDeltaker.setTiltakKode("ENKFAGYRKE");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("ENKFAGYRKE", tub);
    }

    @Test
    public void GRUFAGYRKE() {
        tilsagnGruppe.setTiltakNavn("Gruppe fag og yrkesopplæring");
        tilsagnGruppe.setTiltakKode("GRUFAGYRKE");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf("GRUFAGYRKE", tub);
    }

    @Test
    public void HOYEREUTD() {
        tilsagnDeltaker.setTiltakNavn("Høyere utdanning");
        tilsagnDeltaker.setTiltakKode("HOYEREUTD");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
    }

    @Test
    public void VARLONTIL() {
        tilsagnDeltaker.setTiltakNavn("Varig lønnstilskudd");
        tilsagnDeltaker.setTiltakKode("VARLONTIL");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("VARLONTIL", tub);
    }

    @Test
    public void VASV() {
        tilsagnGruppe.setTiltakNavn("Varig tilrettelagt arbeid");
        tilsagnGruppe.setTiltakKode("VASV");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf("VASV", tub);
    }

    @Test
    public void VATIAROR() {
        tilsagnDeltaker.setTiltakNavn("Varig tilrettelagt arbeid i ordinær virksomhet");
        tilsagnDeltaker.setTiltakKode("VATIAROR");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf("VATIAROR", tub);
    }

    private void opprettPdf(String tiltakskode, TilsagnUnderBehandling tub) {
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tub);
        System.out.println(pdfJson);
        pdfGenService.tilsagnsbrevTilPdfBytes(tub, pdfJson);

        PDDocument pdf = null;
        try {
            pdf = PDDocument.load(new ByteArrayInputStream(tub.getPdf()));
            pdf.save("src/test/resources/PDF/" + tiltakskode + ".pdf");
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

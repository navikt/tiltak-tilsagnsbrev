package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapper.TiltakType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer.topic;
import static no.nav.tag.tilsagnsbrev.mapper.TiltakType.*;

@SpringBootTest
@ActiveProfiles("local")
@EmbeddedKafka(partitions = 1, topics = topic)

/*
    Sjekk ut og kjør dockerimg til tag-dogGen. Kjør testene mot http://localhost:5913 i stedet for mockserver.
    Eller port-forwad til pod i dev-fss på 8080
 */
@Disabled("For manuell sjekk av pdf dokument")
public class PdfgenServiceIntTest {

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private PdfGenService pdfGenService;

    private Tilsagn tilsagnDeltaker = Testdata.tilsagnEnDeltaker();
    private Tilsagn tilsagnGruppe = Testdata.gruppeTilsagn();

    @Test
    public void ARBFORB() {
        tilsagnGruppe.setTiltakKode(ARBFORB.getTiltakskode());
        tilsagnGruppe.setTiltakNavn("Arbeidsforberedende trening");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(ARBFORB, tub);
    }

    @Test
    public void EKSPEBIST() {
        tilsagnDeltaker.setTiltakNavn("Ekspertbistand");
        tilsagnDeltaker.setTiltakKode(EKSPEBIST.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(EKSPEBIST, tub);
    }

    @Test
    public void FUNKSJASS() {
        tilsagnDeltaker.setTiltakNavn("funksjonsassistanse");
        tilsagnDeltaker.setTiltakKode(FUNKSJASS.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(FUNKSJASS, tub);
    }

    @Test
    public void INKLUTILS() {
        tilsagnDeltaker.setTiltakNavn("Inkluderingstilskudd");
        tilsagnDeltaker.setTiltakKode(INKLUTILS.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(INKLUTILS, tub);
    }

    @Test
    public void MENTOR() {
        tilsagnDeltaker.setTiltakNavn("Mentor");
        tilsagnDeltaker.setTiltakKode(MENTOR.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(MENTOR, tub);
    }

    @Test
    public void MIDLONTIL() {
        tilsagnDeltaker.setTiltakNavn("Midlertidig lønnstilskudd");
        tilsagnDeltaker.setTiltakKode(MIDLONTIL.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(MIDLONTIL, tub);
    }

    @Test
    public void ENKELAMO() {
        tilsagnDeltaker.setTiltakNavn("Opplæring AMO");
        tilsagnDeltaker.setTiltakKode(ENKELAMO.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(ENKELAMO, tub);
    }

    @Test
    public void ENKFAGYRKE() {
        tilsagnDeltaker.setTiltakNavn("Enkeltplass fag og yrkesopplæring");
        tilsagnDeltaker.setTiltakKode(ENKFAGYRKE.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(ENKFAGYRKE, tub);
    }

    @Test
    public void GRUFAGYRKE() {
        tilsagnGruppe.setTiltakNavn("Gruppe fag og yrkesopplæring");
        tilsagnGruppe.setTiltakKode(GRUFAGYRKE.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(GRUFAGYRKE, tub);
    }

    @Test
    public void HOYEREUTD() {
        tilsagnDeltaker.setTiltakNavn("Høyere utdanning");
        tilsagnDeltaker.setTiltakKode(HOYEREUTD.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(HOYEREUTD, tub);
    }

    @Test
    public void VARLONTIL() {
        tilsagnDeltaker.setTiltakNavn("Varig lønnstilskudd");
        tilsagnDeltaker.setTiltakKode(VARLONTIL.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(VARLONTIL, tub);
    }

    @Test
    public void VASV() {
        tilsagnGruppe.setTiltakNavn("Varig tilrettelagt arbeid");
        tilsagnGruppe.setTiltakKode(VASV.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(VASV, tub);
    }

    @Test
    public void VATIAROR() {
        tilsagnDeltaker.setTiltakNavn("Varig tilrettelagt arbeid i ordinær virksomhet");
        tilsagnDeltaker.setTiltakKode(VATIAROR.getTiltakskode());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(VATIAROR, tub);
    }

    private void opprettPdf(TiltakType tiltakType, TilsagnUnderBehandling tub) {
        String pdfJson = tilsagnJsonMapper.opprettPdfJson(tub);
        System.out.println(pdfJson);
        pdfGenService.tilsagnsbrevTilPdfBytes(tub, pdfJson);

        PDDocument pdf = null;
        try {
            pdf = PDDocument.load(new ByteArrayInputStream(tub.getPdf()));
            pdf.save("src/test/resources/PDF/" + tiltakType.getTiltakskode() + ".pdf");
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
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
    private PdfGenService pdfGenService;

    private Tilsagn tilsagnDeltaker = Testdata.tilsagnEnDeltaker();
    private Tilsagn tilsagnGruppe = Testdata.gruppeTilsagn();

    @Test
    public void ARBFORB() {
        tilsagnGruppe.setTiltakNavn("Arbeidsforberedende trening");
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(ARBFORB, tub);
    }

    @Test
    public void EKSPEBIST() {
        tilsagnDeltaker.setTiltakNavn("Ekspertbistand");
        tilsagnDeltaker.setTiltakKode(EKSPEBIST.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(EKSPEBIST, tub);
    }

    @Test
    public void FUNKSJASS() {
        tilsagnDeltaker.setTiltakNavn("funksjonsassistanse");
        tilsagnDeltaker.setTiltakKode(FUNKSJASS.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(FUNKSJASS, tub);
    }

    @Test
    public void INKLUTILS() {
        tilsagnDeltaker.setTiltakNavn("Inkluderingstilskudd");
        tilsagnDeltaker.setTiltakKode(INKLUTILS.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(INKLUTILS, tub);
    }

    @Test
    public void MENTOR() {
        tilsagnDeltaker.setTiltakNavn("Mentor");
        tilsagnDeltaker.setTiltakKode(MENTOR.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(MENTOR, tub);
    }

    @Test
    public void MIDLONTIL() {
        tilsagnDeltaker.setTiltakNavn("Midlertidig lønnstilskudd");
        tilsagnDeltaker.setTiltakKode(MIDLONTIL.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(MIDLONTIL, tub);
    }

    @Test
    public void ENKELAMO() {
        tilsagnDeltaker.setTiltakNavn("Opplæring AMO");
        tilsagnDeltaker.setTiltakKode(ENKELAMO.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(ENKELAMO, tub);
    }

    @Test
    public void ENKFAGYRKE() {
        tilsagnDeltaker.setTiltakNavn("Enkeltplass fag og yrkesopplæring");
        tilsagnDeltaker.setTiltakKode(ENKFAGYRKE.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(ENKFAGYRKE, tub);
    }

    @Test
    public void GRUFAGYRKE() {
        tilsagnGruppe.setTiltakNavn("Gruppe fag og yrkesopplæring");
        tilsagnGruppe.setTiltakKode(GRUFAGYRKE.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(GRUFAGYRKE, tub);
    }

    @Test
    public void HOYEREUTD() {
        tilsagnDeltaker.setTiltakNavn("Høyere utdanning");
        tilsagnDeltaker.setTiltakKode(HOYEREUTD.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(HOYEREUTD, tub);
    }

    @Test
    public void VARLONTIL() {
        tilsagnDeltaker.setTiltakNavn("Varig lønnstilskudd");
        tilsagnDeltaker.setTiltakKode(VARLONTIL.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(VARLONTIL, tub);
    }

    @Test
    public void VASV() {
        tilsagnGruppe.setTiltakNavn("Varig tilrettelagt arbeid");
        tilsagnGruppe.setTiltakKode(VASV.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(VASV, tub);
    }

    @Test
    public void VATIAROR() {
        tilsagnDeltaker.setTiltakNavn("Varig tilrettelagt arbeid i ordinær virksomhet");
        tilsagnDeltaker.setTiltakKode(VATIAROR.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(VATIAROR, tub);
    }

    @Test
    public void FORSAMOENK() {
        tilsagnDeltaker.setTiltakNavn("Forsøk AMO enkeltplass");
        tilsagnDeltaker.setTiltakKode(FORSAMOENK.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(FORSAMOENK, tub);
    }
    @Test
    public void FORSFAGGRU() {
        tilsagnGruppe.setTiltakNavn("Forsøk fag- og yrkesopplæring gruppe");
        tilsagnGruppe.setTiltakKode(FORSFAGGRU.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnGruppe).build();
        opprettPdf(FORSFAGGRU, tub);
    }
    @Test
    public void FORSFAGENK() {
        tilsagnDeltaker.setTiltakNavn("Forsøk fag- og yrkesopplæring enkeltplass");
        tilsagnDeltaker.setTiltakKode(FORSFAGENK.name());
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagnDeltaker).build();
        opprettPdf(FORSFAGENK, tub);
    }


    private void opprettPdf(TiltakType tiltakType, TilsagnUnderBehandling tub) {
        byte[] pdfBytes = pdfGenService.tilsagnsbrevTilPdfBytes(tub.getTilsagn());

        PDDocument pdf = null;
        try {
            pdf = PDDocument.load(new ByteArrayInputStream(pdfBytes));
            pdf.save("src/test/resources/PDF/" + tiltakType.name() + ".pdf");
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

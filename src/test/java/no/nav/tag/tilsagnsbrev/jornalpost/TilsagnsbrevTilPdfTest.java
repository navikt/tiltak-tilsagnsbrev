package no.nav.tag.tilsagnsbrev.jornalpost;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.domene.Tilsagnsbrev;
import no.nav.tag.tilsagnsbrev.mapping.pdfgen.TilsagnsbrevTilPdf;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class TilsagnsbrevTilPdfTest {

     private final Tilsagnsbrev tilsagnsbrev = Testdata.tilsagnsbrev();

    @Test
    public void lagerPdf() throws Exception {

        TilsagnsbrevTilPdf tilsagnsbrevTilPdf = new TilsagnsbrevTilPdf();
        byte[] bytes = tilsagnsbrevTilPdf.lagPdf(tilsagnsbrev);

        PDDocument dokument = PDDocument.load(bytes);
        String textInPdf = new PDFTextStripper().getText(dokument);

//        assertTrue(textInPdf.contains(tilsagnsbrev.getNavFaks()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getOrgNummer()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getBedriftsnavn()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getBedriftsNummer()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getDeltakerFnr()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getKontoNummer()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getDeltakerNavn()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getNavBesoksadresse()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getNavKontor()));
        assertTrue(textInPdf.contains(tilsagnsbrev.getNavnSaksbehandler()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getNavPostadresse()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getNavReferanse()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getNavTlf()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getTilskudd()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getTilskuddLonn()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getTilskuddTotalt()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getOprettetDato()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getFraDato()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getTilDato()));
//        assertTrue(textInPdf.contains(tilsagnsbrev.getFristRefusjonskrav()));
    }

    @Ignore("Til manuell test")
    @Test
    public void lagerPdfFil() throws Exception {

        TilsagnsbrevTilPdf tilsagnsbrevTilPdf = new TilsagnsbrevTilPdf();
        byte[] bytes = tilsagnsbrevTilPdf.lagPdf(tilsagnsbrev);

        PDDocument pdf = PDDocument.load(bytes);
        pdf.save("src/test/resources/Resultat.pdf");
        pdf.close();
    }


    private String filTilString() {
        byte[] bytes = new byte[0];
        try {
            Path fil = Paths.get(getClass().getClassLoader()
                    .getResource("kontrollforespoersel.json").toURI());
            bytes = Files.readAllBytes(fil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes);
//        return Base64.getEncoder().encodeToString(bytes);
    }
}

package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PdfTemplateURITest {

    PdfGenKonfig pdfGenKonfig = new PdfGenKonfig("http://localhost");
    PdfTemplateURI pdfTemplateURI = new PdfTemplateURI(pdfGenKonfig);

    @Test
    public void urlForArbeidsfeorberedende() {
        URI url = pdfTemplateURI.getTemplateURI("ARBFORB");
        assertEquals("http://localhost/template/tiltak-tilsagnsbrev-arbeidsforberedende-trening/create-pdf", url.toString());
    }

    @Test
    public void urlForEkspertbistand() {
        URI url = pdfTemplateURI.getTemplateURI("EKSPEBIST");
        assertTrue(url.getPath().contains("ekspertbistand"));
    }

    @Test
    public void urlForFunksjonsassistanse() {
        URI url = pdfTemplateURI.getTemplateURI("FUNKSJASS");
        assertTrue(url.getPath().contains("-funksjonsassistanse"));
    }

    @Test
    public void urlForInkludering() {
        URI url = pdfTemplateURI.getTemplateURI("INKLUTILS");
        assertTrue(url.getPath().contains("inkluderingstilskudd"));
    }

    @Test
    public void urlForMentor() {
        URI url = pdfTemplateURI.getTemplateURI("MENTOR");
        assertTrue(url.getPath().contains("-mentortilskudd"));
    }

    @Test
    public void urlForMidlertidigLønn() {
        URI url = pdfTemplateURI.getTemplateURI("MIDLONTIL");
        assertTrue(url.getPath().contains("-midlertidig-lonnstilskudd"));
    }

    @Test
    public void urlForOpplæringAmo() {
        URI url = pdfTemplateURI.getTemplateURI("ENKELAMO");
        assertTrue(url.getPath().contains("-opplaering-amo"));
    }

    @Test
    public void urlForOpplaeringFagyrkeDeltaker() {
        URI url = pdfTemplateURI.getTemplateURI("ENKFAGYRKE");
        assertTrue(url.getPath().contains("-opplaering-fagyrke-deltaker"));
    }

    @Test
    public void urlForOpplaeringFagyrkeGruppe() {
        URI url = pdfTemplateURI.getTemplateURI("GRUFAGYRKE");
        assertTrue(url.getPath().contains("-opplaering-fagyrke-grp"));
    }

    @Test
    public void urlForOpplaeringHoyereUtdanning() {
        URI url = pdfTemplateURI.getTemplateURI("HOYEREUTD");
        assertTrue(url.getPath().contains("-opplaering-hoyere-utd"));
    }

    @Test
    public void urlForVarigLønn() {
        URI url = pdfTemplateURI.getTemplateURI("VARLONTIL");
        assertTrue(url.getPath().contains("-varig-lonnstilskudd"));
    }

    @Test
    public void urlForTilrettelagtArb() {
        URI url = pdfTemplateURI.getTemplateURI("VASV");
        assertTrue(url.getPath().contains("-varig-tilrettelagt-arbeid"));
    }

    @Test
    public void urlForTilrettelagtOrdinær() {
        URI url = pdfTemplateURI.getTemplateURI("VATIAROR");
        assertTrue(url.getPath().contains("-varig-tilrettelagt-ordinar"));
    }

    @Test
    public void kasterDataException() {
        assertThrows(DataException.class, () -> pdfTemplateURI.getTemplateURI("UKJENT"));
    }
}

package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PdfTemplateURITest {

    PdfGenKonfig pdfGenKonfig = new PdfGenKonfig("http://localhost");
    PdfTemplateURI pdfTemplateURI = new PdfTemplateURI(pdfGenKonfig);

    @Test
    public void urlForArbeidsfeorberedende(){
        URI url = pdfTemplateURI.getTemplateURI("UKJENT_4");
        assertEquals("http://localhost/template/tiltak-tilsagnsbrev-arbeidsforberedende-trening/create-pdf", url.toString());
    }

    @Test
    public void urlForEkspertbistand(){
        URI url = pdfTemplateURI.getTemplateURI("EKSPEBIST");
        assertTrue(url.getPath().contains("ekspertbistand"));
    }

    @Test
    public void urlForFunksjonsassistanse(){
        URI url = pdfTemplateURI.getTemplateURI("FUNKSJASS");
        assertTrue(url.getPath().contains("-funksjonsassistanse"));
    }

    @Test
    public void urlForInkludering(){
        URI url = pdfTemplateURI.getTemplateURI("INKLUTILS");
        assertTrue(url.getPath().contains("inkluderingstilskudd"));
    }

    @Test
    public void urlForMentor(){
        URI url = pdfTemplateURI.getTemplateURI("MENTOR");
        assertTrue(url.getPath().contains("-mentortilskudd"));
    }

    @Test
    public void urlForMidlertidigLønn(){
        URI url = pdfTemplateURI.getTemplateURI("MIDLONTIL");
        assertTrue(url.getPath().contains("-midlertidig-lonnstilskudd"));
    }

    @Test
    public void urlForOpplæringAmo(){
        URI url = pdfTemplateURI.getTemplateURI("AMOE");
        assertTrue(url.getPath().contains("-opplaering-amo"));
    }

    @Test
    public void urlForOpplaeringFagyrkeDeltaker(){
        URI url = pdfTemplateURI.getTemplateURI("UKJENT_1");
        assertTrue(url.getPath().contains("-opplaering-fagyrke-deltaker"));
    }

    @Test
    public void urlForOpplaeringFagyrkeGruppe(){
        URI url = pdfTemplateURI.getTemplateURI("UKJENT_2");
        assertTrue(url.getPath().contains("-opplaering-fagyrke-gruppe"));
    }

    @Test
    public void urlForOpplaeringHoyereUtdanning(){
        URI url = pdfTemplateURI.getTemplateURI("UKJENT_3");
        assertTrue(url.getPath().contains("-opplaering-hoyere-utd"));
    }

    @Test
    public void urlForVarigLønn(){
        URI url = pdfTemplateURI.getTemplateURI("VARLONTIL");
        assertTrue(url.getPath().contains("-varig-lonnstilskudd"));
    }

    @Test
    public void urlForTilrettelagtArb(){
        URI url = pdfTemplateURI.getTemplateURI("TILRARB");
        assertTrue(url.getPath().contains("-varig-tilrettelagt-arbeid"));
    }

    @Test
    public void urlForTilrettelagtOrdinær(){
        URI url = pdfTemplateURI.getTemplateURI("VATIAROR");
        assertTrue(url.getPath().contains("-varig-tilrettelagt-ordinar"));
    }

    @Test(expected = DataException.class)
    public void kasterDataException(){
        pdfTemplateURI.getTemplateURI("UKJENT");
    }
}

package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class TilsagnTilAltinnXmlTest {

    TilsagnTilAltinnXml tilsagnTilAltinnXml = new TilsagnTilAltinnXml();

    @Test
    @Ignore("Ikke klar")
    public void mapperTilAltinnMelding() {
        Tilsagn tilsagn = Testdata.tilsagnsbrev();

        byte[] pdf = "pdf".getBytes();


        String xml = tilsagnTilAltinnXml.tilAltinnMelding(tilsagn, pdf);
        System.out.println(xml);
        validateXml(xml);
    }


    private void validateXml(String xml) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);

        Schema schema = null;
        try {
            schema = schemaFactory.newSchema(new File(Testdata.hentFilString("xsd/StandardBusinessDocumentHeader.xsd")));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

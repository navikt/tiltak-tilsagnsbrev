package no.nav.tag.tilsagnsbrev.mapping;

import no.altinn.services.serviceengine.correspondence._2009._10.InsertCorrespondenceBasicV2;
import no.altinn.services.serviceengine.reporteeelementlist._2010._10.BinaryAttachmentV2;
import no.nav.tag.tilsagnsbrev.konfigurasjon.altinn.AltinnProperties;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import no.nav.tag.tilsagnsbrev.simulator.EncodedString;
import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@Ignore("Sjekk nytteverdi")
@RunWith(MockitoJUnitRunner.class)
public class TilsagnTilAltinnMapperTest {

    @Mock
    private AltinnProperties altinnProperties;

    @InjectMocks
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper = new TilsagnTilAltinnMapper();

    @Test
    public void mapperTilAltinnMelding() {
        when(altinnProperties.getSystemBruker()).thenReturn("bruker");
        when(altinnProperties.getSystemPassord()).thenReturn("passord");

        Tilsagn tilsagn = Testdata.gruppeTilsagn();
        final byte[] bytes = "pdf".getBytes();

        InsertCorrespondenceBasicV2 correspondenceBasicV2 = tilsagnTilAltinnMapper.tilAltinnMelding(tilsagn, bytes);
        assertNotNull(correspondenceBasicV2.getCorrespondence().getVisibleDateTime());

        BinaryAttachmentV2 binaryAttachmentV2 = correspondenceBasicV2.getCorrespondence().getContent().getAttachments().getBinaryAttachments().getBinaryAttachmentV2().get(0);
        binaryAttachmentV2.getData();
        assertEquals(EncodedString.ENC_STR, new String(bytes));
    }

}

package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnAttachmentInitRequest;
import no.nav.tag.tilsagnsbrev.dto.altinn.AltinnCorrespondenceRequest;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.konfigurasjon.altinn.AltinnProperties;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnTilAltinnMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TilsagnTilAltinnMapperTest {

    @Mock
    private AltinnProperties altinnProperties;

    @InjectMocks
    private TilsagnTilAltinnMapper tilsagnTilAltinnMapper;

    @Test
    public void mapperTilAltinnKorrespondanse() {
        Tilsagn tilsagn = Testdata.gruppeTilsagn();

        AltinnCorrespondenceRequest request = tilsagnTilAltinnMapper.tilAltinnKorrespondanse(tilsagn);

        assertNotNull(request);
        assertNotNull(request.getCorrespondence());
        assertEquals(TilsagnTilAltinnMapper.RESOURCE_ID, request.getCorrespondence().getResourceId());
        assertEquals("NAV", request.getCorrespondence().getMessageSender());
        assertNotNull(request.getCorrespondence().getContent());
        assertNotNull(request.getCorrespondence().getContent().getMessageTitle());
        assertNotNull(request.getCorrespondence().getNotification());
        assertEquals(1, request.getRecipients().size());
        assertTrue(request.getRecipients().get(0).startsWith("urn:altinn:organization:identifier-no:"));
    }

    @Test
    public void mapperTilAltinnVedlegg() {
        Tilsagn tilsagn = Testdata.gruppeTilsagn();

        AltinnAttachmentInitRequest attachment = tilsagnTilAltinnMapper.tilAltinnVedlegg(tilsagn);

        assertNotNull(attachment);
        assertEquals(TilsagnTilAltinnMapper.RESOURCE_ID, attachment.getResourceId());
        assertFalse(attachment.isEncrypted());
        assertTrue(attachment.getFileName().endsWith(".pdf"));
        assertNotNull(attachment.getSendersReference());
    }
}

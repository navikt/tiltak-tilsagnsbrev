package no.nav.tag.tilsagnsbrev.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggRepository;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.Testdata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TilsagnJsonMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TilsagnLoggRepository tilsagnLoggRepository;

    @InjectMocks
    TilsagnJsonMapper tilsagnJsonMapper;

    final Tilsagn tilsagn = Testdata.gruppeTilsagn();

    @Test
    public void pakkerUtArenaMelding() throws JsonProcessingException {
        ArenaMelding arenaMelding = Testdata.arenaMelding();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(arenaMelding).build();

        tilsagnJsonMapper.pakkUtArenaMelding(tub);
        System.out.println(tub.getJson());

        final String forventetJson = Testdata.hentFilString("TILSAGN_DATA.json");

        assertEquals("Tilsagnsbrev_id", 111, tub.getTilsagnsbrevId().intValue());
        assertNotNull(tub.getJson());
        assertFalse(tub.getJson().contains("}\""));
        assertFalse(tub.getJson().contains("TILSAGN_ID"));
        assertEquals(forventetJson, tub.getJson());
    }

    @Test
    public void arenaMeldingTilTilsagnGirDatafeilMedInnhold() throws JsonProcessingException {
        ArenaMelding arenaMelding = Testdata.arenaMeldingMedFeil();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().arenaMelding(arenaMelding).build();
        when(objectMapper.readTree(anyString())).thenThrow(JsonProcessingException.class);

        try {
            tilsagnJsonMapper.pakkUtArenaMelding(tub);
            tilsagnJsonMapper.opprettTilsagn(tub);
            fail("Kaster ikke DataException");
        } catch (DataException de) {
            assertNotNull(tub.getJson());
        }
    }

    @Test(expected = DataException.class)
    public void tilsagnTilPdfJsonGirDatafeilMedInnhold() {
        tilsagnJsonMapper = new TilsagnJsonMapper();
        TilsagnUnderBehandling tub = TilsagnUnderBehandling.builder().tilsagn(tilsagn).build();
        tilsagnJsonMapper.opprettPdfJson(tub);
    }
}

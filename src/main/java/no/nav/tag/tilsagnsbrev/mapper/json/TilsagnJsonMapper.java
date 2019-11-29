package no.nav.tag.tilsagnsbrev.mapper.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggRepository;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

import static no.nav.tag.tilsagnsbrev.mapper.json.GsonWrapper.DATE_TIME_FORMATTER;

@Slf4j
@Component
@RequiredArgsConstructor
public class TilsagnJsonMapper {

    @Autowired
    private TilsagnLoggRepository tilsagnLoggRepository;

    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";
    private static final String JSON_ELEM_TILSAGNSBREV_ID = "TILSAGNSBREV_ID";
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).setDateFormat(new SimpleDateFormat("dd MMMM yyyy"));

    private static final String OLD_PATTERN_3 = "}\"";
    private static final String NEW_PATTERN_3 = "}";

    private final GsonWrapper gsonWrapper;

    public void arenaMeldingTilTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            hentTilsagnFraMelding(tilsagnUnderBehandling);
        } catch (Exception e) {
            log.error("Feil v/mapping fra goldengate-melding til Tilsagn-dto, ", e);
            throw new DataException(e.getMessage());
        }
    }

    public void tilsagnTilJson(TilsagnUnderBehandling tilsagnUnderBehandling) {
        try {
            String json = gsonWrapper.opprettPdfJson(tilsagnUnderBehandling.getTilsagn());
            tilsagnUnderBehandling.setJson(json);
            tilsagnUnderBehandling.setMappetFraArena(true);
        } catch (Exception e) {
            log.error("Feil v/mapping fra Tilsagn-dto til pdf-tilsagn ", e);
            throw new DataException(e.getMessage());
        }
    }

    public Tilsagn tilsagnJsonTilTilsagn(String tilsagnJson) {
        return new Gson().fromJson(tilsagnJson, Tilsagn.class);
    }

    private void hentTilsagnFraMelding(TilsagnUnderBehandling tilsagnUnderBehandling) throws JsonProcessingException {
        ObjectNode after = tilsagnUnderBehandling.getArenaMelding().getAfter();
        tilsagnUnderBehandling.setTilsagnsbrevId(after.get(JSON_ELEM_TILSAGNSBREV_ID).intValue());
        log.info("Behandler melding med tilsagnsbrev-id {}", tilsagnUnderBehandling.getTilsagnsbrevId());

        lagreTilsagnsbrevId(tilsagnUnderBehandling);
        if(tilsagnUnderBehandling.isDuplikat()){
            return;
        }

        String jsonStr = meldingtilJsonString(after.get(JSON_ELEM_TILSAGN).asText());
        tilsagnUnderBehandling.setJson(jsonStr);
        JsonNode tilsagnElem = mapper.readTree(jsonStr);
        Tilsagn tilsagn = mapper.treeToValue(tilsagnElem, Tilsagn.class);
        tilsagnUnderBehandling.setTilsagn(tilsagn);
        tilsagnUnderBehandling.setMappetFraArena(true);
    }

    private void lagreTilsagnsbrevId(TilsagnUnderBehandling tilsagnUnderBehandling){
        if (!tilsagnLoggRepository.lagretIdHvisNyMelding(tilsagnUnderBehandling)) {
            tilsagnUnderBehandling.setDuplikat(true);
        }
    }

    private String meldingtilJsonString(String melding) { //TODO Sjekk ut Spring-boot Cusomize mapper
        return StringUtils.replace(melding, OLD_PATTERN_3, NEW_PATTERN_3);
    }
}
package no.nav.tag.tilsagnsbrev.mapper.json;

import com.google.gson.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.*;
import static no.nav.tag.tilsagnsbrev.mapper.json.GsonWrapper.DATE_TIME_FORMATTER;

@Slf4j
@Component
@AllArgsConstructor
public class TilsagnJsonMapper {

    private static final String JSON_ELEM_AFTER = "after";
    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";
    private static final String JSON_ELEM_TILSAGNSBREV_ID = "TILSAGNSBREV_ID";

    private static final String OLD_PATTERN_1 = "\\";
    private static final String NEW_PATTERN_1 = "";
    private static final String OLD_PATTERN_2 = "\"{";
    private static final String NEW_PATTERN_2 = "{";
    private static final String OLD_PATTERN_3 = "}\"";
    private static final String NEW_PATTERN_3 = "}";

    private final GsonWrapper gsonWrapper;

    public void arenaMeldingTilTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling){
        try {
            hentTilsagnFraMelding(tilsagnUnderBehandling);
        } catch (Exception e){
            log.error("Feil v/mapping fra goldengate-melding til Tilsagn-dto, ", e);
            throw new DataException(e.getMessage());
        }
    }

    public void tilsagnTilJson(TilsagnUnderBehandling tilsagnUnderBehandling){
        try {
            String json = gsonWrapper.opprettPdfJson(tilsagnUnderBehandling.getTilsagn());
            tilsagnUnderBehandling.setJson(json);
            tilsagnUnderBehandling.setMappetFraArena(true);
        } catch (Exception e){
            log.error("Feil v/mapping fra Tilsagn-dto til pdf-tilsagn ", e);
            throw new DataException(e.getMessage());
        }
    }

    public Tilsagn tilsagnJsonTilTilsagn(String tilsagnJson){
        return new Gson().fromJson(tilsagnJson, Tilsagn.class);
    }

    private void hentTilsagnFraMelding(TilsagnUnderBehandling tilsagnUnderBehandling) {
        String goldengateJson = meldingtilJsonString(tilsagnUnderBehandling.getJson());
        JsonObject jsonObject = JsonParser.parseString(goldengateJson).getAsJsonObject();
        JsonObject tilsagnJsonObj = jsonObject.getAsJsonObject(JSON_ELEM_AFTER);
        Integer tilsagnsbrevId = Integer.valueOf(tilsagnJsonObj.get(JSON_ELEM_TILSAGNSBREV_ID).getAsInt()); //TODO Bruke denne til sjekk av duplikater
        tilsagnUnderBehandling.setTilsagnsbrevId(tilsagnsbrevId);
        JsonObject tilsagnJsonElement = tilsagnJsonObj.getAsJsonObject(JSON_ELEM_TILSAGN);
        tilsagnUnderBehandling.setJson(tilsagnJsonElement.getAsString());
        Tilsagn tilsagn = gsonWrapper.opprettTilsagn(tilsagnJsonElement);
        tilsagnUnderBehandling.setTilsagn(tilsagn);
    }

    private String meldingtilJsonString(String melding) {
        String str1 = StringUtils.replace(melding, OLD_PATTERN_1, NEW_PATTERN_1);
        String str2 = StringUtils.replace(str1, OLD_PATTERN_2, NEW_PATTERN_2);
        return StringUtils.replace(str2, OLD_PATTERN_3, NEW_PATTERN_3);
    }
}
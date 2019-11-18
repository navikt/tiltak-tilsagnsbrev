package no.nav.tag.tilsagnsbrev.mapper.json;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.*;

@Slf4j
@Component
public class TilsagnJsonMapper {

    private static final String JSON_ELEM_AFTER = "after";
    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";

    private static final String OLD_PATTERN_1 = "\\";
    private static final String NEW_PATTERN_1 = "";
    private static final String OLD_PATTERN_2 = "\"{";
    private static final String NEW_PATTERN_2 = "{";
    private static final String OLD_PATTERN_3 = "}\"";
    private static final String NEW_PATTERN_3 = "}";

    @Autowired
    private GsonWrapper gsonWrapper;

    public void arenaMeldingTilTilsagn(TilsagnUnderBehandling tilsagnUnderBehandling){
        try {
            Tilsagn tilsagn = hentTilsagnFraMelding(tilsagnUnderBehandling.getJson());
            tilsagnUnderBehandling.setTilsagn(tilsagn);
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

    private Tilsagn hentTilsagnFraMelding(String goldengateJson) {
        goldengateJson = meldingtilJsonString(goldengateJson);
        JsonObject jsonObject = JsonParser.parseString(goldengateJson).getAsJsonObject();
        JsonElement tilsagnJsonElement = jsonObject.getAsJsonObject(JSON_ELEM_AFTER).getAsJsonObject(JSON_ELEM_TILSAGN);
        return gsonWrapper.opprettTilsagn(tilsagnJsonElement);
    }

    private String meldingtilJsonString(String melding) {
        String str1 = StringUtils.replace(melding, OLD_PATTERN_1, NEW_PATTERN_1);
        String str2 = StringUtils.replace(str1, OLD_PATTERN_2, NEW_PATTERN_2);
        return StringUtils.replace(str2, OLD_PATTERN_3, NEW_PATTERN_3);
    }



}
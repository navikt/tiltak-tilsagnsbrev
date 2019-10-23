package no.nav.tag.tilsagnsbrev.mapping;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Slf4j
@Component
public class TilsagnJsonMapper {

    private static final String JSON_ELEM_AFTER = "after";
    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";
    private static final String JSON_DATOFORMAT = "dd MMM YYYY";

    private static final String OLD_PATTERN_1 = "\\";
    private static final String NEW_PATTERN_1 = "";
    private static final String OLD_PATTERN_2 = "\"{";
    private static final String NEW_PATTERN_2 = "{";
    private static final String OLD_PATTERN_3 = "}\"";
    private static final String NEW_PATTERN_3 = "}";


    public String tilsagnTilJson(Tilsagn tilsagn) {
        Gson gson = new GsonBuilder().setDateFormat(JSON_DATOFORMAT).create();
        return gson.toJson(tilsagn);
    }

    public Tilsagn goldengateJsonTilTilsagn(String melding) {

        melding = meldingtilJsonString(melding);
        JsonObject jsonObject = JsonParser.parseString(melding).getAsJsonObject();
        JsonElement tilsagnJsonElement = jsonObject.getAsJsonObject(JSON_ELEM_AFTER).getAsJsonObject(JSON_ELEM_TILSAGN);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
              LocalDate.parse(json.getAsJsonPrimitive().getAsString())).create();
        return gson.fromJson(tilsagnJsonElement, Tilsagn.class);
    }

    private String meldingtilJsonString(String goldengateJson) {
        String str1 = StringUtils.replace(goldengateJson, OLD_PATTERN_1, NEW_PATTERN_1);
        String str2 = StringUtils.replace(str1, OLD_PATTERN_2, NEW_PATTERN_2);
        return StringUtils.replace(str2, OLD_PATTERN_3, NEW_PATTERN_3);
    }

}
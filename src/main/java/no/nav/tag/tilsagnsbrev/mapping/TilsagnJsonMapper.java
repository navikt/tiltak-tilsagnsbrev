package no.nav.tag.tilsagnsbrev.mapping;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class TilsagnJsonMapper {

    private static final String JSON_ELEM_AFTER = "after";
    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";
    private static final String JSON_DATOFORMAT = "dd MMM YYYY";

    public String tilsagnTilJson(Tilsagn tilsagn) {
        Gson gson = new GsonBuilder().setDateFormat(JSON_DATOFORMAT).create();
        return gson.toJson(tilsagn);
    }

    public Tilsagn goldengateJsonTilTilsagn(String goldengateJson) {
        JsonObject jsonObject = JsonParser.parseString(goldengateJson).getAsJsonObject();
        JsonElement tilsagnJsonElement = jsonObject.getAsJsonObject(JSON_ELEM_AFTER).getAsJsonObject(JSON_ELEM_TILSAGN);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
              LocalDate.parse(json.getAsJsonPrimitive().getAsString())).create();

        return gson.fromJson(tilsagnJsonElement, Tilsagn.class);
    }

}
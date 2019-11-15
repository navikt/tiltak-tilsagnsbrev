package no.nav.tag.tilsagnsbrev.mapper;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Component
public class TilsagnJsonMapper {

    private static final String JSON_ELEM_AFTER = "after";
    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";
    private static final String JSON_DATOFORMAT = "dd MMMM yyyy";

    private static final String OLD_PATTERN_1 = "\\";
    private static final String NEW_PATTERN_1 = "";
    private static final String OLD_PATTERN_2 = "\"{";
    private static final String NEW_PATTERN_2 = "{";
    private static final String OLD_PATTERN_3 = "}\"";
    private static final String NEW_PATTERN_3 = "}";

    private static final Locale LOCALE_NO = Locale.forLanguageTag("no");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(JSON_DATOFORMAT, LOCALE_NO);

    public String tilsagnTilPdfJson(Tilsagn tilsagn) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                new JsonPrimitive(date.format(DATE_TIME_FORMATTER))).create();
        return gson.toJson(tilsagn);
    }

    public Tilsagn goldengateMeldingTilTilsagn(String melding) {
        melding = meldingtilJsonString(melding);
        JsonObject jsonObject = JsonParser.parseString(melding).getAsJsonObject();
        JsonElement tilsagnJsonElement = jsonObject.getAsJsonObject(JSON_ELEM_AFTER).getAsJsonObject(JSON_ELEM_TILSAGN);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                LocalDate.parse(json.getAsJsonPrimitive().getAsString())).create();
        return gson.fromJson(tilsagnJsonElement, Tilsagn.class);
    }

    private String meldingtilJsonString(String melding) {
        String str1 = StringUtils.replace(melding, OLD_PATTERN_1, NEW_PATTERN_1);
        String str2 = StringUtils.replace(str1, OLD_PATTERN_2, NEW_PATTERN_2);
        return StringUtils.replace(str2, OLD_PATTERN_3, NEW_PATTERN_3);
    }

}
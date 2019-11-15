package no.nav.tag.tilsagnsbrev.mapper.json;

import com.google.gson.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class GsonWrapper {

    private static final String JSON_DATOFORMAT = "dd MMMM yyyy";
    private static final Locale LOCALE_NO = Locale.forLanguageTag("no");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(JSON_DATOFORMAT, LOCALE_NO);

    private final Gson pdfGson;
    private final Gson tilsagnGson;

    public GsonWrapper() {
        tilsagnGson = tilsagnGson();
        pdfGson = pdfGson();
    }

    public String opprettPdfJson(Tilsagn tilsagn){
        return pdfGson.toJson(tilsagn);
    }

    public Tilsagn opprettTilsagn(JsonElement jsonElem){
        return tilsagnGson.fromJson(jsonElem, Tilsagn.class);
    }

    private static Gson tilsagnGson(){
        return new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                LocalDate.parse(json.getAsJsonPrimitive().getAsString())).create();
    }

    private static Gson pdfGson(){
        return new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                new JsonPrimitive(date.format(DATE_TIME_FORMATTER))).create();
    }
}

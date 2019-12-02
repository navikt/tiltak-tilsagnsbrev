package no.nav.tag.tilsagnsbrev.mapper.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;

import java.io.IOException;

public class PdfJsonSerializer extends JsonSerializer<Tilsagn> {
    @Override
    public void serialize(Tilsagn tilsagn, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writ writeStringField(
                "favoriteColor",
                getColorAsWebColor(user.getFavoriteColor()));
        jsonGenerator.writeEndObject();
    }
}

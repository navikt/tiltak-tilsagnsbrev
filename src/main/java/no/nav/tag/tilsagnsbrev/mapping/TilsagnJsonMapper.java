package no.nav.tag.tilsagnsbrev.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TilsagnJsonMapper {

    static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String JSON_ELEM_AFTER = "after";
    private static final String JSON_ELEM_TILSAGN = "TILSAGN_DATA";

    public Tilsagn jsonTilTilsagn(String json) {
        JSONObject jsonObj = new JSONObject(json).getJSONObject(JSON_ELEM_AFTER).getJSONObject(JSON_ELEM_TILSAGN);
        try {
            return objectMapper.readValue(jsonObj.toString(), Tilsagn.class);
        } catch (IOException e) {
            log.error("Feil v/mapping fra arena-json: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

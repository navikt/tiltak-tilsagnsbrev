package no.nav.tag.tilsagnsbrev.simulator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String lesFil(String filnavn) {
        try {
            Path fil = Paths.get(SimUtil.class.getClassLoader()
                    .getResource(filnavn).toURI());
            return Files.readString(fil);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArenaMelding arenaMelding(int tilsagnId, String json) {
        ObjectNode after = objectMapper.createObjectNode();
        after.put("TILSAGNSBREV_ID", tilsagnId);
        after.put("TILSAGN_DATA", json);
        return ArenaMelding.builder().after(after).build();
    }
}

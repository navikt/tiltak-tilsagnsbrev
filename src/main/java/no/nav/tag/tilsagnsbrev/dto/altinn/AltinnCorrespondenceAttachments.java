package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AltinnCorrespondenceAttachments {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("isEncrypted")
    private boolean isEncrypted;

    @JsonProperty("checksum")
    private String checksum;

    @JsonProperty("sendersReference")
    private String sendersReference;

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("dataLocationType")
    private String dataLocationType;
}

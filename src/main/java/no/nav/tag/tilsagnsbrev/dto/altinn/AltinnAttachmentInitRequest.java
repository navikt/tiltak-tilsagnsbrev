package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AltinnAttachmentInitRequest {

    @JsonProperty("resourceId")
    private String resourceId;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("isEncrypted")
    private boolean isEncrypted;

    @JsonProperty("sendersReference")
    private String sendersReference;
}

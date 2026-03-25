package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AltinnCorrespondenceRequest {

    @JsonProperty("correspondence")
    private AltinnCorrespondenceBase correspondence;

    @JsonProperty("recipients")
    private List<String> recipients;

    @JsonProperty("existingAttachments")
    private List<UUID> existingAttachments;

    @JsonProperty("idempotentKey")
    private String idempotentKey;

}

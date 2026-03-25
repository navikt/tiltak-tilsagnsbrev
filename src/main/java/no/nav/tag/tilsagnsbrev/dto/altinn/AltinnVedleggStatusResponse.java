package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AltinnVedleggStatusResponse {

    @JsonProperty("attachmentId")
    private String attachmentId;

    @JsonProperty("status")
    private String status;
}

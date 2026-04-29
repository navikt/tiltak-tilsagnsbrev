package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AltinnCorrespondenceResponse {

    @JsonProperty("correspondences")
    private List<InitializedCorrespondence> correspondences;

    @JsonProperty("attachmentIds")
    private List<UUID> attachmentIds;

    @Data
    public static class InitializedCorrespondence {

        @JsonProperty("correspondenceId")
        private UUID correspondenceId;

        @JsonProperty("status")
        private String status;

        @JsonProperty("recipient")
        private String recipient;
    }
}

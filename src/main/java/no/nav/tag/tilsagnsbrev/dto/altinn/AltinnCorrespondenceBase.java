package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class AltinnCorrespondenceBase {

    @JsonProperty("resourceId")
    private String resourceId;

    @JsonProperty("sendersReference")
    private String sendersReference;

    @JsonProperty("messageSender")
    private String messageSender;

    @JsonProperty("content")
    private AltinnCorrespondenceContent content;

    @JsonProperty("requestedPublishTime")
    private OffsetDateTime requestedPublishTime;

    @JsonProperty("dueDateTime")
    private OffsetDateTime dueDateTime;

    @JsonProperty("notification")
    private AltinnCorrespondenceNotification notification;
}

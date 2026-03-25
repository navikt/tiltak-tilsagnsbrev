package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty("requestedPublishTime")
    private OffsetDateTime requestedPublishTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty("allowSystemDeleteAfter")
    private OffsetDateTime allowSystemDeleteAfter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    @JsonProperty("dueDateTime")
    private OffsetDateTime dueDateTime;

    @JsonProperty("notification")
    private AltinnCorrespondenceNotification notification;

}

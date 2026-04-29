package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AltinnCorrespondenceNotification {

    @JsonProperty("notificationTemplate")
    private String notificationTemplate;

    @JsonProperty("notificationChannel")
    private String notificationChannel;

    @JsonProperty("emailSubject")
    private String emailSubject;

    @JsonProperty("emailBody")
    private String emailBody;

    @JsonProperty("sendReminder")
    private boolean sendReminder;
}

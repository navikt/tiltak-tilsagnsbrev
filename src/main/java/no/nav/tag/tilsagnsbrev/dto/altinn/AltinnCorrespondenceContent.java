package no.nav.tag.tilsagnsbrev.dto.altinn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AltinnCorrespondenceContent {

    @JsonProperty("language")
    private String language;

    @JsonProperty("messageTitle")
    private String messageTitle;

    @JsonProperty("messageSummary")
    private String messageSummary;

    @JsonProperty("messageBody")
    private String messageBody;
}

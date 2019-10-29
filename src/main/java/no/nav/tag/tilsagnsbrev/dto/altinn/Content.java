package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

@Data
public class Content {
    String LanguageCode;
    String MessageTitle;
    String MessageSummary;
    String MessageBody;
    Attachments Attachments = new Attachments();
    String CustomMessageData;
}

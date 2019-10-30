package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

@Data
public class Content {

    private static final String LANGUAGE_CODE = "1044";
    private static final String MESSAGE_TITLE = "Tilsagnsbrev";
    private static final String MESSAGE_SUMMARY = "Tilsagnsbrevet følger som et vedlegg i PDF-format";

    private final String LanguageCode = LANGUAGE_CODE;
    private final String MessageTitle = MESSAGE_TITLE;
    private final String MessageSummary = MESSAGE_SUMMARY;
    //private String MessageBody;
    private String CustomMessageData;
    private Attachments Attachments = new Attachments();
}

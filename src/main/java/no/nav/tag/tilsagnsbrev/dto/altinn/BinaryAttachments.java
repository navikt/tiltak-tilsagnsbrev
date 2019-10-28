package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BinaryAttachments {
    List< BinaryAttachmentV2 > BinaryAttachmentV2 = new ArrayList<>();
}

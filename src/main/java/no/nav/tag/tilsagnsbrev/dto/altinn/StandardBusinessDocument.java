package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.nav.tag.tilsagnsbrev.dto.altinn.header.StandardBusinessDocumentHeader;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class StandardBusinessDocument {
    StandardBusinessDocumentHeader standardBusinessDocumentHeader;
    InsertCorrespondenceBasicV2 insertCorrespondenceBasicV2Object;
}

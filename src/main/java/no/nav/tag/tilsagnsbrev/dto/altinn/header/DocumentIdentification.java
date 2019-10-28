package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentIdentification {
    private final String Standard = "http://www.altinn.no/services/ServiceEngine/Correspondence/2009/10";
    private final String TypeVersion = "1.0";
    private final String Type = "InsertCorrespondenceV2";
    private String CreationDateAndTime; //FORMAT: 2019-10-18T12:24:35
    private String InstanceIdentifier;
}

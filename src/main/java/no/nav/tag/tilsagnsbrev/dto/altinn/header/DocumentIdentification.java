package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentIdentification {

    private static final String ELEM_STANDARD = "http://www.altinn.no/services/ServiceEngine/Correspondence/2009/10";
    private static final String ELEM_TYPEVERSION = "1.0";
    private static final String ELEM_TYPE = "InsertCorrespondenceBasicV2";

    private final String Standard = ELEM_STANDARD;
    private final String TypeVersion = ELEM_TYPEVERSION;
    private final String Type = ELEM_TYPE;
    private String CreationDateAndTime; //FORMAT: 2019-10-18T12:24:35
    private String InstanceIdentifier;
}

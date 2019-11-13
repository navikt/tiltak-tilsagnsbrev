package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@XmlRootElement(name = "InsertCorrespondenceBasicV2")
public class InsertCorrespondenceBasicV2 {

    private final static String SYSTEMUSER_CODE = "NAV_TILSAGNSBREV";

    private final String SystemUserCode = SYSTEMUSER_CODE;
    private String systemUserName;
    private String systemPassword;
    private String ExternalShipmentReference; //Lik InstanceIdentifier

    private Correspondence CorrespondenceObject;
}



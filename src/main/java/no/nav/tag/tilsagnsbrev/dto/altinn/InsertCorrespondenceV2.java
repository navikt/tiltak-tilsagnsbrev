package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InsertCorrespondenceV2 {

    private final static String SYSTEMUSER_CODE = "NAV-TILSAGNSBREV";

    private final String SystemUserCode = SYSTEMUSER_CODE;
    private String ExternalShipmentReference; //Lik InstanceIdentifier
    private Correspondence CorrespondenceObject;
}



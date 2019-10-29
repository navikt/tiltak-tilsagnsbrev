package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InsertCorrespondenceV2 {
    private String SystemUserCode; //NAV_T12
    private String ExternalShipmentReference; //Lik InstanceIdentifier
    private Correspondence CorrespondenceObject;
}



package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

@Data
public class Correspondence {

    private static final String SERVICE_CODE = "0000"; //TODO finn servicekode
    private static final String SERVICE_EDITION = "1"; //TODO finn service-edition

    private final String ServiceCode = SERVICE_CODE;
    private final String ServiceEdition = SERVICE_EDITION;
    private String Reportee; //lik receier u. kommunenr
    private Content Content;
}

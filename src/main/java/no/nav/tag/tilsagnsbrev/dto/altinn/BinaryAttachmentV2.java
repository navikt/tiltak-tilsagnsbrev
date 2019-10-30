package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

@Data
public class BinaryAttachmentV2 {

    private static final String FUNCTION_TYPE = "Unspecified";
    private static final String ENCRYPTED = "false";

    private final String FunctionType = FUNCTION_TYPE;
    private final String Encrypted = ENCRYPTED;
    private String FileName;
    private String Name;
    private String Data;
    private String SendersReference;

}

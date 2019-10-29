package no.nav.tag.tilsagnsbrev.dto.altinn;

import lombok.Data;

@Data
public class BinaryAttachmentV2 {
    String FunctionTyp;
    String FileName;
    String Name;
    String Encrypted;
    String Data;
    String SendersReference;

}

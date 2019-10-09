package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deltaker {

    private String etternavn;
    private String fornavn;
    private String fodselsnr;
    private String landKode;
    private String postAdresse;
    private String postNummer;
    private String postSted;

}

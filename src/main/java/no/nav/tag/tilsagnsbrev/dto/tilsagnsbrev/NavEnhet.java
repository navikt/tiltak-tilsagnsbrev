package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavEnhet {

    private String faks;
    private String navKontor;
    private String navKontorNavn;
    private String postAdresse;
    private String postNummer;
    private String postSted;
    private String telefon;
}

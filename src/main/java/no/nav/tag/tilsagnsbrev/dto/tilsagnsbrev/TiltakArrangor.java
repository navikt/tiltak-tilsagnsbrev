package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiltakArrangor {

    
    private String arbgiverNavn;
    
    private String kontoNummer;
    
    private String landKode;
    
    private String maalform;
    
    private String orgNummer;
    
    private String orgNummerMorselskap;
    
    private String postAdresse;
    
    private String postNummer;
    
    private String postSted;

}

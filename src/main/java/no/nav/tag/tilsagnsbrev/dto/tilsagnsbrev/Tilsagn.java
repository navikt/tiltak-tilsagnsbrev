package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  Tilsagn {

    private String administrasjonKode;
    
    private String antallDeltakere;
    
    private String antallTimeverk;
    
    private Person beslutter;
    
    private Deltaker deltaker;
    
    private String kommentar;
    
    private NavEnhet navEnhet;
    
    private Periode periode;
    
    private Person saksbehandler;

    private LocalDate tilsagnDato;
    
    private TilsagnNummer tilsagnNummer;
    
    private List<Tilskudd> tilskuddListe;
    
    private TiltakArrangor tiltakArrangor;
    
    private String tiltakKode;
    
    private String tiltakNavn;
    
    private String totaltTilskuddbelop;

    private String valutaKode;
}

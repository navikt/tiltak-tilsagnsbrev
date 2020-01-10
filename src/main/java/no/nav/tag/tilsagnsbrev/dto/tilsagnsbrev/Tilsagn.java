package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tilsagn {

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

    private LocalDate refusjonfristDato;

    private TilsagnNummer tilsagnNummer;

    private List<Tilskudd> tilskuddListe;

    private TiltakArrangor tiltakArrangor;

    private String tiltakKode;

    private String tiltakNavn;

    private String totaltTilskuddbelop;

    private String valutaKode;

    public boolean erGruppeTilsagn() {
        return this.deltaker == null;
    }

    public boolean getPdfVisTilskuddProsent(){
        if (this.tilskuddListe != null) {
            return this.tilskuddListe.stream().findFirst() //Tilskudslisten har aldri mer enn ett element
                    .map(Tilskudd::getVisTilskuddProsent)
                    .orElse(false);
        }
        return false;
    }

    public String getPdfTilskuddsProsent() {
        if (this.tilskuddListe != null) {
            return this.tilskuddListe.stream().findFirst() //Tilskudslisten har aldri mer enn ett element
                    .map(tilskudd -> {
                        if (tilskudd.getVisTilskuddProsent()) {
                            return tilskudd.getTilskuddProsent();
                        }
                        return null;
                    }).orElse(null);
        }
        return null;
    }
}

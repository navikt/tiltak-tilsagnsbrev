package no.nav.tag.tilsagnsbrev;

import com.google.gson.Gson;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;

public class Testdata {

    //Hentet fra fullREquest.json
    public static LocalDate TILSAGNSDATO = LocalDate.parse("2019-09-25");
    public static LocalDate FRA_DATO = LocalDate.parse("2019-09-14");
    public static LocalDate TIL_DATO = LocalDate.parse("2019-12-31");



    public static Tilsagn tilsagnsbrev() {
        return new TilsagnBuilder()
                .medAdministrasjonKode("IND")
                .medAntallDeltakere("1")
                .medAntallTimeverk("110")
                .medBeslutter(new Person("Bjarne", "Beslutter"))
                .medDeltaker(
                        new DeltakerBuilder()
                                .medFornavn("Ola")
                                .medEtternavn("Nordmann")
                                .medFodselsnr("0101011990")
                                .medLandKode("NO")
                                .medPostAdresse("Veien 1")
                                .medPostNummer("2001")
                                .medPostSted("Skjetten")
                                .createDeltaker())
                .medKommentar("Ingen kommentar")
                .medNavEnhet(new NavEnhetBuilder()
                        .medNavKontor("0231")
                        .medNavKontorNavn("NAV Skedsmo")
                        .medPostAdresse("Postboks 294")
                        .medPostNummer("2001")
                        .medPostSted("Lillestrøm")
                        .medTelefon("55553333")
                        .createNavEnhet())
                .medPeriode(new Periode(FRA_DATO , TIL_DATO))
                .medSaksbehandler(new Person("Saksbehandler", "Strine"))
                .medTilsagnDato(TILSAGNSDATO)
                .medTilsagnNummer(new TilsagnNummer("2019", "319383", "1"))
                .medTilskuddListe(Arrays.asList(
                        new Tilskudd("48000", "Drift"),
                        new Tilskudd("1800", "Drift"),
                        new Tilskudd("9500", "Drift")))
                .medTiltakArrangor(new TiltakArrangorBuilder()
                        .medArbgiverNavn("TREIDER KOMPETANSE A")
                        .medKontoNummer("32600596984")
                        .medLandKode("NO")
                        .medMaalform("Nynorsk")
                        .medOrgNummer("920130283")
                        .medOrgNummerMorselskap("920053106")
                        .medPostAdresse("Nedre Vollgate ")
                        .medPostNummer("0158")
                        .medPostSted("Oslo")
                        .createTiltakArrangor())
                .medTiltakKode("ENKFAGYRKE")
                .medTiltakNavn("Enkeltplass Fag- og yrkesopplæring VGS og høyere yrkesfaglig utdanning")
                .medTotaltTilskuddbelop("59300")
                .medValutaKode("NOK")
                .createTilsagn();
    }

    public static String tilsagnTilJSON(Tilsagn tilsagnsbrev) {
        return new Gson().toJson(tilsagnsbrev);
    }

    public static Tilsagn jsonTilTilsagn(String json) throws Exception {
        return new Gson().fromJson(json, Tilsagn.class);
    }

    public static String hentJsonFil() {
        byte[] bytes = new byte[0];
        try {
            Path fil = Paths.get(Testdata.class.getClassLoader()
                    .getResource("fullRequest.json").toURI());
            bytes = Files.readAllBytes(fil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }
}

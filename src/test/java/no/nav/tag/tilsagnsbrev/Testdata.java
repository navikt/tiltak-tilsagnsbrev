package no.nav.tag.tilsagnsbrev;

import com.google.gson.Gson;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;

public class Testdata {

    public static String JSON_FIL = "fullRequest.json";
    public static String JSON_FIL_FEILER = "fullRequest_som_feiler.json";

    //Hentet fra fullREquest.json
    public static LocalDate TILSAGNSDATO = LocalDate.parse("2019-10-22");
    public static LocalDate FRA_DATO = LocalDate.parse("2019-10-01");
    public static LocalDate TIL_DATO = LocalDate.parse("2019-12-31");

    public static Tilsagn tilsagnsbrev() {
        return new TilsagnBuilder()
                .medAdministrasjonKode("INST")
                .medAntallDeltakere("17")
                .medAntallTimeverk("2932")
                .medBeslutter(new Person("Strømmen", "Evy"))
                .medKommentar("Ingen kommentar")
                .medNavEnhet(new NavEnhetBuilder()
                        .medNavKontor("1187")
                        .medNavKontorNavn("NAV Tiltak Rogaland")
                        .medPostAdresse("Postboks 420")
                        .medPostNummer("4002")
                        .medPostSted("Stavanger")
                        .medTelefon("55553333")
                        .medFaks("52048361")
                        .createNavEnhet())
                .medPeriode(new Periode(FRA_DATO , TIL_DATO))
                .medSaksbehandler(new Person("Johannessen", "Odd Helge"))
                .medTilsagnDato(TILSAGNSDATO)
                .medTilsagnNummer(new TilsagnNummer("2019", "366023", "1"))
                .medTilskuddListe(Arrays.asList(
                        new Tilskudd("142000", "Opplæringstilskudd"),
                        new Tilskudd("142000", "Lønnstilskudd")))
                .medTiltakArrangor(new TiltakArrangorBuilder()
                        .medArbgiverNavn("L.S. Solland AS")
                        .medKontoNummer("32010501481")
                        .medLandKode("NO")
                        .medMaalform("Nynorsk")
                        .medOrgNummer("973152289")
                        .medOrgNummerMorselskap("918160922")
                        .medPostAdresse("Pedersgata 110 ")
                        .medPostNummer("4014")
                        .medPostSted("Oslo")
                        .createTiltakArrangor())
                .medTiltakKode("BIO")
                .medTiltakNavn("Bedriftsintern opplæring (BIO)")
                .medTotaltTilskuddbelop("284000")
                .medValutaKode("NOK")
                .createTilsagn();
    }

    public static String tilsagnTilJSON(Tilsagn tilsagnsbrev) {
        return new Gson().toJson(tilsagnsbrev);
    }

    public static Tilsagn jsonTilTilsagn(String json) throws Exception {
        return new Gson().fromJson(json, Tilsagn.class);
    }

    public static String hentJsonFil(String filnavn) {
        byte[] bytes = new byte[0];
        try {
            Path fil = Paths.get(Testdata.class.getClassLoader()
                    .getResource(filnavn).toURI());
            bytes = Files.readAllBytes(fil);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }
}

package no.nav.tag.tilsagnsbrev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Testdata {

    public static String JSON_FIL = "arenaMelding.json";
    public static String JSON_FIL_FEILER = "arenaMelding_som_feiler.json";

    //Hentet fra fullREquest.tilsagn
    public static LocalDate TILSAGNSDATO = LocalDate.parse("2019-10-22");
    public static LocalDate FRA_DATO = LocalDate.parse("2019-10-01");
    public static LocalDate TIL_DATO = LocalDate.parse("2019-12-31");

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String tilsagnDataStr = Testdata.hentFilString("TILSAGN_DATA.json");
    private static String tilsagnDataFeilStr = Testdata.hentFilString("TILSAGN_DATA_feil.json");

    public static Tilsagn gruppeTilsagn() {
        return tilsagnsBuilder().antallDeltakere("17")
                .antallTimeverk("2932")
                .tiltakNavn("Varig tilrettelagt arbeid")
                .tiltakKode("VASV")
                .build();
    }

    public static Tilsagn tilsagnEnDeltaker() {
        return tilsagnsBuilder().deltaker(Deltaker.builder()
                .etternavn("Nilsen")
                .fornavn("Kurt")
                .fodselsnr("05055599999")
                .landKode("NO")
                .postAdresse("Waldemar Thranesgt 89")
                .postNummer("0223")
                .postSted("Oslo")
                .build())
                .tiltakKode("VARLONTIL")
                .tiltakNavn("Varig lønnstilskudd")
                .build();
    }

    private static Tilsagn.TilsagnBuilder tilsagnsBuilder() {
        return Tilsagn.builder()
                .administrasjonKode("INST")
                .beslutter(new Person("Strømmen", "Evy"))
                .kommentar("Ingen kommentar")
                .navEnhet(NavEnhet.builder()
                        .navKontor("1187")
                        .navKontorNavn("NAV Tiltak Rogaland")
                        .postAdresse("Postboks 420")
                        .postNummer("4002")
                        .postSted("Stavanger")
                        .telefon("55553333")
                        .faks("52048361")
                        .build())
                .periode(new Periode(FRA_DATO, TIL_DATO))
                .saksbehandler(new Person("Åsberg", "Kristina"))
                .tilsagnDato(TILSAGNSDATO)
                .tilsagnNummer(new TilsagnNummer("2019", "366023", "1"))
                .tilskuddListe(Arrays.asList(
                        new Tilskudd("28000", "Lønnstilskudd", "60", true)))
                .tiltakArrangor(TiltakArrangor.builder()
                        .arbgiverNavn("BIRTAVARRE OG VÆRLANDET REGNSKAP")
                        .kontoNummer("32010501481")
                        .landKode("NO")
                        .maalform("Nynorsk")
                        .orgNummer("910825607")
                        .orgNummerMorselskap("943506698")
                        .postAdresse("Pedersgata 110 ")
                        .postNummer("4014")
                        .postSted("Oslo")
                        .build())
                .tiltakKode("BIO")
                .tiltakNavn("Bedriftsintern opplæring (BIO)")
                .totaltTilskuddbelop("28000")
                .valutaKode("NOK");
    }

    public static String hentFilString(String filnavn) {
        return new String(hentFilBytes(filnavn));
    }

    public static byte[] hentFilBytes(String filnavn) {
        try {
            Path fil = Paths.get(Testdata.class.getClassLoader()
                    .getResource(filnavn).toURI());
            return Files.readAllBytes(fil);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TilsagnUnderBehandling.TilsagnUnderBehandlingBuilder tubBuilder(){
        return TilsagnUnderBehandling.builder().opprettet(LocalDateTime.now());
    }

    public static ArenaMelding arenaMelding() {
        ObjectNode after = objectMapper.createObjectNode();
        after.put("TILSAGNSBREV_ID", 111);
        after.put("TILSAGN_DATA", tilsagnDataStr);
        return ArenaMelding.builder().after(after).build();
    }

    public static ArenaMelding arenaMeldingMedFeil() {
        ObjectNode after = objectMapper.createObjectNode();
        after.put("TILSAGNSBREV_ID", 111);
        after.put("TILSAGN_DATA", tilsagnDataFeilStr);
        return ArenaMelding.builder().after(after).build();
    }
}

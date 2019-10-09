package no.nav.tag.tilsagnsbrev;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Testdata {

    static ObjectMapper objectMapper = new ObjectMapper(); // .registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY");

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
                .medPeriode(new Periode("2019-09-14", "2019-12-31"))
                .medSaksbehandler(new Person("Saksbehandler", "Strine"))
                .medTilsagnDato("2019-09-25")
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
        try {
            return objectMapper.writeValueAsString(tilsagnsbrev);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tilsagn jsonTilTilsagn(String json) throws Exception {
        return objectMapper.readValue(json, Tilsagn.class);
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

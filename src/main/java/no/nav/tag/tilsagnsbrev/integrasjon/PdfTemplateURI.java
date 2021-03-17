package no.nav.tag.tilsagnsbrev.integrasjon;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class PdfTemplateURI {

    private final static String PREFIX = "template/";
    private final static String CREATE_PDF = "/create-pdf";
    private Map<String, URI> tiltaksKodeURI = new HashMap<String, URI>();

    public PdfTemplateURI(PdfGenKonfig pdfGenKonfig) {
        final URI HOST = URI.create(pdfGenKonfig.getUri());
        initPdfTemplateURIs(HOST);
    }

    private void initPdfTemplateURIs(URI host) {
        final URI arbeidsForberedendeTrening = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-arbeidsforberedende-trening").path(CREATE_PDF).build().toUri();
        final URI ekspertbistand = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-ekspertbistand").path(CREATE_PDF).build().toUri();
        final URI funksjonsassistanse = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-funksjonsassistanse").path(CREATE_PDF).build().toUri();
        final URI inkluderingstilskudd = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-inkluderingstilskudd").path(CREATE_PDF).build().toUri();
        final URI mentortilskudd = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-mentortilskudd").path(CREATE_PDF).build().toUri();
        final URI midlertidigLonnstilskudd = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-midlertidig-lonnstilskudd").path(CREATE_PDF).build().toUri();
        final URI opplaeringAmo = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-opplaering-amo").path(CREATE_PDF).build().toUri();
        final URI opplaeringFagyrkeDeltaker = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-opplaering-fagyrke-deltaker").path(CREATE_PDF).build().toUri();
        final URI opplaeringFagyrkeGruppe = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-opplaering-fagyrke-grp").path(CREATE_PDF).build().toUri();
        final URI opplaeringHoyereUtdanning = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-opplaering-hoyere-utd").path(CREATE_PDF).build().toUri();
        final URI varigLonnstilskudd = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-varig-lonnstilskudd").path(CREATE_PDF).build().toUri();
        final URI varigTilrettelagtArbeid = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-varig-tilrettelagt-arbeid").path(CREATE_PDF).build().toUri();
        final URI varigTilrettelagtOrdinar = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-varig-tilrettelagt-ordinar").path(CREATE_PDF).build().toUri();

        final URI forsokKompetansetiltak = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-kompetansetiltak").path(CREATE_PDF).build().toUri();

        tiltaksKodeURI.put("ARBFORB", arbeidsForberedendeTrening);
        tiltaksKodeURI.put("EKSPEBIST", ekspertbistand);
        tiltaksKodeURI.put("FUNKSJASS", funksjonsassistanse);
        tiltaksKodeURI.put("INKLUTILS", inkluderingstilskudd);
        tiltaksKodeURI.put("MENTOR", mentortilskudd);
        tiltaksKodeURI.put("MIDLONTIL", midlertidigLonnstilskudd);
        tiltaksKodeURI.put("ENKELAMO", opplaeringAmo);
        tiltaksKodeURI.put("ENKFAGYRKE", opplaeringFagyrkeDeltaker);
        tiltaksKodeURI.put("GRUFAGYRKE", opplaeringFagyrkeGruppe);
        tiltaksKodeURI.put("HOYEREUTD", opplaeringHoyereUtdanning);
        tiltaksKodeURI.put("VARLONTIL", varigLonnstilskudd);
        tiltaksKodeURI.put("VASV", varigTilrettelagtArbeid);
        tiltaksKodeURI.put("VATIAROR", varigTilrettelagtOrdinar);

        tiltaksKodeURI.put("FORSAMOENK", forsokKompetansetiltak);
        tiltaksKodeURI.put("FORSFAGGRU", forsokKompetansetiltak);
        tiltaksKodeURI.put("FORSFAGENK", forsokKompetansetiltak);

    }

    URI getTemplateURI(String kode) {
        Optional<URI> optURI = Optional.ofNullable (tiltaksKodeURI.get(kode));
        if(optURI.isEmpty()){
            log.error("Ingen pdf template path oppgitt for tiltakskode " + kode);
            throw new DataException("Ukjent tiltakskode: " + kode);
        }
        return optURI.get();
    }
}

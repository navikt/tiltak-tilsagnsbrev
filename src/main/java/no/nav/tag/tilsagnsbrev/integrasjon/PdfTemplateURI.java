package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.konfigurasjon.PdfGenKonfig;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        final URI ARBTREN = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-arbeidsforberedende-trening").path(CREATE_PDF).build().toUri();
        final URI EKSPEBIST = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-ekspertbistand").path(CREATE_PDF).build().toUri();
        final URI FUNKASS = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-funksjonsassistanse").path(CREATE_PDF).build().toUri();
        final URI INKLUTILS = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-inkluderingstilskudd").path(CREATE_PDF).build().toUri();
        final URI MENTOR = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-mentortilskudd").path(CREATE_PDF).build().toUri();
        final URI MIDLONTIL = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-midlertidig-lonnstilskudd").path(CREATE_PDF).build().toUri();
        final URI OPPLAR = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-opplaeringstiltak").path(CREATE_PDF).build().toUri();
        final URI VARLONTIL = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-varig-lonnstilskudd").path(CREATE_PDF).build().toUri();
        final URI TILRARB = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-varig-tilrettelagt-arbeid").path(CREATE_PDF).build().toUri();
        final URI VATIAROR = UriComponentsBuilder.fromUri(host).path(PREFIX).path("tiltak-tilsagnsbrev-varig-tilrettelagt-ordinar").path(CREATE_PDF).build().toUri();

        tiltaksKodeURI.put("ARBTREN", ARBTREN);
        tiltaksKodeURI.put("EKSPEBIST", EKSPEBIST);
        tiltaksKodeURI.put("FUNKASS", FUNKASS); //TODO
        tiltaksKodeURI.put("INKLUTILS", INKLUTILS);
        tiltaksKodeURI.put("MENTOR", MENTOR);
        tiltaksKodeURI.put("MIDLONTIL", MIDLONTIL);
        tiltaksKodeURI.put("OPPLAR", OPPLAR); //TODO
        tiltaksKodeURI.put("VARLONTIL", VARLONTIL);
        tiltaksKodeURI.put("TILRARB", TILRARB); //TODO
        tiltaksKodeURI.put("VATIAROR", VATIAROR);
    }

    URI getTemplateURI (String kode) {
        return Optional.ofNullable(tiltaksKodeURI.get(kode))
                .orElseThrow(() -> new DataException("Ingen pdf template path oppgitt for tiltakskode " + kode));
    }
} //TODO Sjekk at alle tiltakskodene stemmer

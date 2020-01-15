package no.nav.tag.tilsagnsbrev.mapper;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.journalpost.*;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnNummer;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TilsagnJournalpostMapper {

    private Map<String, String> tiltaksKodeJPTittel = new HashMap<String, String>();

    public TilsagnJournalpostMapper() {
        initJournalpostTittel();
    }

    public Journalpost tilsagnTilJournalpost(TilsagnUnderBehandling tilsagnUnderBehandling){
        try {
            return opprettJournalpost(tilsagnUnderBehandling.getTilsagn(), tilsagnUnderBehandling.getPdf());
        } catch (Exception e){
            log.error("Feil ved mapping til Journalpost", e);
            throw new DataException(e.getMessage());
        }
    }

    private Journalpost opprettJournalpost(Tilsagn tilsagnsbrev, final byte[] pdfBytes) {
        Sak sak = new Sak(opprettArkivsaknr(tilsagnsbrev.getTilsagnNummer()));
        Bruker bruker = new Bruker(tilsagnsbrev.getTiltakArrangor().getOrgNummer());
        Mottaker mottaker = new Mottaker(tilsagnsbrev.getTiltakArrangor().getOrgNummer(), tilsagnsbrev.getTiltakArrangor().getArbgiverNavn());
        final String base64EncodetPdf = Base64.getEncoder().encodeToString(pdfBytes);
        Dokument dokument = new Dokument(getJournalpostTittel(tilsagnsbrev.getTiltakKode()), Collections.singletonList(new DokumentVariant(base64EncodetPdf)));
        Journalpost journalpost = new Journalpost(getJournalpostTittel(tilsagnsbrev.getTiltakKode()), bruker, mottaker, sak, Collections.singletonList(dokument));
        return journalpost;
    }

    private String opprettArkivsaknr(TilsagnNummer tilsagnNr){
        return new StringBuilder(tilsagnNr.getAar()).append(tilsagnNr.getLoepenrSak()).append(tilsagnNr.getLoepenrTilsagn()).toString();
    }

    private void initJournalpostTittel() {
        tiltaksKodeJPTittel.put("ARBFORB", "Tilsagnsbrev Arbeidsforberedende trening");
        tiltaksKodeJPTittel.put("EKSPEBIST", "Tilsagnsbrev Ekspertbistand");
        tiltaksKodeJPTittel.put("FUNKSJASS", "Tilsagnsbrev funksjonsassistanse");
        tiltaksKodeJPTittel.put("INKLUTILS", "Tilsagnsbrev Inkluderingstilskudd");
        tiltaksKodeJPTittel.put("MENTOR", "Tilsagnsbrev Mentor");
        tiltaksKodeJPTittel.put("MIDLONTIL", "Tilsagnsbrev Midlertidig lønnstilskudd");
        tiltaksKodeJPTittel.put("ENKELAMO", "Tilsagnsbrev Opplæring AMO");
        tiltaksKodeJPTittel.put("ENKFAGYRKE", "Tilsagnsbrev Enkeltplass fag og yrkesopplæring");
        tiltaksKodeJPTittel.put("GRUFAGYRKE", "Tilsagnsbrev Gruppe fag og yrkesopplæring");
        tiltaksKodeJPTittel.put("HOYEREUTD", "Tilsagnsbrev Opplæring høyere utdanning");
        tiltaksKodeJPTittel.put("VARLONTIL", "Tilsagnsbrev Varig lønnstilskudd");
        tiltaksKodeJPTittel.put("VASV", "Tilsagnsbrev Varig tilrettelagt arbeid");
        tiltaksKodeJPTittel.put("VATIAROR", "Tilsagnsbrev Varig tilrettelagt arbeid i ordinær virksomhet");
    }

    private String getJournalpostTittel(String kode) {
        Optional<String> optTittel = Optional.ofNullable (tiltaksKodeJPTittel.get(kode));
        if(optTittel.isEmpty()){
            final String alternativTittel = "Tilsagnsbrev " + kode;
            log.error("Ingen journalpost-tittel funnet for tiltakskode " + kode + ". Erstatter tittel med: " + alternativTittel);
            return alternativTittel;
        }
        return optTittel.get();
    }
}
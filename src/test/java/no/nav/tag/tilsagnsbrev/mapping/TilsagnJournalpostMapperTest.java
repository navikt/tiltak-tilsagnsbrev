package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TilsagnJournalpostMapperTest {

    TilsagnJournalpostMapper tilsagnJournalpostMapper = new TilsagnJournalpostMapper();
    Tilsagn tilsagn = Testdata. gruppeTilsagn();

    @Test
    public void mapperTilTittelEKSPEBIST(){
        tilsagn.setTiltakKode("EKSPEBIST");
        TilsagnUnderBehandling tub = Testdata.tubBuilder().tilsagn(tilsagn).pdf("pds".getBytes()).build();
        Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tub);
        assertEquals("Tilsagnsbrev Ekspertbistand", journalpost.getTittel());
        assertEquals("Tilsagnsbrev Ekspertbistand", journalpost.getDokumenter().get(0).getTittel());
    }

    @Test
    public void mapperTilTittelENKFAGYRKE(){
        tilsagn.setTiltakKode("ENKFAGYRKE");
        TilsagnUnderBehandling tub = Testdata.tubBuilder().tilsagn(tilsagn).pdf("pds".getBytes()).build();
        Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tub);
        assertEquals("Tilsagnsbrev Enkeltplass fag og yrkesopplæring", journalpost.getTittel());
        assertEquals("Tilsagnsbrev Enkeltplass fag og yrkesopplæring", journalpost.getDokumenter().get(0).getTittel());
    }
}
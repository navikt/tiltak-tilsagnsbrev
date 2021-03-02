package no.nav.tag.tilsagnsbrev.mapping;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.journalpost.Journalpost;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnNummer;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.exception.DataException;
import no.nav.tag.tilsagnsbrev.mapper.TilsagnJournalpostMapper;
import no.nav.tag.tilsagnsbrev.mapper.TiltakType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TilsagnJournalpostMapperTest {

    TilsagnJournalpostMapper tilsagnJournalpostMapper = new TilsagnJournalpostMapper();
    Tilsagn tilsagn = Testdata.gruppeTilsagn();
    TilsagnUnderBehandling tub = Testdata.tubBuilder().tilsagn(tilsagn).pdf("pds".getBytes()).build();

    @BeforeEach
    public void setUp() {
        tilsagn.setTilsagnNummer(new TilsagnNummer("2020", "575223", "1"));
    }

    @ParameterizedTest
    @EnumSource(TiltakType.class)
    public void mapperTilRikitigTiltak(TiltakType tiltakType) {
        tilsagn.setTiltakKode(tiltakType.getTiltakskode());
        Journalpost journalpost = tilsagnJournalpostMapper.tilsagnTilJournalpost(tub);
        assertEquals(tiltakType.getTiltakskode(), tilsagn.getTiltakKode());
        assertEquals(tiltakType.getTittel(), journalpost.getTittel());
        assertEquals(tiltakType.getTittel(), journalpost.getDokumenter().get(0).getTittel());
        assertEquals(tiltakType.getBrevkode(), journalpost.getDokumenter().get(0).getBrevkode());
        assertEquals("2020575223", journalpost.getSak().getFagsakId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ukjent"})
    public void finnerIkkeTiltakstype(String tiltakskode) {
        tilsagn.setTiltakKode(tiltakskode);
        assertThrows(DataException.class, () -> tilsagnJournalpostMapper.tilsagnTilJournalpost(tub));
    }

}
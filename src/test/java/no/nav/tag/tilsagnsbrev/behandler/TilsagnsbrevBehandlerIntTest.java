package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.ArenaMelding;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"dev", "local"})
@DirtiesContext
public class TilsagnsbrevBehandlerIntTest {

    @Autowired
    private IntegrasjonerMockServer mockServer;

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Autowired
    private TilsagnsbrevRepository tilsagnsbrevRepository;

    @Autowired
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    private TilsagnLoggCrudRepository loggCrudRepository;

    private final static String altinnFeilRespons = Testdata.hentFilString("altinn500Resp.xml");
    private static ArenaMelding arenaMelding = Testdata.arenaMelding();

    @After
    public void tearDown() {
        mockServer.getServer().resetAll();
        tilsagnsbrevRepository.deleteAll();
        loggCrudRepository.deleteAll();
    }

    @Before
    public void setUp(){
        mockServer.stubForAltOk();
    }

    @Test
    public void abryterHvisAlleredeLest(){
        TilsagnUnderBehandling tub = Testdata.tubBuilder().arenaMelding(arenaMelding).cid(UUID.randomUUID()).tilsagnsbrevId(111).build();

        assertTrue("Må logges før kjøring!", tilsagnLoggRepository.lagretIdHvisNyMelding(tub));
        tub.setCid(UUID.randomUUID()); //Blir lest fra topic igjen som en 'ny' melding

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tub);
        //Skal ikke gjennomføre disse kallene
        assertFalse(tub.erJournalfoert());
        assertTrue(tub.skalTilAltinn());
        assertFalse(tub.isBehandlet());
    }

    @Test
    public void oppretterIkkeTilsagnObjektFraTilsagnJson() {
        final UUID CID = UUID.randomUUID();
        final String feilbarGoldengateJson = Testdata.hentFilString("TILSAGN_DATA_feil.json");

        ArenaMelding feiler = Testdata.arenaMelding();
        feiler.getAfter().put("TILSAGN_DATA", feilbarGoldengateJson);
        TilsagnUnderBehandling tilsagnUnderBehandling = Testdata.tubBuilder().arenaMelding(feiler).cid(CID).build();

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = tilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertFalse(tub.skalRekjoeres());
            assertFalse(tub.isMappetFraArena());
            assertEquals(feilbarGoldengateJson, tub.getJson());
            assertNotNull(tub.getOpprettet());
            assertTrue("Ikke satt til datafeil", tub.isDatafeil());
            return tub;
        });
    }

    @Test
    public void pdfGenFeil() {
        mockServer.getServer().stubFor(post("/template/tiltak-tilsagnsbrev-midlertidig-lonnstilskudd/create-pdf").willReturn(serverError()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = Testdata.tubBuilder().arenaMelding(arenaMelding).cid(CID).build();

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        Optional<TilsagnUnderBehandling> feilet = tilsagnsbrevRepository.findById(CID);

        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertFalse(tub.erJournalfoert());
            assertTrue(tub.skalTilAltinn());
            assertNotNull(tub.getJson());
            return tub;
        });
    }

    @Test
    public void feilFraJoark() {

        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true").willReturn(unauthorized()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = Testdata.tubBuilder().arenaMelding(arenaMelding).cid(CID).build();

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = tilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertFalse(tub.erJournalfoert());
            assertNotNull(tub.getJson());
            assertNotNull(tub.getPdf());
            return tub;
        });
    }

    @Test
    public void feilFraAltinn() {

        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError().withBodyFile(altinnFeilRespons)));
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = Testdata.tubBuilder().arenaMelding(arenaMelding).cid(CID).build();

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = tilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertNotNull(tub.getJson());
            assertNotNull(tub.getPdf());
            return tub;
        });
    }

    @Test
    public void feilFraAltinnOgJoark() {

        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true").willReturn(unauthorized()));
        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError().withBodyFile(altinnFeilRespons)));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = Testdata.tubBuilder().arenaMelding(arenaMelding).cid(CID).build();

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = tilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertNotNull(tub.getJson());
            assertNotNull(tub.getPdf());
            return tub;
        });
    }
}

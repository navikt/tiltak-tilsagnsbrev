package no.nav.tag.tilsagnsbrev;

import com.github.tomakehurst.wiremock.WireMockServer;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
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
@ActiveProfiles("dev")
@DirtiesContext
public class TilsagnsbrevBehandlerIntTest {

    @Autowired
    IntegrasjonerMockServer mockServer;

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Autowired
    FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    final String goldengateJson = Testdata.hentFilString("arenaMelding.json");
    final String altinnOkRespons = Testdata.hentFilString("altinn200Resp.xml");
    final String altinnFeilRespons = Testdata.hentFilString("altinn500Resp.xml");

    @Before
    public void setUp() {
        mockServer.stubForAltOk();
    }

    @After
    public void tearDown(){
        mockServer.getServer().resetAll();
        feiletTilsagnsbrevRepository.deleteAll();
    }

    @Test
    public void behandlerTilsagn(){
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(UUID.randomUUID()).build();
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        assertTrue(feiletTilsagnsbrevRepository.findAll().isEmpty());
    }

    @Test
    public void parserIkkeArenaMelding(){
        final UUID CID = UUID.randomUUID();
        final String feilbarGoldengateJson = Testdata.hentFilString("arenaMelding_som_feiler.json");

        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(feilbarGoldengateJson).cid(CID).build();
        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
               assertFalse(tub.skalRekjoeres());
               assertFalse(tub.isMappetFraArena());
               assertEquals(feilbarGoldengateJson, tub.getJson());
               assertNotNull(tub.getOpprettet());
            return tub;
        });
    }

    @Test
    public void pdfGenFeil(){
        mockServer.getServer().stubFor(post("/template/tilsagnsbrev-gruppe/create-pdf").willReturn(serverError()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertFalse(tub.erJournalfoert());
            assertNotNull(tub.getJson());
            return tub;
        });
    }

    @Test
    public void feilFraJoark(){

        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true").willReturn(unauthorized()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertFalse(tub.erJournalfoert());
            assertNotNull(tub.getJson());
            return tub;
        });
    }

    @Test
    public void feilFraAltinn(){

        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError().withBodyFile(altinnFeilRespons)));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertNotNull(tub.getJson());
            return tub;
        });
    }

    @Test
    public void feilFraAltinnOgJoark(){

        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true").willReturn(unauthorized()));
        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError().withBodyFile(altinnFeilRespons)));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);

        tilsagnsbrevbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertNotNull(tub.getJson());
            return tub;
        });
    }
}
package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.mapper.json.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import static no.nav.tag.tilsagnsbrev.feilet.NesteSteg.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class TilsagnsbehandlerIntTest {

    @Autowired
    IntegrasjonerMockServer mockServer;

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    private Tilsagnsbehandler tilsagnsbehandler;

    @Autowired
    FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    final String goldengateJson = Testdata.hentFilString("goldengate.json");

    final String altinnOkResponse = Testdata.hentFilString("altinn200Resp.xml");

    @Before
    public void setUp(){
        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true")
                .willReturn(okJson("{\"pdf\" : \"[B@b78a709\"}")));
    }

    @After
    public void tearDown(){
        mockServer.getServer().resetAll();
        feiletTilsagnsbrevRepository.deleteAll();
    }

    @Test
    public void behandlerTilsagn(){

        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true")
                .willReturn(okJson("{\"journalpostId\" : \"001\", \"journalstatus\" : \"MIDLERTIDIG\", \"melding\" : \"Gikk bra\"}")));
        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(ok(altinnOkResponse)));

        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(UUID.randomUUID()).build();
        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        assertTrue(feiletTilsagnsbrevRepository.findAll().isEmpty());
    }

    @Test
    public void parserIkkeArenaMelding(){

        final UUID CID = UUID.randomUUID();
        final String feilbarGoldengateJson = Testdata.hentFilString("arena_melding_som_feiler.json");

        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(feilbarGoldengateJson).cid(CID).build();
        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
               assertFalse(tub.skalRekjoeres());
               assertEquals(START, tub.getNesteSteg());
               assertEquals(feilbarGoldengateJson, tub.getJson());
               assertNotNull(tub.getOpprettet());
            return tub;
        });
    }

    @Test
    public void pdfGenFeil(){

        mockServer.getServer().stubFor(post("/template/tilsagnsbrev/create-pdf").willReturn(serverError()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();

        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);
        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertEquals(JOURNALFOER, tub.getNesteSteg());
            assertNotNull(tub.getJson());
            assertTrue(tub.getFeilmelding().contains("500"));
            return tub;
        });
    }

    @Test
    public void feilFraJoark(){

        mockServer.getServer().stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true").willReturn(unauthorized()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);

        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertEquals(JOURNALFOER, tub.getNesteSteg());
            assertNotNull(tub.getJson());
            assertNotNull(tub.getFeilmelding());
            return tub;
        });
    }

    @Test
    @Ignore("Ikke klar")
    public void feilFraAltinn(){

        mockServer.getServer().stubFor(post("/rest/altinn").willReturn(unauthorized()));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).cid(CID).build();
        tilsagnJsonMapper.arenaMeldingTilTilsagn(tilsagnUnderBehandling);

        tilsagnsbehandler.behandleOgVerifisereTilsagn(tilsagnUnderBehandling);

        Optional<TilsagnUnderBehandling> feilet = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue(feilet.isPresent());
        feilet.map(tub -> {
            assertTrue(tub.skalRekjoeres());
            assertEquals(TIL_ALTINN, tub.getNesteSteg());
            assertNotNull(tub.getJson());
            assertTrue(tub.getFeilmelding().contains("401"));
            return tub;
        });
    }
}

package no.nav.tag.tilsagnsbrev;

import com.github.tomakehurst.wiremock.WireMockServer;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@DirtiesContext
public class TilsagnRetryProsessIntTest {

    @Autowired
    private IntegrasjonerMockServer mockServer;

    @Autowired
    private FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    final String altinnOkRespons = Testdata.hentFilString("altinn200Resp.xml");
    final String goldengateJson = Testdata.hentFilString("goldengate.json");

    @Before
    public void setUp() {
        stubForAltOk(mockServer.getServer());
    }

    @After
    public void tearDown() {
        mockServer.getServer().resetAll();
        feiletTilsagnsbrevRepository.deleteAll();
    }

    @Test
    public void behandlerTilsagnEtterEnMAnuellBehandlingAvArenaMappingFeil() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(goldengateJson).mappetFraArena(false).cid(CID).build();
        feiletTilsagnsbrevRepository.save(tilsagnUnderBehandling);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertTrue("Ikke behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertFalse("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
    }

    @Test
    public void behandlerTilsagnEtterFeiletJournalforing() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = TilsagnUnderBehandling.builder().cid(CID).json(Testdata.tilsagnTilJson(Testdata.tilsagnEnDeltaker())).mappetFraArena(true).altinnKittering(002).build();
        feiletTilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertTrue("Ikke behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertFalse("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
    }


    @Test
    public void behandlerTilsagnEtterFeiletAltinnSending() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = TilsagnUnderBehandling.builder().cid(CID).json(Testdata.tilsagnTilJson(Testdata.tilsagnEnDeltaker())).mappetFraArena(true).journalpostId("1234").build();
        feiletTilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertTrue("Ikke behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertFalse("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
    }

    private void stubForAltOk(WireMockServer server) {
        server.stubFor(post("/rest/journalpostapi/v1/journalpost?forsoekFerdigstill=true")
                .willReturn(okJson("{\"journalpostId\" : \"001\", \"journalstatus\" : \"MIDLERTIDIG\", \"melding\" : \"Gikk bra\"}")));
        server.stubFor(post("/template/tilsagnsbrev/create-pdf").willReturn(okJson("{\"pdf\" : \"[B@b78a709\"}")));
        server.stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(ok().withBody(altinnOkRespons)));
    }

}
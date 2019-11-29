package no.nav.tag.tilsagnsbrev.behandler;

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

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static org.junit.Assert.*;

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
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    private TilsagnRetryProsess tilsagnRetryProsess;

    @Autowired
    private TilsagnJsonMapper tilsagnJsonMapper;

    private final static String tilsagnData = Testdata.hentFilString("TILSAGN_DATA.json");
    private final static String altinnFeilRespons = Testdata.hentFilString("altinn500Resp.xml");

    @Before
    public void setUp() {
        mockServer.stubForAltOk();
    }

    @After
    public void tearDown() {
        mockServer.getServer().resetAll();
        feiletTilsagnsbrevRepository.deleteAll();
    }

    @Test
    public void behandlerTilsagnEtterEnManuellBehandlingAvArenaMappingFeil() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling tilsagnUnderBehandling = TilsagnUnderBehandling.builder().json(tilsagnData).mappetFraArena(false).cid(CID).tilsagnsbrevId(1).build();
        feiletTilsagnsbrevRepository.save(tilsagnUnderBehandling);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertTrue("Ikke behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertFalse("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }

    @Test
    public void behandlerTilsagnEtterFeiletJournalforing() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = TilsagnUnderBehandling.builder().cid(CID).json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).altinnKittering(002).build();
        feiletTilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertTrue("Ikke behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertFalse("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }


    @Test
    public void behandlerTilsagnEtterFeiletAltinnSending() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = TilsagnUnderBehandling.builder().cid(CID).json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1234").build();
        feiletTilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertTrue("Ikke behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertFalse("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }

    @Test
    public void feilerIgjenEtterFeiletAltinnSending() {
        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError().withBodyFile(altinnFeilRespons)));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = TilsagnUnderBehandling.builder().retry(1).cid(CID).json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1234").build();
        feiletTilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = feiletTilsagnsbrevRepository.findById(CID);
        assertTrue("Tilsagn ikke i database", opt.isPresent());

        TilsagnUnderBehandling tub = opt.get();
        assertFalse("Behandlet", tub.isBehandlet());
        assertTrue("MappetFraArena", tub.isMappetFraArena());
        assertTrue("Til Altinn", tub.skalTilAltinn());
        assertTrue("Journalført", tub.erJournalfoert());
        assertEquals("Feil retry", 2, tub.getRetry());
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }

    private void assertMeldingBleIkkeLoggetVedRetry(Integer tilsagnsbrevId){
         assertFalse(tilsagnLoggRepository.tilsagnsbevIdFinnes(tilsagnsbrevId));
    }
}

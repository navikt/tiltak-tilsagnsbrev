package no.nav.tag.tilsagnsbrev.behandler;

import no.nav.tag.tilsagnsbrev.DatoUtils;
import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer.topic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("local")
@EmbeddedKafka(partitions = 1, topics = topic)
@DirtiesContext
public class TilsagnRetryProsessIntTest {

    @Autowired
    private IntegrasjonerMockServer mockServer;

    @Autowired
    private TilsagnsbrevRepository tilsagnsbrevRepository;

    @Autowired
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    private TilsagnRetryProsess tilsagnRetryProsess;

    private final static String tilsagnData = Testdata.hentFilString("TILSAGN_DATA.json");
    private final static String altinnFeilRespons = Testdata.hentFilString("altinn500Resp.xml");

    @BeforeEach
    public void setUp() {
        mockServer.stubForAltOk();
    }

    @AfterEach
    public void tearDown() {
        mockServer.getServer().resetAll();
        tilsagnsbrevRepository.deleteAll();
    }

    @Test
    public void behandlerToFeiledeTilsagn() {
        final UUID CID1 = UUID.randomUUID();
        final UUID CID2 = UUID.randomUUID();
        TilsagnUnderBehandling tub1 = Testdata.tubBuilder().json(tilsagnData).mappetFraArena(false).cid(CID1).tilsagnsbrevId(1).opprettet(DatoUtils.getNow()).build();
        TilsagnUnderBehandling tub2 = Testdata.tubBuilder().json(tilsagnData).mappetFraArena(true).pdfJoark("pdf".getBytes()).altinnReferanse(1).cid(CID2).opprettet(DatoUtils.getNow()).tilsagnsbrevId(2).build();

        tilsagnsbrevRepository.saveAll(Arrays.asList(tub1, tub2));

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        List<TilsagnUnderBehandling> tubList = tilsagnsbrevRepository.findAll();

        assertEquals(2, tubList.size());
        assertTrue(tubList.stream().anyMatch(tub -> tub.getCid().equals(CID1)));
        assertTrue(tubList.stream().anyMatch(tub -> tub.getCid().equals(CID2)));

        tubList.stream().forEach(tub -> {
            assertTrue(tub.isBehandlet(), "Ikke behandlet");
            assertTrue(tub.isMappetFraArena(), "MappetFraArena");
            assertFalse(tub.skalTilAltinn(), "Til Altinn");
            assertTrue(tub.erJournalfoert(), "Journalført");
            assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
        });
    }

    @Test
    public void behandlerEnAvToTilsagn() {
        final UUID CID1 = UUID.randomUUID();
        final UUID CID2 = UUID.randomUUID();
        TilsagnUnderBehandling ok = Testdata.tubBuilder().behandlet(true).cid(CID1).retry(0).build();
        TilsagnUnderBehandling feil = Testdata.tubBuilder().json(tilsagnData).pdfAltinn("pdf".getBytes()).cid(CID2).retry(0).build();

        tilsagnsbrevRepository.saveAll(Arrays.asList(ok, feil));

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        List<TilsagnUnderBehandling> tubList = tilsagnsbrevRepository.findAll();

        assertEquals(2, tubList.size());
        assertTrue(tubList.stream().anyMatch(tub -> tub.getCid().equals(CID1) && tub.getRetry() == 0));
        assertTrue(tubList.stream().anyMatch(tub -> tub.getCid().equals(CID2) && tub.getRetry() == 1));
    }

    @Test
    public void behandlerTilsagnEtterFeiletJournalforing() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = Testdata.tubBuilder().cid(CID).json(tilsagnData).pdfJoark("pdf".getBytes()).tilsagnsbrevId(1).mappetFraArena(true).altinnReferanse(002).build();
        tilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = tilsagnsbrevRepository.findById(CID);
        assertTrue(opt.isPresent(), "Tilsagn ikke i database");

        TilsagnUnderBehandling tub = opt.get();
        assertTrue(tub.isBehandlet(), "Ikke behandlet");
        assertTrue(tub.isMappetFraArena(), "MappetFraArena");
        assertFalse(tub.skalTilAltinn(), "Til Altinn");
        assertTrue(tub.erJournalfoert(), "Journalført");
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }


    @Test
    public void behandlerTilsagnEtterFeiletAltinnSending() {
        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = Testdata.tubBuilder().cid(CID).json(tilsagnData).pdfAltinn("pdf".getBytes()).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1234").build();
        tilsagnsbrevRepository.save(feilet);

        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = tilsagnsbrevRepository.findById(CID);
        assertTrue(opt.isPresent(), "Tilsagn ikke i database");

        TilsagnUnderBehandling tub = opt.get();
        assertTrue(tub.isBehandlet(), "Ikke behandlet");
        assertTrue(tub.isMappetFraArena(), "MappetFraArena");
        assertFalse(tub.skalTilAltinn(), "Til Altinn");
        assertTrue(tub.erJournalfoert(), "Journalført");
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }

    @Test
    public void feilerIgjenEtterFeiletAltinnSending() {
        mockServer.getServer().stubFor(post("/ekstern/altinn/BehandleAltinnMelding/v1").willReturn(serverError().withBodyFile(altinnFeilRespons)));

        final UUID CID = UUID.randomUUID();
        TilsagnUnderBehandling feilet = Testdata.tubBuilder().retry(1).cid(CID).json(tilsagnData).tilsagnsbrevId(1).mappetFraArena(true).journalpostId("1234").build();

        tilsagnsbrevRepository.save(feilet);
        tilsagnRetryProsess.finnOgRekjoerFeiletTilsagn();

        Optional<TilsagnUnderBehandling> opt = tilsagnsbrevRepository.findById(CID);
        assertTrue(opt.isPresent(), "Tilsagn ikke i database");

        TilsagnUnderBehandling tub = opt.get();
        assertFalse(tub.isBehandlet(), "Behandlet");
        assertTrue(tub.isMappetFraArena(), "MappetFraArena");
        assertTrue(tub.skalTilAltinn(), "Til Altinn");
        assertTrue(tub.erJournalfoert(), "Journalført");
        assertEquals(2, tub.getRetry(), "Feil retry");
        assertEquals(feilet.getOpprettet(), tub.getOpprettet());
        assertMeldingBleIkkeLoggetVedRetry(tub.getTilsagnsbrevId());
    }

    private void assertMeldingBleIkkeLoggetVedRetry(Integer tilsagnsbrevId) {
        assertFalse(tilsagnLoggRepository.tilsagnsbevIdFinnes(tilsagnsbrevId));
    }
}

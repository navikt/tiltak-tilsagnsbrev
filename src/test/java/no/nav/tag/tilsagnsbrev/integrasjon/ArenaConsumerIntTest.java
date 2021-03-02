package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLogg;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggCrudRepository;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggRepository;
import no.nav.tag.tilsagnsbrev.feilet.TilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static no.nav.tag.tilsagnsbrev.integrasjon.ArenaConsumer.topic;
import static org.junit.Assert.*;

@ActiveProfiles("local")
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = topic)
public class ArenaConsumerIntTest {

    @Autowired
    private TilsagnsbrevRepository tilsagnsbrevRepository;

    @Autowired
    private TilsagnLoggCrudRepository loggCrudRepository;

    @Autowired
    private TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    private IntegrasjonerMockServer mockServer;

    private static final long SLEEP_LENGTH = 1000L;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    public void setUp(){
        mockServer.stubForAltOk();
    }

    @AfterEach
    public void tearDown() {
        mockServer.getServer().resetAll();
        tilsagnsbrevRepository.deleteAll();
        loggCrudRepository.deleteAll();
    }

    @Test
    public void behandlerTilsagnsbrev() throws InterruptedException {
        final String arenamelding = Testdata.hentFilString("arenaMelding.json");

        kafkaTemplate.send(topic, "", arenamelding);
        Thread.sleep(SLEEP_LENGTH);

        assertTrue(tilsagnLoggRepository.tilsagnsbevIdFinnes(111));
        TilsagnLogg tilsagnLogg = loggCrudRepository.findAll().iterator().next();
        assertTrue(tilsagnsbrevRepository.existsById(tilsagnLogg.getId()));
        assertEquals(1, tilsagnsbrevRepository.findAll().stream().filter(tub -> tub.getTilsagnsbrevId() == 111).count());
        assertTrue(tilsagnsbrevRepository.findAll().stream().filter(tub -> tub.getTilsagnsbrevId() == 111).allMatch(tub -> tub.isBehandlet()));

    }

    @Test
    public void feilVedUtpakkingAvArenaMelding() throws InterruptedException {

        assertTrue(tilsagnsbrevRepository.findAll().isEmpty());
        assertFalse(loggCrudRepository.findAll().iterator().hasNext());

        final String arenameldingMedFeil = Testdata.hentFilString("arenaMelding_som_feiler.json");
        kafkaTemplate.send(topic, "", arenameldingMedFeil);
        Thread.sleep(SLEEP_LENGTH);

        assertTrue("feil-database",tilsagnsbrevRepository.findAll().isEmpty());
        assertTrue("logg-database", loggCrudRepository.findAll().isEmpty());
    }

    @Test
    public void enFeilOgEnOkArenaMelding() throws InterruptedException {

        assertTrue(tilsagnsbrevRepository.findAll().isEmpty());
        assertTrue(loggCrudRepository.findAll().isEmpty());

        final String arenameldingMedFeil = Testdata.hentFilString("arenaMelding_som_feiler.json");
        final String arenameldingOk = Testdata.hentFilString("arenaMelding.json");
        kafkaTemplate.send(topic, "", arenameldingMedFeil);
        kafkaTemplate.send(topic, "", arenameldingOk);
        Thread.sleep(SLEEP_LENGTH);

        assertEquals(1, tilsagnsbrevRepository.findAll().stream().filter(tub -> tub.getTilsagnsbrevId() == 111).count());
        assertTrue(tilsagnsbrevRepository.findAll().stream().filter(tub -> tub.getTilsagnsbrevId() == 111).allMatch(tub -> tub.isBehandlet()));

        TilsagnLogg tilsagnLogg = loggCrudRepository.findAll().get(0);
        assertEquals(111, tilsagnLogg.getTilsagnsbrevId().intValue());
        assertNotNull(tilsagnLogg.getTidspunktLest());
    }
}
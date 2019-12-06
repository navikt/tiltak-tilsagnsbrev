package no.nav.tag.tilsagnsbrev.integrasjon;

import no.nav.tag.tilsagnsbrev.Testdata;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLogg;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggCrudRepository;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnLoggRepository;
import no.nav.tag.tilsagnsbrev.behandler.TilsagnsbrevBehandler;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import no.nav.tag.tilsagnsbrev.feilet.FeiletTilsagnsbrevRepository;
import no.nav.tag.tilsagnsbrev.simulator.IntegrasjonerMockServer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles({"dev","kafka"})
@DirtiesContext
public class TiltakTilsagnsbrevIntTest {

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, ArenaConsumer.topic);

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private TilsagnsbrevBehandler tilsagnsbrevbehandler;

    @Autowired
    private FeiletTilsagnsbrevRepository feiletTilsagnsbrevRepository;

    @Autowired
    private TilsagnLoggCrudRepository loggCrudRepository;

    @Autowired
    TilsagnLoggRepository tilsagnLoggRepository;

    @Autowired
    IntegrasjonerMockServer mockServer;

    private static final long SLEEP_LENGTH = 500L;
    private KafkaTemplate<String, String> kafkaTemplate;

    @AfterClass
    public static void tearDownAfterClass(){
        embeddedKafkaRule.getEmbeddedKafka().destroy();
    }

    @Before
    public void setUp(){
        Map<String, Object> senderProps = KafkaTestUtils.senderProps(embeddedKafkaRule.getEmbeddedKafka().getBrokersAsString());
        senderProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        senderProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory(senderProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(ArenaConsumer.topic);
        kafkaListenerEndpointRegistry.getListenerContainers()
                .forEach(messageListenerContainer ->
                        ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaRule.getEmbeddedKafka().getPartitionsPerTopic()));
    }

    @After
    public void tearDown() {
        mockServer.getServer().resetAll();
        feiletTilsagnsbrevRepository.deleteAll();
        loggCrudRepository.deleteAll();
    }

    @Test
    public void behandlerTilsagnsbrev() throws InterruptedException {
        final String arenamelding = Testdata.hentFilString("arenaMelding.json");

        kafkaTemplate.send(ArenaConsumer.topic, "", arenamelding);
        Thread.sleep(SLEEP_LENGTH);

        assertTrue(tilsagnLoggRepository.tilsagnsbevIdFinnes(111));
        TilsagnLogg tilsagnLogg = loggCrudRepository.findAll().iterator().next();
        assertFalse(feiletTilsagnsbrevRepository.existsById(tilsagnLogg.getId()));
    }

    @Test
    public void feilVedUtpakkingAvArenaMelding() throws InterruptedException {

        assertTrue(feiletTilsagnsbrevRepository.findAll().isEmpty());
        assertFalse(loggCrudRepository.findAll().iterator().hasNext());

        final String arenameldingMedFeil = Testdata.hentFilString("arenaMelding_som_feiler.json");
        kafkaTemplate.send(ArenaConsumer.topic, "", arenameldingMedFeil);
        Thread.sleep(SLEEP_LENGTH);

        List<TilsagnUnderBehandling> feiletList = feiletTilsagnsbrevRepository.findAll();
        assertEquals(feiletList.size() + " fra feil-database", 1, feiletList.size());
        TilsagnUnderBehandling feilet = feiletList.get(0);

        assertTrue(feilet.isDatafeil());
        assertNotNull(feilet.getOpprettet());
        assertNotNull(feilet.getCid());
        assertFalse(feilet.isMappetFraArena());

        TilsagnLogg tilsagnLogg = loggCrudRepository.findById(feilet.getCid()).orElseThrow();
        assertNotNull(tilsagnLogg.getTidspunktLest());
    }
}
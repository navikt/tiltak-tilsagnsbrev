package no.nav.tag.tilsagnsbrev;

import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.Tilsagn;
import no.nav.tag.tilsagnsbrev.integrasjon.AltInnService;
import no.nav.tag.tilsagnsbrev.integrasjon.PdfGenService;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnJsonMapper;
import no.nav.tag.tilsagnsbrev.mapping.TilsagnTilAltinnXml;
import no.nav.tag.tilsagnsbrev.simulator.Testdata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Tilsagnsbehandler {

    @Autowired
    TilsagnJsonMapper tilsagnJsonMapper;

    @Autowired
    TilsagnTilAltinnXml tilsagnTilAltinnXml;

    @Autowired
    PdfGenService pdfService;

    @Autowired
    AltInnService altInnService;

    public void behandleTilsagn(String goldenGateJson) {

        final Tilsagn tilsagn = tilsagnJsonMapper.goldengateMeldingTilTilsagn(goldenGateJson);
        final String tilsagnJson = tilsagnJsonMapper.tilsagnTilPdfJson(tilsagn);

        log.info("Tilsagnsbrev til pdfGen: {}", tilsagnJson);

        //TODO Til pdf-gen er klar
        final byte[] pdf = Testdata.hentFilBytes("dummy.pdf"); // = pdfService.tilsagnTilPdfBrev(tilsagnJson);

        final String tilsagnXml = tilsagnTilAltinnXml.tilAltinnMelding(tilsagn, pdf);

        log.info("Tilsagnsbrev til Altinn: {}", tilsagnXml);

        altInnService.sendTilsagnsbrev(tilsagnXml);

        //Tilsagn til joark

    }
}

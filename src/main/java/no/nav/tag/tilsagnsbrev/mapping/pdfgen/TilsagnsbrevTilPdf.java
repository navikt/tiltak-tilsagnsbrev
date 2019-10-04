package no.nav.tag.tilsagnsbrev.mapping.pdfgen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.StringTemplateSource;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import no.nav.tag.tilsagnsbrev.domene.Tilsagnsbrev;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TilsagnsbrevTilPdf {

    private InputStream fontIS = TilsagnsbrevTilPdf.class.getResourceAsStream("/pdf/times_new_roman.ttf");
    private InputStream fontBoldIS = TilsagnsbrevTilPdf.class.getResourceAsStream("/pdf/times_new_roman_bold.ttf"); //TODO bruke disse
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StringTemplateSource templateSource = new StringTemplateSource("tilsagnsbrev.hbs", hentMal());
    private final Handlebars handlebars = new Handlebars();
    private Template template;

    public TilsagnsbrevTilPdf() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //TODO datoformat, ev. dato bare som string
        template = handlebars.compile(templateSource);
    }


    public byte[] lagPdf(Tilsagnsbrev tilsagnsbrev) throws Exception {

        JsonNode jsonNode = objectMapper.valueToTree(tilsagnsbrev);
        Document w3cDok = render(jsonNode);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder pdfRendererBuilder = new PdfRendererBuilder();
            pdfRendererBuilder
                .useFont(() -> fontIS, "Times New Roman")
                .useFont(() -> fontBoldIS, "Times New Roman")
                    .useSVGDrawer(new BatikSVGDrawer())
//                .usePdfAConformance(PdfRendererBuilder.PdfAConformance.PDFA_2_U)
                    .withW3cDocument(w3cDok, "")
                    .toStream(baos)
                    .buildPdfRenderer()
                    .createPDF();
            return baos.toByteArray();
        }
    }

    public Document render(JsonNode jsonNode) {
        String html;

        try {
            html = template.apply(Context
                    .newBuilder(jsonNode)
                    .resolver(JsonNodeValueResolver.INSTANCE,
                            MapValueResolver.INSTANCE)
                    .build());
        } catch (IOException e) {
            throw new RuntimeException("Problem med innlasting av PDF mal", e);
        }

        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        return new W3CDom().fromJsoup(doc);
    }

//    private void registerHepler(){
//        handlebars.registerHelper("image", new Helper<String>() {
//            if
//        });
//
//        registerHelper("image", Helper<String> { context, _ ->
//        if (context == null) "" else images[context]
//        })
//    }

    private String hentMal() {
        try{
        Path fil = Paths.get(getClass().getClassLoader()
                .getResource("pdf/tilsagnsbrev.hbs").toURI());
        return Files.readString(fil, Charset.forName("UTF-8")); // utf 8
        } catch (Exception e){
            throw new RuntimeException("Problem med innlasting av PDF mal", e);
        }
    }

}

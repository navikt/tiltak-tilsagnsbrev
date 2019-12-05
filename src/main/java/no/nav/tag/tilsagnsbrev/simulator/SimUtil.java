package no.nav.tag.tilsagnsbrev.simulator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimUtil {

    public static String lesFil(String filnavn) {
        try {
            Path fil = Paths.get(SimUtil.class.getClassLoader()
                    .getResource(filnavn).toURI());
            return Files.readString(fil);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TilsagnUnderBehandlingTest {

    @Test
    public void skalReskjores_returnerer_false_hvis_datafeil() {
        assertThat(TilsagnUnderBehandling.builder().datafeil(true).build().skalRekjoeres(), is(false));
    }

    @Test
    public void skalReskjores_returnerer_false_hvis_antall_er_storre_eller_lik_maks() {
        assertThat(TilsagnUnderBehandling.builder().datafeil(false).retry(TilsagnUnderBehandling.MAX_RETRIES).build().skalRekjoeres(), is(false));
    }

    @Test
    public void skalReskjores_returnerer_true_hvis_ikke_datafeil_og_antall_er_mindre_enn_maks() {
        assertThat(TilsagnUnderBehandling.builder().datafeil(false).retry(TilsagnUnderBehandling.MAX_RETRIES - 1).build().skalRekjoeres(), is(true));
    }

}

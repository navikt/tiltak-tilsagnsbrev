package no.nav.tag.tilsagnsbrev.behandler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class TilsagnLoggRepository {

   private final JdbcTemplate jdbcTemplate;

    public boolean lagretIdHvisNyMelding(TilsagnUnderBehandling tilsagnUnderBehandling) {
        boolean erNyMelding = !tilsagnsbevIdFinnes(tilsagnUnderBehandling.getTilsagnsbrevId());
        if (erNyMelding) {
            TilsagnLogg tilsagnLogg = new TilsagnLogg(tilsagnUnderBehandling.getCid(), tilsagnUnderBehandling.getTilsagnsbrevId());
            lagre(tilsagnLogg);
            log.info("Melding med tilsagnsbrev-id {} registrert i logg", tilsagnLogg.getTilsagnsbrevId());
        }
        return erNyMelding;
    }

    public boolean tilsagnsbevIdFinnes(int tilsagnsbrevId) {
        return jdbcTemplate.queryForObject("select exists (select 1 from tilsagn_logg where tilsagnsbrev_id = ?)", Boolean.class, tilsagnsbrevId);
    }

    private void lagre(TilsagnLogg tilsagnLogg) {
        jdbcTemplate.update("insert into tilsagn_logg values(?, ?, ?)", tilsagnLogg.getId(), tilsagnLogg.getTilsagnsbrevId(), tilsagnLogg.getTidspunktLest());
    }



}

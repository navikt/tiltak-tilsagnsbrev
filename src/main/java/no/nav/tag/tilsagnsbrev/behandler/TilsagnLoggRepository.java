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

    private static final String SQL_ID_FINNES = "select exists (select 1 from tilsagn_logg where tilsagnsbrev_id = ?)";
    private static final String SQL_INSERT_LOGG = "insert into tilsagn_logg values(?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public boolean registrerNyMelding(TilsagnUnderBehandling tilsagnUnderBehandling) {
        Integer tilsagnsbrevId = tilsagnUnderBehandling.getTilsagnsbrevId();

        if (tilsagnsbevIdFinnes(tilsagnsbrevId)) {
            return false;
        }

        TilsagnLogg tilsagnLogg = new TilsagnLogg(
            tilsagnUnderBehandling.getCid(),
            tilsagnsbrevId
        );
        lagre(tilsagnLogg);
        log.info("Melding med tilsagnsbrev-id {} registrert i logg", tilsagnsbrevId);

        return true;
    }

    public boolean tilsagnsbevIdFinnes(Integer tilsagnsbrevId) {
        if(tilsagnsbrevId == null){
            return false;
        }
        return jdbcTemplate.queryForObject(SQL_ID_FINNES, Boolean.class, tilsagnsbrevId);
    }

    private void lagre(TilsagnLogg tilsagnLogg) {
        jdbcTemplate.update(SQL_INSERT_LOGG, tilsagnLogg.getId(), tilsagnLogg.getTilsagnsbrevId(), tilsagnLogg.getTidspunktLest());
    }
}

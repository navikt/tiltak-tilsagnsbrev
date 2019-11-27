package no.nav.tag.tilsagnsbrev;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TilsagnLoggReository extends CrudRepository<Integer, Integer> {

}

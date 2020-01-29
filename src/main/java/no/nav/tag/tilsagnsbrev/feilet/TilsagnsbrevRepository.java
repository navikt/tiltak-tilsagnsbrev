package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TilsagnsbrevRepository extends CrudRepository<TilsagnUnderBehandling, UUID> {

    @Override
    List<TilsagnUnderBehandling> findAll();

    @Query(value = "select tub from TilsagnUnderBehandling tub where tub.behandlet = false")
    List<TilsagnUnderBehandling> findFailed();
}
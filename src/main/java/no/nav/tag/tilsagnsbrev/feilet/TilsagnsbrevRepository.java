package no.nav.tag.tilsagnsbrev.feilet;

import no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev.TilsagnUnderBehandling;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TilsagnsbrevRepository extends CrudRepository<TilsagnUnderBehandling, UUID> {

    @Override
    List<TilsagnUnderBehandling> findAll();
}


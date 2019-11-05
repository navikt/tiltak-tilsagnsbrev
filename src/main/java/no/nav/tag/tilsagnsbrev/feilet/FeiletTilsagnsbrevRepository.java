package no.nav.tag.tilsagnsbrev.feilet;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FeiletTilsagnsbrevRepository extends CrudRepository<FeiletTilsagnsbrev, UUID> {

    @Override
    List<FeiletTilsagnsbrev> findAll();
}


package no.nav.tag.tilsagnsbrev.behandler;

import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@ActiveProfiles("local")
public interface TilsagnLoggCrudRepository extends CrudRepository<TilsagnLogg, UUID> {

    @Override
    List<TilsagnLogg> findAll();
}

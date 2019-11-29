package no.nav.tag.tilsagnsbrev.behandler;

import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@ActiveProfiles("dev")
interface TilsagnLoggCrudRepository extends CrudRepository<TilsagnLogg, UUID> {

}

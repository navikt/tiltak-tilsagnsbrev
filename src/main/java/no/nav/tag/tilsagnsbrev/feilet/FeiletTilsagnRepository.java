package no.nav.tag.tilsagnsbrev.feilet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface FeiletTilsagnRepository extends CrudRepository<FeiletTilsagn, UUID> {

    @Query(value = "select f.id from feilet_tilsagnsbrev f where f.retry is < 5")
    List<FeiletTilsagn> finnTilsagnTilRekjoring();

}


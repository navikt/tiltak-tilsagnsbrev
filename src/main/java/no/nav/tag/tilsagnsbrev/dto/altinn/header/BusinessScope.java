package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessScope {
    List< Scope > Scope = new ArrayList<>();
}

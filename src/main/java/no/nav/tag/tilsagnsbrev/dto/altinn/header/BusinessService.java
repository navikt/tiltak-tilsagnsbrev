package no.nav.tag.tilsagnsbrev.dto.altinn.header;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusinessService {
    String BusinessServiceName; //ALTINNRapportere
    String ServiceTransaction; //xml attributter
}

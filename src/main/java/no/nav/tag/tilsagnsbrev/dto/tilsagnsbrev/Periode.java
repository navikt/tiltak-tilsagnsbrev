package no.nav.tag.tilsagnsbrev.dto.tilsagnsbrev;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Periode {

    private LocalDate fraDato;
    private LocalDate tilDato;
}

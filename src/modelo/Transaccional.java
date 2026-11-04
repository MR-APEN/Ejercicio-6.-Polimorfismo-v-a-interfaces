package modelo;

import java.math.BigDecimal;

public interface Transaccional {
    default ResultadoOperacion cobrar(BigDecimal monto) {
        return ResultadoOperacion.ignorado("Esta integración no admite cobros; se continúa sin cambios.");
    }

    default ResultadoOperacion reintegrar(BigDecimal monto) {
        return ResultadoOperacion.ignorado("Esta integración no admite reintegros; se continúa sin cambios.");
    }
}

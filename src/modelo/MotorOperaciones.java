package modelo;

import java.math.BigDecimal;

public final class MotorOperaciones {
    public ResultadoOperacion procesar(Transaccional servicio, BigDecimal monto) {
        return procesar(servicio, monto, TipoTransaccion.COBRO);
    }

    public ResultadoOperacion procesar(Transaccional servicio, BigDecimal monto, TipoTransaccion tipo) {
        if (servicio == null || tipo == null) {
            return ResultadoOperacion.error("Falta servicio o tipo de transacción.");
        }
        return tipo == TipoTransaccion.COBRO ? servicio.cobrar(monto) : servicio.reintegrar(monto);
    }

    public ResultadoOperacion generar(Documentable servicio, String referencia) {
        return servicio == null ? ResultadoOperacion.error("Falta servicio documental.")
                : servicio.generarDocumento(referencia);
    }

    public String auditar(Auditable servicio) {
        return servicio == null ? "[ERROR] Falta servicio auditable." : servicio.consultarAuditoria();
    }
}

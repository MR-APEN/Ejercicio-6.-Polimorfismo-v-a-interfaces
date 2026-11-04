package modelo;

import java.math.BigDecimal;

public class ProveedorLogistico extends IntegracionAPI {
    private final BigDecimal tarifaBase;

    public ProveedorLogistico(String id, String nombre, String endpoint, int timeoutMs,
            double latenciaMs, EstadoConexion estado, BigDecimal tarifaBase) {
        super(id, nombre, endpoint, timeoutMs, latenciaMs, estado);
        if (tarifaBase == null || tarifaBase.signum() < 0 || tarifaBase.scale() > 2) {
            throw new IllegalArgumentException("Tarifa inválida.");
        }
        this.tarifaBase = tarifaBase.setScale(2);
    }

    @Override
    public ResultadoOperacion consultar(String referencia) {
        ResultadoOperacion validacion = super.consultar(referencia);
        if (!validacion.esExito()) { return validacion; }
        return ResultadoOperacion.exito("Envío " + referencia.trim()
                + ": EN TRÁNSITO (simulado). Cotización base: Q " + tarifaBase
                + ". " + validarDisponibilidad().getMensaje());
    }

    @Override
    public ResultadoOperacion generarDocumento(String referencia) {
        ResultadoOperacion validacion = super.consultar(referencia);
        if (!validacion.esExito()) { return validacion; }
        String xml = "<Guia simulada=\"true\">\n  <Proveedor>" + escaparXml(getNombre())
                + "</Proveedor>\n  <Referencia>" + escaparXml(referencia.trim())
                + "</Referencia>\n  <Tarifa moneda=\"GTQ\">" + tarifaBase + "</Tarifa>\n</Guia>";
        return ResultadoOperacion.exito("Guía de demostración generada. "
                + validarDisponibilidad().getMensaje(), xml);
    }

    public BigDecimal getTarifaBase() { return tarifaBase; }
    @Override public String getTipo() { return "Logística"; }
    @Override public String getConfiguracion() {
        return super.getConfiguracion() + "\nTarifa base: Q " + tarifaBase;
    }
}

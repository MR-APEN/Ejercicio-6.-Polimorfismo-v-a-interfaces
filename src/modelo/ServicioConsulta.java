package modelo;

public final class ServicioConsulta extends IntegracionAPI {
    public ServicioConsulta(String id, String nombre, String endpoint, int timeoutMs,
            double latenciaMs, EstadoConexion estado) {
        super(id, nombre, endpoint, timeoutMs, latenciaMs, estado);
    }

    @Override
    public ResultadoOperacion consultar(String referencia) {
        ResultadoOperacion validacion = super.consultar(referencia);
        if (!validacion.esExito()) { return validacion; }
        return ResultadoOperacion.exito("Rastreo " + referencia.trim()
                + ": EN CENTRO DE DISTRIBUCIÓN (simulado). " + validarDisponibilidad().getMensaje());
    }

    @Override public String getTipo() { return "Solo consulta"; }
}

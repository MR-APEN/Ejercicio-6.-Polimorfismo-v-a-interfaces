package modelo;

public final class CertificadorFEL extends IntegracionAPI {
    public CertificadorFEL(String id, String nombre, String endpoint, int timeoutMs,
            double latenciaMs, EstadoConexion estado) {
        super(id, nombre, endpoint, timeoutMs, latenciaMs, estado);
    }

    @Override
    public ResultadoOperacion generarDocumento(String referencia) {
        ResultadoOperacion validacion = super.consultar(referencia);
        if (!validacion.esExito()) { return validacion; }
        String xml = "<FacturaDemo simulada=\"true\" validezFiscal=\"false\">\n  <Certificador>"
                + escaparXml(getNombre()) + "</Certificador>\n  <Referencia>"
                + escaparXml(referencia.trim()) + "</Referencia>\n</FacturaDemo>";
        return ResultadoOperacion.exito("XML de demostración generado; sin validez fiscal. "
                + validarDisponibilidad().getMensaje(), xml);
    }

    @Override public String getTipo() { return "FEL"; }
}

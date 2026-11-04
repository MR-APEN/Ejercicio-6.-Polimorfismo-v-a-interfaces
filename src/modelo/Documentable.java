package modelo;

public interface Documentable {
    default ResultadoOperacion generarDocumento(String referencia) {
        return ResultadoOperacion.ignorado("Esta integración no genera documentos; se continúa sin cambios.");
    }
}

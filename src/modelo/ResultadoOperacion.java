package modelo;

import java.util.Objects;

public final class ResultadoOperacion {
    private final EstadoOperacion estado;
    private final String mensaje;
    private final String contenido;

    private ResultadoOperacion(EstadoOperacion estado, String mensaje, String contenido) {
        this.estado = Objects.requireNonNull(estado);
        this.mensaje = Objects.requireNonNull(mensaje);
        this.contenido = Objects.requireNonNull(contenido);
    }

    public static ResultadoOperacion exito(String mensaje) {
        return exito(mensaje, "");
    }

    public static ResultadoOperacion exito(String mensaje, String contenido) {
        return new ResultadoOperacion(EstadoOperacion.EXITO, mensaje, contenido);
    }

    public static ResultadoOperacion ignorado(String mensaje) {
        return new ResultadoOperacion(EstadoOperacion.IGNORADO, mensaje, "");
    }

    public static ResultadoOperacion error(String mensaje) {
        return new ResultadoOperacion(EstadoOperacion.ERROR, mensaje, "");
    }

    public EstadoOperacion getEstado() { return estado; }
    public String getMensaje() { return mensaje; }
    public String getContenido() { return contenido; }
    public boolean esExito() { return estado == EstadoOperacion.EXITO; }

    @Override
    public String toString() {
        return "[" + estado + "] " + mensaje
                + (contenido.isEmpty() ? "" : System.lineSeparator() + contenido);
    }
}

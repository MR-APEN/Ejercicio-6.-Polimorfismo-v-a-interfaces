package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public abstract class IntegracionAPI implements Transaccional, Documentable,
        Auditable, Comparable<IntegracionAPI> {
    private final String id;
    private final String nombre;
    private final String endpoint;
    private final int timeoutMs;
    private boolean activa;
    private EstadoConexion estadoConexion;
    private double latenciaPromedioMs;
    private int totalEventos;
    private String ultimoEvento;

    protected IntegracionAPI(String id, String nombre, String endpoint, int timeoutMs,
            double latenciaPromedioMs, EstadoConexion estadoConexion) {
        this.id = exigirTexto(id, "identificador").toUpperCase(Locale.ROOT);
        this.nombre = exigirTexto(nombre, "nombre");
        this.endpoint = exigirTexto(endpoint, "endpoint");
        if (timeoutMs <= 0) {
            throw new IllegalArgumentException("El timeout debe ser positivo.");
        }
        this.timeoutMs = timeoutMs;
        this.activa = true;
        this.totalEventos = 0;
        this.ultimoEvento = "Sin operaciones registradas.";
        setEstadoConexion(estadoConexion);
        setLatenciaPromedioMs(latenciaPromedioMs);
    }

    private static String exigirTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("Falta " + campo + ".");
        }
        return texto.trim();
    }

    public abstract String getTipo();

    public ResultadoOperacion validarDisponibilidad() {
        if (!activa) {
            return ResultadoOperacion.error("La integración " + id + " está inactiva.");
        }
        if (estadoConexion == EstadoConexion.DESCONECTADA) {
            return ResultadoOperacion.error("La integración " + id + " está desconectada.");
        }
        return ResultadoOperacion.exito(estadoConexion == EstadoConexion.DEGRADADA
                ? "Conexión degradada: operación disponible con latencia elevada."
                : "Conexión disponible.");
    }

    public ResultadoOperacion consultar(String referencia) {
        ResultadoOperacion disponible = validarDisponibilidad();
        if (!disponible.esExito()) { return disponible; }
        if (!referenciaValida(referencia)) {
            return ResultadoOperacion.error("La referencia debe tener entre 1 y 80 caracteres.");
        }
        return ResultadoOperacion.exito("Consulta simulada " + referencia.trim()
                + ": " + getTipo() + " disponible. " + disponible.getMensaje());
    }

    protected final boolean referenciaValida(String referencia) {
        return referencia != null && !referencia.trim().isEmpty()
                && referencia.trim().length() <= 80;
    }

    protected final String escaparXml(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
    }

    @Override
    public void registrarEvento(String evento) {
        totalEventos++;
        ultimoEvento = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " | " + Objects.requireNonNull(evento);
    }

    @Override
    public String consultarAuditoria() {
        return "Integración: " + id + "\nOperaciones registradas: " + totalEventos
                + "\nÚltimo evento: " + ultimoEvento
                + "\nAuditoría de sesión: se conserva solo el último evento y el contador.";
    }

    public String getConfiguracion() {
        return "Identificador: " + id + "\nNombre: " + nombre + "\nTipo: " + getTipo()
                + "\nEndpoint simulado: " + endpoint + "\nTimeout: " + timeoutMs + " ms"
                + "\nActiva: " + (activa ? "Sí" : "No") + "\nConexión: " + estadoConexion
                + "\nLatencia promedio: " + String.format(Locale.ROOT, "%.1f", latenciaPromedioMs)
                + " ms\nEntorno: demostración local; sin credenciales ni llamadas HTTP.";
    }

    public final String getId() { return id; }
    public final String getNombre() { return nombre; }
    public final String getEndpoint() { return endpoint; }
    public final int getTimeoutMs() { return timeoutMs; }
    public final boolean isActiva() { return activa; }
    public final EstadoConexion getEstadoConexion() { return estadoConexion; }
    public final double getLatenciaPromedioMs() { return latenciaPromedioMs; }
    public final int getTotalEventos() { return totalEventos; }
    public final void setActiva(boolean activa) { this.activa = activa; }

    public final void setEstadoConexion(EstadoConexion estadoConexion) {
        this.estadoConexion = Objects.requireNonNull(estadoConexion, "Falta estado de conexión.");
    }

    public final void setLatenciaPromedioMs(double ms) {
        if (!Double.isFinite(ms) || ms < 0) {
            throw new IllegalArgumentException("La latencia debe ser finita y no negativa.");
        }
        this.latenciaPromedioMs = ms;
    }

    @Override
    public final int compareTo(IntegracionAPI otra) {
        int comparacion = Double.compare(latenciaPromedioMs, otra.latenciaPromedioMs);
        return comparacion != 0 ? comparacion : id.compareTo(otra.id);
    }

    @Override
    public final boolean equals(Object objeto) {
        if (this == objeto) { return true; }
        if (!(objeto instanceof IntegracionAPI)) { return false; }
        return id.equals(((IntegracionAPI) objeto).id);
    }

    @Override
    public final int hashCode() { return id.hashCode(); }

    @Override
    public String toString() {
        return id + " | " + nombre + " | " + getTipo() + " | " + estadoConexion
                + " | " + latenciaPromedioMs + " ms | " + (activa ? "activa" : "inactiva");
    }
}

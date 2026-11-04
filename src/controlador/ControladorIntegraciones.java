package controlador;

import java.math.BigDecimal;
import java.util.Objects;
import modelo.*;

public final class ControladorIntegraciones {
    private final CatalogoIntegraciones catalogo;
    private final MotorOperaciones motor;

    public ControladorIntegraciones(CatalogoIntegraciones catalogo, MotorOperaciones motor) {
        this.catalogo = Objects.requireNonNull(catalogo);
        this.motor = Objects.requireNonNull(motor);
    }

    public Iterable<IntegracionAPI> listar() { return catalogo; }
    public IntegracionAPI buscar(String id) { return catalogo.buscar(id); }
    public void ordenarPorLatencia() { catalogo.ordenarPorLatencia(); }

    private ResultadoOperacion validar(IntegracionAPI api) {
        return api == null ? ResultadoOperacion.error("No se encontró una API con ese identificador.")
                : api.validarDisponibilidad();
    }

    private ResultadoOperacion registrar(IntegracionAPI api, String accion, ResultadoOperacion resultado) {
        if (api != null) {
            api.registrarEvento(accion + " | " + resultado.getEstado() + " | " + resultado.getMensaje());
        }
        return resultado;
    }

    public ResultadoOperacion consultar(String id, String referencia) {
        IntegracionAPI api = catalogo.buscar(id);
        ResultadoOperacion validacion = validar(api);
        return registrar(api, "CONSULTA", validacion.esExito() ? api.consultar(referencia) : validacion);
    }

    public ResultadoOperacion cobrar(String id, BigDecimal monto) {
        IntegracionAPI api = catalogo.buscar(id);
        ResultadoOperacion validacion = validar(api);
        return registrar(api, "COBRO", validacion.esExito() ? motor.procesar(api, monto) : validacion);
    }

    public ResultadoOperacion reintegrar(String id, BigDecimal monto) {
        IntegracionAPI api = catalogo.buscar(id);
        ResultadoOperacion validacion = validar(api);
        return registrar(api, "REINTEGRO", validacion.esExito()
                ? motor.procesar(api, monto, TipoTransaccion.REINTEGRO) : validacion);
    }

    public ResultadoOperacion generarDocumento(String id, String referencia) {
        IntegracionAPI api = catalogo.buscar(id);
        ResultadoOperacion validacion = validar(api);
        return registrar(api, "DOCUMENTO", validacion.esExito() ? motor.generar(api, referencia) : validacion);
    }

    public String auditar(String id) {
        IntegracionAPI api = catalogo.buscar(id);
        return api == null ? "[ERROR] No se encontró una API con ese identificador." : motor.auditar(api);
    }
}

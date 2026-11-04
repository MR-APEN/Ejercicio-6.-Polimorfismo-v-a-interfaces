package modelo;

import java.math.BigDecimal;
import java.util.Objects;

public final class PasarelaPago extends IntegracionAPI {
    private final MedioPago medioPago;
    private BigDecimal saldo;

    public PasarelaPago(String id, String nombre, String endpoint, int timeoutMs,
            double latenciaMs, EstadoConexion estado, MedioPago medioPago) {
        super(id, nombre, endpoint, timeoutMs, latenciaMs, estado);
        this.medioPago = Objects.requireNonNull(medioPago);
        this.saldo = new BigDecimal("0.00");
    }

    private boolean montoValido(BigDecimal monto) {
        return monto != null && monto.signum() > 0 && monto.scale() <= 2;
    }

    @Override
    public ResultadoOperacion cobrar(BigDecimal monto) {
        ResultadoOperacion disponible = validarDisponibilidad();
        if (!disponible.esExito()) { return disponible; }
        if (!montoValido(monto)) {
            return ResultadoOperacion.error("El monto debe ser positivo y tener máximo dos decimales.");
        }
        saldo = saldo.add(monto).setScale(2);
        return ResultadoOperacion.exito("Cobro simulado de Q " + monto.setScale(2)
                + " mediante " + medioPago + ". Saldo neto: Q " + saldo + ". " + disponible.getMensaje());
    }

    @Override
    public ResultadoOperacion reintegrar(BigDecimal monto) {
        ResultadoOperacion disponible = validarDisponibilidad();
        if (!disponible.esExito()) { return disponible; }
        if (!montoValido(monto)) {
            return ResultadoOperacion.error("El monto debe ser positivo y tener máximo dos decimales.");
        }
        if (monto.compareTo(saldo) > 0) {
            return ResultadoOperacion.error("El reintegro supera el saldo neto disponible: Q " + saldo + ".");
        }
        saldo = saldo.subtract(monto).setScale(2);
        return ResultadoOperacion.exito("Reintegro simulado de Q " + monto.setScale(2)
                + ". Saldo neto: Q " + saldo + ". " + disponible.getMensaje());
    }

    public BigDecimal getSaldo() { return saldo; }
    public MedioPago getMedioPago() { return medioPago; }
    @Override public String getTipo() { return "Pago / " + medioPago; }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nMedio de pago: " + medioPago
                + "\nMoneda: GTQ\nSaldo neto de sesión: Q " + saldo;
    }
}

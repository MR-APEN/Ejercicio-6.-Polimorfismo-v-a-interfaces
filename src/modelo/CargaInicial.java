package modelo;

import java.math.BigDecimal;

public final class CargaInicial {
    private CargaInicial() { }

    public static CatalogoIntegraciones crearCatalogo() {
        CatalogoIntegraciones catalogo = new CatalogoIntegraciones();
        catalogo.agregar(new PasarelaPago("PAG-01", "Tarjeta Ágil", "https://tarjeta-agil.example/api",
                5000, 120, EstadoConexion.CONECTADA, MedioPago.TARJETA));
        catalogo.agregar(new PasarelaPago("PAG-02", "Tarjeta Segura", "https://tarjeta-segura.example/api",
                5000, 310, EstadoConexion.DEGRADADA, MedioPago.TARJETA));
        catalogo.agregar(new PasarelaPago("PAG-03", "Banco Uno", "https://banco-uno.example/api",
                8000, 220, EstadoConexion.CONECTADA, MedioPago.TRANSFERENCIA));
        catalogo.agregar(new PasarelaPago("PAG-04", "Banco Dos", "https://banco-dos.example/api",
                8000, 180, EstadoConexion.CONECTADA, MedioPago.TRANSFERENCIA));
        catalogo.agregar(new PasarelaPago("PAG-05", "Billetera Sol", "https://billetera-sol.example/api",
                4000, 95, EstadoConexion.CONECTADA, MedioPago.BILLETERA));
        catalogo.agregar(new PasarelaPago("PAG-06", "Billetera Luna", "https://billetera-luna.example/api",
                4000, 150, EstadoConexion.CONECTADA, MedioPago.BILLETERA));
        catalogo.agregar(new ProveedorLogistico("LOG-01", "Ruta Express", "https://ruta-express.example/api",
                6000, 240, EstadoConexion.CONECTADA, new BigDecimal("25.00")));
        catalogo.agregar(new ProveedorLogistico("LOG-02", "Entrega Local", "https://entrega-local.example/api",
                6000, 420, EstadoConexion.DESCONECTADA, new BigDecimal("18.50")));
        catalogo.agregar(new ServicioConsulta("CON-01", "Rastreo Central", "https://rastreo.example/api",
                3000, 80, EstadoConexion.CONECTADA));
        catalogo.agregar(new CertificadorFEL("FEL-01", "Certificador Demo", "https://fel-demo.example/api",
                7000, 280, EstadoConexion.CONECTADA));
        return catalogo;
    }
}

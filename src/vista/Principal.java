package vista;

import controlador.ControladorIntegraciones;
import modelo.CargaInicial;
import modelo.IntegracionAPI;
import modelo.MotorOperaciones;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public final class Principal {
    private final ControladorIntegraciones controlador;

    public Principal(ControladorIntegraciones controlador) { this.controlador = controlador; }

    public static void main(String[] args) {
        Principal vista = new Principal(new ControladorIntegraciones(
                CargaInicial.crearCatalogo(), new MotorOperaciones()));
        boolean grafica = args.length > 0 && "--gui".equalsIgnoreCase(args[0]);
        if (grafica && !GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(vista::ejecutarGrafica);
        } else {
            if (grafica) {
                System.out.println("No hay entorno gráfico disponible. Se abrirá la consola.");
            }
            vista.ejecutarConsola();
        }
    }

    private void ejecutarConsola() {
        System.out.println("GESTOR DE INTEGRACIONES | Ejercicio 6 | Prototipo simulado");
        System.out.println("10 APIs cargadas. Moneda: GTQ. Los datos duran esta sesión.");
        try (Scanner entrada = new Scanner(System.in, "UTF-8")) {
            boolean continuar = true;
            while (continuar) {
                System.out.println("\n1. Listar activas\n2. Buscar y ver configuración\n3. Ordenar por latencia"
                        + "\n4. Consultar estado / cotización\n5. Cobrar\n6. Reintegrar"
                        + "\n7. Generar documento\n8. Consultar auditoría\n0. Salir");
                System.out.print("Opción: ");
                if (!entrada.hasNextLine()) {
                    continuar = false;
                } else {
                    String opcionTexto = entrada.nextLine().trim();
                    if (!opcionTexto.matches("[0-8]")) {
                        System.out.println("[ERROR] Ingrese una opción entre 0 y 8.");
                    } else {
                        int opcion = Integer.parseInt(opcionTexto);
                        if (opcion == 0) {
                            continuar = false;
                        } else {
                            String id = "";
                            String dato = "";
                            if (opcion != 1 && opcion != 3) {
                                System.out.print("Identificador (ejemplo: PAG-01, LOG-01, CON-01, FEL-01): ");
                                id = entrada.hasNextLine() ? entrada.nextLine() : null;
                            }
                            if (id != null && opcion >= 4 && opcion <= 7) {
                                System.out.print(opcion == 5 || opcion == 6
                                        ? "Monto en GTQ (ejemplo: 125.50): " : "Referencia (ejemplo: PED-001): ");
                                dato = entrada.hasNextLine() ? entrada.nextLine() : null;
                            }
                            if (id == null || dato == null) {
                                continuar = false;
                            } else {
                                System.out.println(ejecutarAccion(opcion, id, dato));
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Sesión finalizada. Gracias por utilizar el gestor.");
    }

    private String formatearCatalogo() {
        StringBuilder texto = new StringBuilder();
        texto.append(String.format("%-8s %-19s %-22s %-13s %10s%n",
                "ID", "NOMBRE", "TIPO", "CONEXIÓN", "LATENCIA"));
        texto.append("-".repeat(80)).append('\n');
        int cantidad = 0;
        for (IntegracionAPI api : controlador.listar()) {
            if (api.isActiva()) {
                texto.append(String.format(Locale.ROOT, "%-8s %-19s %-22s %-13s %7.1f ms%n",
                        api.getId(), api.getNombre(), api.getTipo(),
                        api.getEstadoConexion(), api.getLatenciaPromedioMs()));
                cantidad++;
            }
        }
        return texto.append("\nIntegraciones activas: ").append(cantidad)
                .append("\nActiva indica habilitación; una activa puede estar desconectada.").toString();
    }

    private String formatearConfiguracion(String id) {
        IntegracionAPI api = controlador.buscar(id);
        return api == null ? "[ERROR] No se encontró una API con ese identificador." : api.getConfiguracion();
    }

    private static BigDecimal convertirMonto(String texto) {
        if (texto == null || texto.trim().length() > 18 || !texto.trim().matches("\\d+(?:[.,]\\d{1,2})?")) {
            return null;
        }
        return new BigDecimal(texto.trim().replace(',', '.'));
    }

    private String ejecutarAccion(int opcion, String id, String dato) {
        if (opcion == 5 || opcion == 6) {
            BigDecimal monto = convertirMonto(dato);
            if (monto == null || monto.signum() <= 0) {
                return "[ERROR] Use un monto positivo de hasta 18 caracteres y máximo dos decimales, sin Q ni separadores de miles.";
            }
            return (opcion == 5 ? controlador.cobrar(id, monto) : controlador.reintegrar(id, monto)).toString();
        }
        return switch (opcion) {
            case 1 -> formatearCatalogo();
            case 2 -> formatearConfiguracion(id);
            case 3 -> {
                controlador.ordenarPorLatencia();
                yield "Catálogo ordenado de menor a mayor latencia.\n\n" + formatearCatalogo();
            }
            case 4 -> controlador.consultar(id, dato).toString();
            case 7 -> controlador.generarDocumento(id, dato).toString();
            case 8 -> controlador.auditar(id);
            default -> "[ERROR] Opción no válida.";
        };
    }

    private void ejecutarGrafica() {
        JFrame ventana = new JFrame("Gestor de integraciones | Ejercicio 6");
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel principal = new JPanel(new BorderLayout(16, 16));
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));
        principal.setBackground(new Color(240, 244, 248));

        JLabel titulo = new JLabel("GESTOR DE INTEGRACIONES  /  PROTOTIPO LOCAL");
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 21));
        titulo.setForeground(new Color(26, 55, 82));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel controles = new JPanel(new GridLayout(0, 1, 5, 8));
        controles.setOpaque(false);
        JComboBox<String> acciones = new JComboBox<>(new String[] {
                "1. Listar activas", "2. Buscar configuración", "3. Ordenar por latencia",
                "4. Consultar estado / cotización", "5. Cobrar", "6. Reintegrar",
                "7. Generar documento", "8. Consultar auditoría" });
        JTextField id = new JTextField("PAG-01");
        JLabel etiquetaDato = new JLabel("Monto en GTQ o referencia");
        JTextField dato = new JTextField("100.00");
        JButton ejecutar = new JButton("Ejecutar operación");
        JLabel ayuda = new JLabel("Seleccione una operación.");
        controles.add(new JLabel("Operación"));
        controles.add(acciones);
        controles.add(new JLabel("Identificador de la API"));
        controles.add(id);
        controles.add(etiquetaDato);
        controles.add(dato);
        controles.add(ejecutar);
        controles.add(ayuda);
        principal.add(controles, BorderLayout.WEST);

        JTextArea salida = new JTextArea(formatearCatalogo(), 22, 82);
        salida.setEditable(false);
        salida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        salida.setMargin(new java.awt.Insets(12, 12, 12, 12));
        principal.add(new JScrollPane(salida), BorderLayout.CENTER);
        JLabel pie = new JLabel("Simulación sin red | GTQ | XML sin validez fiscal | Datos temporales de sesión");
        pie.setForeground(new Color(65, 80, 95));
        principal.add(pie, BorderLayout.SOUTH);

        acciones.addActionListener(evento -> {
            int opcion = acciones.getSelectedIndex() + 1;
            id.setEnabled(opcion != 1 && opcion != 3);
            dato.setEnabled(opcion >= 4 && opcion <= 7);
            boolean pago = opcion == 5 || opcion == 6;
            etiquetaDato.setText(pago ? "Monto en GTQ (ejemplo: 100.00)" : "Referencia (ejemplo: PED-001)");
            dato.setText(pago ? "100.00" : "PED-001");
            ayuda.setText(opcion == 6 ? "LOG-01 permite probar IGNORADO." : "IDs: PAG-01 / LOG-01 / FEL-01");
        });
        id.setEnabled(false);
        dato.setEnabled(false);
        ejecutar.addActionListener(evento -> {
            salida.setText(ejecutarAccion(acciones.getSelectedIndex() + 1, id.getText(), dato.getText()));
            salida.setCaretPosition(0);
        });
        ventana.setContentPane(principal);
        ventana.pack();
        ventana.setMinimumSize(new java.awt.Dimension(960, 600));
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
}

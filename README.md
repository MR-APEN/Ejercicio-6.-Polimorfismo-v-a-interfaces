# Ejercicio 6 - Polimorfismo vía interfaces

Prototipo de gestión de integraciones de comercio electrónico en **Java 17**, con
arquitectura MVC, una única lista polimórfica y los contratos `Transaccional`,
`Documentable` y `Auditable`. Incluye consola obligatoria y GUI Swing opcional.

## Requisitos y ejecución en Windows

Necesitas un **JDK 17 o superior**, no solo un JRE. Comprueba `java -version` y
`javac -version`. No necesitas Maven, Gradle, base de datos, conexión a Internet
ni bibliotecas adicionales. Extrae todo el ZIP antes de ejecutarlo.

En PowerShell, abre la carpeta `ejercicio6` y ejecuta:

```powershell
# Consola: compila el código y ejecuta el programa.
.\ejecutar.bat

# Interfaz gráfica: mismo modelo y controlador.
.\ejecutar.bat --gui

# Compilación con advertencias y pruebas automáticas.
.\probar.bat
```
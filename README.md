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

## Estructura

| Ruta | Contenido |
|---|---|
| `src/modelo/` | Entidades, contratos, catálogo, motor y carga inicial |
| `src/controlador/` | Coordinación de casos de uso y auditoría |
| `src/vista/Principal.java` | Driver, menú de consola, Swing y toda la E/S |
| `test/pruebas/PruebasSistema.java` | Pruebas de comportamiento sin dependencias |
| `docs/Analisis_diseno.pdf` | Análisis, diseño, UML, trazabilidad y pruebas |
| `docs/UML.puml` | Diagrama de clases editable en PlantUML |
| `docs/DISENO.txt` | Decisiones fijadas antes de implementar |
| `docs/VERIFICACION.txt` | Resultados y límites de la verificación realizada |
| `fuentes.txt` | Rutas explícitas para compilar las 18 unidades Java de producción |

El ordenamiento modifica la misma lista; continúa vigente al volver a listar.
La búsqueda admite mayúsculas/minúsculas y espacios exteriores.
Los montos admiten punto o coma decimal, hasta dos decimales y sin separador de miles.
# Ge$tor Financiero (AppFinanzas)

Este proyecto es una aplicación en Java que permite registrar y consultar movimientos de dinero personales.

## Funcionalidad principal

- Registrar **ingresos** (por ejemplo: sueldo, ventas, etc.).
- Registrar **gastos** (por ejemplo: compras, servicios, etc.).
- Consultar la **caja libre / saldo disponible**.
- Guardar cada movimiento con:
  - nombre / concepto
  - monto
  - fecha
  - categoría (ingreso o gasto)

El sistema trabaja con clases como `Usuario`, `Ingreso` y `Gasto`, aplicando principios de Programación Orientada a Objetos

## Cómo ejecutar

### Opción 1: NetBeans
1. Clonar o descargar este repositorio.
2. Abrir NetBeans.
3. Menú: `File > Open Project...` y seleccionar la carpeta del proyecto.
4. Hacer clic en **Run Project** (o presionar `F6`).

NetBeans va a compilar automáticamente y generar las carpetas `build/` y `dist/` en tu máquina local.

### Opción 2: Línea de comandos (javac/java)
Si se quiere ejecutar sin NetBeans:
1. Entrar en `src/`
2. Compilar las clases:
   ```bash
   javac appfinanzas/*.java

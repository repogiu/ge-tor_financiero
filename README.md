# GeStor Financiero (AppFinanzas)

Aplicación en Java para registrar y consultar movimientos de dinero personales.

## Funcionalidad principal
- Registrar ingresos (sueldo, ventas, etc.).
- Registrar gastos (compras, servicios, etc.).
- Ver la caja libre / saldo disponible.
- Cada movimiento guarda: concepto, monto, fecha y tipo (ingreso/gasto).

Clases principales: `Usuario`, `Ingreso`, `Gasto`. Se aplica POO (encapsulamiento con atributos privados y getters/setters).

## Cómo ejecutar (Opción 1: NetBeans)
1. Clonar o descargar este repositorio.
2. Abrir NetBeans.
3. File > Open Project... y seleccionar la carpeta del proyecto.
4. Run Project (F6).

NetBeans compila solo y genera `build/` y `dist/` en la PC local.

## Cómo ejecutar (Opción 2: Consola)
1. Ir a la carpeta `src`
2. Compilar:
   ```bash
   javac appfinanzas/*.java



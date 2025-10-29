# Ge$tor Financiero (AppFinanzas)

Aplicación en Java para registrar y consultar movimientos de dinero personales.

## Funcionalidad principal
- Registrar ingresos (sueldo, ventas, etc.).
- Registrar gastos (compras, servicios, etc.).
- Ver la caja libre / saldo disponible.
- Cada movimiento guarda: concepto, monto, fecha y tipo (ingreso/gasto).

Clases principales: `Usuario`, `Ingreso`, `Gasto`. Se aplica POO (encapsulamiento con atributos privados y getters/setters).

## Requisitos
- JDK 8 o superior.
- Opcional: NetBeans 12+ si se prefiere usar un IDE.

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
   ```
3. Ejecutar la aplicación:
   ```bash
   java appfinanzas.AppFinanzas
   ```

## Uso
Al ejecutar `AppFinanzas`, se despliega un menú interactivo con las siguientes opciones:

1. **Caja libre / saldo disponible**: calcula el saldo a partir de todos los ingresos y gastos registrados.
2. **Presupuesto** *(en desarrollo)*: actualmente muestra un mensaje indicando que la funcionalidad está pendiente.
3. **Perfil financiero** *(en desarrollo)*: también informa que la sección se implementará en próximas versiones.
4. **Registrar ingreso**: solicita el concepto, monto y fecha. Además, pregunta si el ingreso es futuro para registrarlo correctamente.
5. **Registrar gasto**: solicita el concepto, monto y fecha, y pide confirmar si se trata de un gasto fijo o variable.
6. **Salir**: cierra la aplicación.

## Validaciones y flujo de captura
- Los montos se solicitan como números positivos; cualquier entrada inválida se vuelve a pedir.
- En ingresos se pregunta si el dinero se recibirá en el futuro, para distinguirlo de ingresos ya cobrados.
- En gastos se clasifica entre fijos y variables para futuras métricas.

## Estado de desarrollo
Las secciones de presupuesto y perfil financiero aún no cuentan con lógica implementada. El menú las muestra para dar visibilidad a las futuras funcionalidades que se incorporarán en la siguiente versión del proyecto.

## Estructura del proyecto
```
src/
└── appfinanzas/
    ├── AppFinanzas.java
    ├── Gasto.java
    ├── Ingreso.java
    └── Usuario.java
```

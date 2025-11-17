# Ge$tor Financiero (AppFinanzas)

Aplicación en Java para registrar y consultar movimientos de dinero personales.

## Funcionalidad principal
- Registrar ingresos (sueldo, ventas, etc.).
- Registrar gastos (compras, servicios, etc.).
- Ver la caja libre / saldo disponible.
- Cada movimiento guarda: concepto, monto, fecha y tipo (ingreso/gasto).
- Clases principales: Usuario, Ingreso, Gasto, Transaccion. Se aplica POO (encapsulamiento con atributos privados y getters/setters, herencia y métodos abstractos).

## Requisitos
- JDK 8 o superior.


## Cómo ejecutar

1. Clonar o descargar este repositorio.
2. Configurar la base de datos:
    - Ve a la carpeta `db/`.
    - Copia el archivo `db.properties.example` y renómbralo como `db.properties`.
    - Edita `db.properties` y completa tus datos de conexión
        - Antes de ejecutar la aplicación, importa el archivo `bd_finanzas.sql` en tu base de datos MySQL para crear la estructura y cargar datos de ejemplo.
3. Ir a la carpeta `src`
4. Ejecutar la aplicación:
    ```
    java appfinanzas.AppFinanzas
    ```

## Uso
Al ejecutar AppFinanzas, se despliega un menú interactivo con las siguientes opciones:

- **Caja libre / saldo disponible:** calcula el saldo a partir de todos los ingresos y gastos registrados.
- **Presupuesto:** muestra un mensaje con sugerencias de gasto diario/semanal según los ingresos y próximos cobros.
- **Perfil financiero:** muestra el estado del usuario según la proporción de gastos, deseos y ahorro, con recomendaciones y destinos sugeridos.
- **Registrar ingreso:** solicita el concepto, monto y fecha. Además, pregunta si el ingreso es futuro para registrarlo correctamente.
- **Registrar gasto:** solicita el concepto, monto y fecha, y pide confirmar si se trata de un gasto fijo o variable.
- **Salir:** cierra la aplicación.

## Validaciones y flujo de captura
- Los montos se solicitan como números positivos; cualquier entrada inválida se vuelve a pedir.
- En ingresos se pregunta si el dinero se recibirá en el futuro, para distinguirlo de ingresos ya cobrados.
- En gastos se clasifica entre fijos y variables para futuras métricas.


## Estructura del proyecto
```
src/
└── appfinanzas/
    ├── AppFinanzas.java
    ├── modelo/
    │   ├── Usuario.java
    │   ├── Ingreso.java
    │   ├── Gasto.java
    │   └── Transaccion.java
    ├── controlador/
    │   └── UsuarioController.java
    ├── vista/
    │   ├── ConsolaVistaUsuario.java
    │   └── VistaUsuario.java
    └── persistencia/
        ├── ConexionBD.java
        ├── IngresoDAO.java
        ├── GastoDAO.java
        └── UsuarioDAO.java
```

## Arquitectura y POO
- Se utiliza el patrón MVC (Modelo-Vista-Controlador).
- Las clases `Ingreso` y `Gasto` heredan de `Transaccion` (clase abstracta).
- El modelo encapsula los datos y la lógica de negocio.
- El controlador gestiona la interacción entre vista y modelo.
- La vista maneja la entrada/salida por consola.

## Persistencia
La persistencia de datos se realiza mediante DAOs conectados a MySQL, permitiendo que los ingresos, gastos y usuarios se guarden y consulten entre ejecuciones de la aplicación.


---

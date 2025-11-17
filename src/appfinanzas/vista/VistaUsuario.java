package appfinanzas.vista;

/**
 * Define las operaciones que debe ofrecer una vista para interactuar con un usuario.
 */
public interface VistaUsuario {

    void mostrarSaludoInicial();

    /** Solicita el DNI del usuario para iniciar o reingresar. */
    String solicitarDni();

    String solicitarNombreUsuario();

    void mostrarUsuarioRegistrado(String nombre, String dni);
    /** Muestra únicamente el encabezado de inicio de sesión para usuarios existentes. */
    void mostrarInicioSesion(String nombre);

    void mostrarMensaje(String mensaje);

    IngresoInput solicitarDatosIngreso();

    GastoInput solicitarDatosGasto();

    boolean preguntarContinuacion(String pregunta);

    int mostrarMenuYObtenerOpcion();

    void mostrarCajaLibre(double monto);

    void mostrarDespedida(String nombre);

    /** Solicita la fecha aproximada del próximo ingreso (dd/MM/yyyy). */
    java.time.LocalDate solicitarFechaProximoIngreso();

    /** Muestra una guía rápida para diferenciar gastos fijos, variables (necesidades) y deseos. */
    void mostrarGuiaGastos();

    // --- Menú principal de Ingresos ---
    /** Muestra el menú: 1) Listado 2) Nuevo ingreso 3) Volver */
    void mostrarMenuIngresos();
    /** Lee 1, 2 o 3 para el menú de ingresos. */
    int leerOpcionMenuIngresos();

    // --- Submenú de Ingresos ---
    /** Muestra el listado de ingresos. */
    void mostrarListadoIngresos(java.util.List<appfinanzas.modelo.Ingreso> ingresos);
    /** Lee una opción del listado: 'a' para volver o letra desde 'b' para seleccionar. */
    String leerOpcionListadoIngresos(int cantidad);
    /** Muestra el ingreso seleccionado y el submenú rápido de acción (a=Modificar, b=Eliminar, c=Volver). */
    void mostrarIngresoSeleccionado(appfinanzas.modelo.Ingreso ingreso);
    /** Devuelve 'a' (modificar), 'b' (eliminar) o 'c' (volver). */
    String leerOpcionAccionIngreso();
    /** Solicita datos para editar un ingreso permitiendo Enter para mantener valores actuales. */
    EditIngresoInput solicitarDatosEdicionIngreso(appfinanzas.modelo.Ingreso actual);
    /** Confirma eliminación del ingreso. */
    boolean confirmarEliminacionIngreso(String nombre);

    /** Datos de edición de ingreso (pueden ser iguales al original si se presiona Enter en todos). */
    class EditIngresoInput {
        private final String nombre;
        private final double monto;
        private final java.time.LocalDate fecha;
        private final boolean esFuturo;

        public EditIngresoInput(String nombre, double monto, java.time.LocalDate fecha, boolean esFuturo) {
            this.nombre = nombre;
            this.monto = monto;
            this.fecha = fecha;
            this.esFuturo = esFuturo;
        }

        public String getNombre() { return nombre; }
        public double getMonto() { return monto; }
        public java.time.LocalDate getFecha() { return fecha; }
        public boolean esFuturo() { return esFuturo; }
    }

    // --- Menú principal de Gastos ---
    /** Muestra el menú: 1) Listado 2) Nuevo gasto 3) Volver */
    void mostrarMenuGastos();
    /** Lee 1, 2 o 3 para el menú de gastos. */
    int leerOpcionMenuGastos();

    // --- Submenú de Gastos ---
    /** Muestra el listado de gastos. */
    void mostrarListadoGastos(java.util.List<appfinanzas.modelo.Gasto> gastos);
    /** Lee una opción del listado: 'a' para volver o letra desde 'b' para seleccionar. */
    String leerOpcionListadoGastos(int cantidad);
    /** Muestra el gasto seleccionado y el submenú rápido de acción (a=Modificar, b=Eliminar, c=Volver). */
    void mostrarGastoSeleccionado(appfinanzas.modelo.Gasto gasto);
    /** Devuelve 'a' (modificar), 'b' (eliminar) o 'c' (volver). */
    String leerOpcionAccionGasto();
    /** Solicita datos para editar un gasto permitiendo Enter para mantener valores actuales. */
    EditGastoInput solicitarDatosEdicionGasto(appfinanzas.modelo.Gasto actual);
    /** Confirma eliminación del gasto. */
    boolean confirmarEliminacionGasto(String nombre);

    /** Datos de edición de gasto. */
    class EditGastoInput {
        private final String nombre;
        private final double monto;
        private final boolean esFijo;

        public EditGastoInput(String nombre, double monto, boolean esFijo) {
            this.nombre = nombre;
            this.monto = monto;
            this.esFijo = esFijo;
        }

        public String getNombre() { return nombre; }
        public double getMonto() { return monto; }
        public boolean esFijo() { return esFijo; }
    }

    /**
     * Estructura de datos para capturar la información necesaria para crear un ingreso.
     */
    class IngresoInput {
        private final String nombre;
        private final double monto;
        private final java.time.LocalDate fecha;
        private final boolean esFuturo;

        public IngresoInput(String nombre, double monto, java.time.LocalDate fecha, boolean esFuturo) {
            this.nombre = nombre;
            this.monto = monto;
            this.fecha = fecha;
            this.esFuturo = esFuturo;
        }

        public String getNombre() {
            return nombre;
        }

        public double getMonto() {
            return monto;
        }

        public java.time.LocalDate getFecha() {
            return fecha;
        }

        public boolean esFuturo() {
            return esFuturo;
        }
    }

    /**
     * Estructura de datos para capturar la información necesaria para crear un gasto.
     */
    class GastoInput {
        private final String nombre;
        private final double monto;
        private final boolean esFijo;

        public GastoInput(String nombre, double monto, boolean esFijo) {
            this.nombre = nombre;
            this.monto = monto;
            this.esFijo = esFijo;
        }

        public String getNombre() {
            return nombre;
        }

        public double getMonto() {
            return monto;
        }

        public boolean esFijo() {
            return esFijo;
        }
    }
}

package appfinanzas.vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

/**
 * Implementación de {@link VistaUsuario} que interactúa a través de la consola.
 */
public class ConsolaVistaUsuario implements VistaUsuario {

    private final Scanner scanner;
    private final DateTimeFormatter formatoFecha;

    public ConsolaVistaUsuario(Scanner scanner) {
        this.scanner = scanner;
        this.formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    @Override
    public void mostrarSaludoInicial() {
        System.out.println();
        System.out.println("¡Hola! Te damos la bienvenida a tu Ge$tor Financiero. Empecemos a ordenar tus finanzas.");
    }

    @Override
    public String solicitarDni() {
        System.out.print("Ingresa tu DNI (sólo números, sin puntos): ");
        String dni = scanner.nextLine().trim();
        while (true) {
            if (dni.isEmpty()) {
                System.out.print("El DNI no puede estar vacío. Inténtalo nuevamente: ");
            } else if (!dni.matches("\\d{6,15}")) {
                System.out.print("Formato inválido. Debe contener entre 6 y 15 dígitos: ");
            } else {
                return dni;
            }
            dni = scanner.nextLine().trim();
        }
    }

    @Override
    public String solicitarNombreUsuario() {
        System.out.print("Antes de comenzar, ingresa tu nombre: ");
        String nombre = scanner.nextLine().trim();
        while (nombre.isEmpty()) {
            System.out.print("El nombre no puede estar vacío. Ingrésalo nuevamente: ");
            nombre = scanner.nextLine().trim();
        }
        return nombre;
    }

    @Override
    public void mostrarUsuarioRegistrado(String nombre, String dni) {
        System.out.println("¡Listo! Usuario " + nombre + " registrado con DNI: " + dni);
        System.out.println();
        System.out.println("===== Inicio de sesión: " + nombre + " =====");
        System.out.println();
    }

    @Override
    public void mostrarInicioSesion(String nombre) {
        System.out.println();
        System.out.println("===== Inicio de sesión: " + nombre + " =====");
        System.out.println();
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println();
        System.out.println(mensaje);
    }

    @Override
    public IngresoInput solicitarDatosIngreso() {
        System.out.println();
        System.out.println("--- Registrar Ingreso ---");
        System.out.println();
        while (true) {
            try {
                System.out.print("Nombre del ingreso: ");
                String nombre = leerLineaNoVacia();
                System.out.print("Monto: ");
                double monto = leerMonto();

                System.out.print("Fecha ingreso (dd/MM/yyyy): ");
                LocalDate fecha = leerFecha();

                boolean esFuturo = leerRespuestaSiNo("¿Es un ingreso futuro? (sí/no): ");

                return new IngresoInput(nombre, monto, fecha, esFuturo);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public GastoInput solicitarDatosGasto() {
        System.out.println();
        System.out.println("--- Registrar Gasto ---");
        System.out.println();
        while (true) {
            try {
                System.out.print("Nombre del gasto: ");
                String nombre = leerLineaNoVacia();
                System.out.print("Monto: ");
                double monto = leerMonto();

                boolean esFijo = leerRespuestaTipoGasto();

                return new GastoInput(nombre, monto, esFijo);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean preguntarContinuacion(String pregunta) {
        return leerRespuestaSiNo(pregunta);
    }

    @Override
    public int mostrarMenuYObtenerOpcion() {
        while (true) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1) Ver Caja Libre");
            System.out.println("2) Ver Presupuesto Financiero");
            System.out.println("3) Ver Perfil Financiero");
            System.out.println("4) Ver/gestionar Ingresos");
            System.out.println("5) Ver/gestionar Gastos");
            System.out.println("6) Salir");
            System.out.print("Seleccioná una opción: ");

            String linea = scanner.nextLine();
            try {
                int opcion = Integer.parseInt(linea.trim());
                if (opcion >= 1 && opcion <= 6) {
                    return opcion;
                }
                System.out.println("Opción inválida. Ingresá un número del 1 al 6.");
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar un número válido.");
            }
        }
    }

    @Override
    public void mostrarCajaLibre(double monto) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        System.out.println("Tu caja libre actual es: " + nf.format(monto));
    }

    @Override
    public void mostrarDespedida(String nombre) {
        System.out.println("¡Gracias por usar tu app financiera, " + nombre + "!");
    }

    @Override
    public java.time.LocalDate solicitarFechaProximoIngreso() {
        System.out.print("Ingresá la fecha de tu próximo ingreso (dd/MM/yyyy): ");
        return leerFecha();
    }
    
    @Override
    public void mostrarGuiaGastos() {
        // Formato solicitado tipo tabla sin borde superior/inferior, solo líneas con '|'
        java.util.List<String> lineas = new java.util.ArrayList<>();
        lineas.add("GUIA PARA REGISTRAR GASTOS");
        lineas.add("Gastos fijos: Pagos que deben hacerse sí o sí, todos los meses y en fechas específicas.");
        lineas.add("  Ejemplos: alquiler, expensas, servicios (luz, agua, gas), cuotas colegio/universidad, prepaga.");
        lineas.add("Gastos variables: Compras necesarias sin monto ni fecha fija.");
        lineas.add("  Ejemplos: comida diaria, transporte, farmacia, higiene.");
        lineas.add("Deseos: gastos que no son necesarios pero mejoran calidad de vida. NO se cargan como gasto aquí.");
        lineas.add("  La app te sugerirá cuánto destinar según tu perfil financiero.");
        lineas.add("  Ejemplos: vestimenta, delivery, ocio (salidas, bares, restaurantes, kiosco), viajes/vacaciones,");
        lineas.add("            cuota de gimnasio o actividades deportivas, streaming extra.");

        int maxLen = 0;
        for (String ln : lineas) if (ln.length() > maxLen) maxLen = ln.length();

        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (String ln : lineas) {
            int diff = maxLen - ln.length();
            sb.append('|').append(' ');
            sb.append(ln);
            for (int k = 0; k < diff; k++) sb.append(' ');
            sb.append(' ').append('|').append('\n');
        }
        System.out.print(sb.toString());
    }

    // ========== Menú de Ingresos (1/2/3) ==========
    @Override
    public void mostrarMenuIngresos() {
        System.out.println();
        System.out.println("--- Ingresos ---");
        System.out.println("1) Ver listado de ingresos");
        System.out.println("2) Agregar nuevo ingreso");
        System.out.println("3) Volver al menú principal");
    }

    @Override
    public int leerOpcionMenuIngresos() {
        while (true) {
            System.out.print("Elegí una opción (1/2/3): ");
            String linea = scanner.nextLine().trim();
            try {
                int op = Integer.parseInt(linea);
                if (op >= 1 && op <= 3) return op;
            } catch (NumberFormatException ignored) {}
            System.out.println("❌ Opción inválida. Ingresá 1, 2 o 3.");
        }
    }

    // ========== Submenú de Ingresos ==========
    @Override
    public void mostrarListadoIngresos(java.util.List<appfinanzas.modelo.Ingreso> ingresos) {
        System.out.println();
        System.out.println("--- Listado de Ingresos ---");
        System.out.println("a) Volver");
        if (ingresos == null || ingresos.isEmpty()) {
            System.out.println("(no hay ingresos registrados)");
        } else {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            char letra = 'b';
            for (appfinanzas.modelo.Ingreso i : ingresos) {
                String linea = String.format("%c) %s | %s | %s | %s",
                        letra,
                        i.getFecha().format(fmt),
                        i.getNombre(),
                        nf.format(i.getMonto()),
                        i.isEsFuturo() ? "Futuro" : "Disponible");
                System.out.println(linea);
                letra++;
            }
        }
    }

    // ========== Menú de Gastos (0/1/2/3) ==========
    @Override
    public void mostrarMenuGastos() {
        System.out.println();
        System.out.println("--- Gastos ---");
        System.out.println("0) Ver guía de gastos");
        System.out.println("1) Ver listado de gastos");
        System.out.println("2) Agregar nuevo gasto");
        System.out.println("3) Volver al menú principal");
    }

    @Override
    public int leerOpcionMenuGastos() {
        while (true) {
            System.out.print("Elegí una opción (0/1/2/3): ");
            String linea = scanner.nextLine().trim();
            try {
                int op = Integer.parseInt(linea);
                if (op >= 0 && op <= 3) return op;
            } catch (NumberFormatException ignored) {}
            System.out.println("❌ Opción inválida. Ingresá 0, 1, 2 o 3.");
        }
    }

    // ========== Submenú de Gastos ==========
    @Override
    public void mostrarListadoGastos(java.util.List<appfinanzas.modelo.Gasto> gastos) {
        System.out.println();
        System.out.println("--- Listado de Gastos ---");
        System.out.println("a) Volver");
        if (gastos == null || gastos.isEmpty()) {
            System.out.println("(no hay gastos registrados)");
        } else {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            char letra = 'b';
            for (appfinanzas.modelo.Gasto g : gastos) {
                String linea = String.format("%c) %s | %s | %s | %s",
                        letra,
                        g.getFecha().format(fmt),
                        g.getNombre(),
                        nf.format(g.getMonto()),
                        g.isEsFijo() ? "Fijo" : "Variable");
                System.out.println(linea);
                letra++;
            }
        }
    }

    @Override
    public String leerOpcionListadoGastos(int cantidad) {
        while (true) {
            System.out.print("Seleccioná un gasto para modificar o eliminar: ");
            String op = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (op.equals("a")) return op; // volver
            if (op.length() == 1) {
                int idx = op.charAt(0) - 'b'; // 'b' -> 0
                if (idx >= 0 && idx < cantidad) return op;
            }
            System.out.println("❌ Opción inválida. Usá 'a' para volver o una letra desde 'b'.");
        }
    }

    @Override
    public void mostrarGastoSeleccionado(appfinanzas.modelo.Gasto gasto) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println();
        System.out.println("Gasto seleccionado: " + gasto.getNombre() + " | " + nf.format(gasto.getMonto()) + " | " + gasto.getFecha().format(fmt));
        System.out.println("a) Modificar");
        System.out.println("b) Eliminar");
        System.out.println("c) Volver");
    }

    @Override
    public String leerOpcionAccionGasto() {
        while (true) {
            System.out.print("Opción: ");
            String op = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (op.equals("a") || op.equals("b") || op.equals("c")) return op;
            System.out.println("❌ Opción inválida. Ingresá 'a', 'b' o 'c'.");
        }
    }

    @Override
    public EditGastoInput solicitarDatosEdicionGasto(appfinanzas.modelo.Gasto actual) {
        System.out.println();
        System.out.println("Modificar (Enter para mantener el valor actual)");
        System.out.print("Nombre [" + actual.getNombre() + "]: ");
        String nombre = leerLineaConDefault(actual.getNombre());

        System.out.print("Monto [" + String.format(Locale.forLanguageTag("es-AR"), "%.0f", actual.getMonto()) + "]: ");
        double monto = leerMontoConDefault(actual.getMonto());

        boolean esFijo = leerRespuestaTipoGastoConDefault(actual.isEsFijo());
        return new EditGastoInput(nombre, monto, esFijo);
    }

    @Override
    public boolean confirmarEliminacionGasto(String nombre) {
        return leerRespuestaSiNo("¿Seguro que querés eliminar \"" + nombre + "\"? (sí/no): ");
    }

    @Override
    public String leerOpcionListadoIngresos(int cantidad) {
        while (true) {
            System.out.print("Seleccioná un ingreso para modificar o eliminar: ");
            String op = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (op.equals("a")) return op; // volver
            if (op.length() == 1) {
                int idx = op.charAt(0) - 'b'; // 'b' -> 0, 'c' -> 1, ...
                if (idx >= 0 && idx < cantidad) return op;
            }
            System.out.println("❌ Opción inválida. Usá 'a' para volver o una letra desde 'b'.");
        }
    }

    @Override
    public void mostrarIngresoSeleccionado(appfinanzas.modelo.Ingreso ingreso) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println();
        System.out.println("Ingreso seleccionado: " + ingreso.getNombre() + " | " + nf.format(ingreso.getMonto()) + " | " + ingreso.getFecha().format(fmt));
        System.out.println("a) Modificar");
        System.out.println("b) Eliminar");
        System.out.println("c) Volver");
    }

    @Override
    public String leerOpcionAccionIngreso() {
        while (true) {
            System.out.print("Opción: ");
            String op = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (op.equals("a") || op.equals("b") || op.equals("c")) return op;
            System.out.println("❌ Opción inválida. Ingresá 'a', 'b' o 'c'.");
        }
    }

    @Override
    public EditIngresoInput solicitarDatosEdicionIngreso(appfinanzas.modelo.Ingreso actual) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println();
        System.out.println("Modificar (Enter para mantener el valor actual)");
        System.out.print("Nombre [" + actual.getNombre() + "]: ");
        String nombre = leerLineaConDefault(actual.getNombre());

        System.out.print("Monto [" + String.format(Locale.forLanguageTag("es-AR"), "%.0f", actual.getMonto()) + "]: ");
        double monto = leerMontoConDefault(actual.getMonto());

        System.out.print("Fecha (dd/MM/yyyy) [" + actual.getFecha().format(fmt) + "]: ");
        java.time.LocalDate fecha = leerFechaConDefault(actual.getFecha());

        String def = actual.isEsFuturo() ? "sí" : "no";
        System.out.print("¿Futuro? (sí/no) [" + def + "]: ");
        boolean esFuturo = leerSiNoConDefault(actual.isEsFuturo());

        return new EditIngresoInput(nombre, monto, fecha, esFuturo);
    }

    @Override
    public boolean confirmarEliminacionIngreso(String nombre) {
        return leerRespuestaSiNo("¿Seguro que querés eliminar \"" + nombre + "\"? (sí/no): ");
    }

    private String leerLineaNoVacia() {
        String linea = scanner.nextLine().trim();
        while (true) {
            if (linea.isEmpty()) {
                System.out.print("Este campo no puede estar vacío. Inténtalo nuevamente: ");
            } else if (linea.matches("\\d+")) { // solo dígitos
                System.out.print("El nombre no puede ser solo números. Escribe algo más descriptivo: ");
            } else {
                return linea;
            }
            linea = scanner.nextLine().trim();
        }
    }

    private String leerLineaConDefault(String actual) {
        String linea = scanner.nextLine();
        linea = linea.trim();
        if (linea.isEmpty()) return actual;
        if (linea.matches("\\d+")) {
            // Evitar nombres puramente numéricos
            System.out.print("El nombre no puede ser solo números. Mantengo valor actual.\n");
            return actual;
        }
        return linea;
    }

    private double leerMonto() {
        while (true) {
            String linea = scanner.nextLine().trim();
            try {
                double monto = Double.parseDouble(linea);
                if (monto <= 0) {
                    System.out.print("El monto debe ser mayor a 0. Inténtalo nuevamente: ");
                    continue;
                }
                return monto; // La vista vuelve a validar monto >= 0 para simplificar el flujo
            } catch (NumberFormatException e) {
                System.out.print("Monto inválido. Ingresá un valor numérico: ");
            }
        }
    }

    private double leerMontoConDefault(double actual) {
        String linea = scanner.nextLine().trim();
        if (linea.isEmpty()) return actual;
        try {
            double monto = Double.parseDouble(linea);
            if (monto <= 0) {
                System.out.println("El monto debe ser mayor a 0. Mantengo valor actual.");
                return actual;
            }
            return monto;
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido. Mantengo valor actual.");
            return actual;
        }
    }

    private LocalDate leerFecha() {
        while (true) {
            String linea = scanner.nextLine().trim();
            try {
                return LocalDate.parse(linea, formatoFecha);
            } catch (DateTimeParseException e) {
                System.out.print("Fecha inválida. Usá el formato dd/MM/yyyy: ");
            }
        }
    }

    private LocalDate leerFechaConDefault(LocalDate actual) {
        String linea = scanner.nextLine().trim();
        if (linea.isEmpty()) return actual;
        try {
            return LocalDate.parse(linea, formatoFecha);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha inválida. Mantengo valor actual.");
            return actual;
        }
    }

    private boolean leerRespuestaSiNo(String pregunta) {
        while (true) {
            System.out.print(pregunta);
            String respuesta = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (respuesta.equals("sí") || respuesta.equals("si")) {
                return true;
            } else if (respuesta.equals("no")) {
                return false;
            }
            System.out.println("❌ Respuesta no válida. Escribí exactamente 'sí' o 'no'.");
        }
    }

    private boolean leerRespuestaTipoGasto() {
        while (true) {
            System.out.print("¿Es un gasto fijo o variable? (fijo/variable): ");
            String respuesta = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (respuesta.equals("fijo")) {
                return true;
            } else if (respuesta.equals("variable")) {
                return false;
            }
            System.out.println("❌ Respuesta no válida. Escribí exactamente 'fijo' o 'variable'.");
        }
    }

    private boolean leerRespuestaTipoGastoConDefault(boolean actual) {
        while (true) {
            System.out.print("¿Tipo? (fijo/variable) [" + (actual ? "fijo" : "variable") + "]: ");
            String respuesta = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (respuesta.isEmpty()) return actual;
            if (respuesta.equals("fijo")) return true;
            if (respuesta.equals("variable")) return false;
            System.out.println("❌ Respuesta no válida. Ingresá 'fijo' o 'variable'.");
        }
    }

    private boolean leerSiNoConDefault(boolean actual) {
        String linea = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        if (linea.isEmpty()) return actual;
        if (linea.equals("sí") || linea.equals("si")) return true;
        if (linea.equals("no")) return false;
        System.out.println("Respuesta inválida. Mantengo valor actual.");
        return actual;
    }

    // Utilidad local para repetir caracteres usada en cuadros ASCII.
    private static String repeatChar(char c, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }
}

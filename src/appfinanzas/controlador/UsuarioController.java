package appfinanzas.controlador;

import appfinanzas.modelo.Gasto;
import appfinanzas.modelo.Ingreso;
import appfinanzas.modelo.Usuario;
import appfinanzas.persistencia.GastoDAO;
import appfinanzas.persistencia.IngresoDAO;
import appfinanzas.persistencia.UsuarioDAO;
import appfinanzas.vista.VistaUsuario;

import java.sql.SQLException;

/**
 * Controla el flujo de la aplicación coordinando la vista y el modelo {@link Usuario}.
 */
public class UsuarioController {

    private final VistaUsuario vista;
    private Usuario usuario;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final IngresoDAO ingresoDAO = new IngresoDAO();
    private final GastoDAO gastoDAO = new GastoDAO();

    public UsuarioController(VistaUsuario vista) {
        this.vista = vista;
    }

    public void iniciar() {
        vista.mostrarSaludoInicial();
        // Flujo por DNI: se pide primero.
        String dni = vista.solicitarDni();
        boolean esNuevo = false;
        try {
            Usuario existente = usuarioDAO.buscarPorDni(dni);
            if (existente != null) {
                // Cargar ingresos y gastos desde la BD
                for (Ingreso i : ingresoDAO.listarPorDni(dni)) {
                    existente.agregarIngreso(i);
                }
                for (Gasto g : gastoDAO.listarPorDni(dni)) {
                    existente.agregarGasto(g);
                }
                usuario = existente;
            } else {
                String nombre = vista.solicitarNombreUsuario();
                usuario = new Usuario(nombre, dni);
                usuarioDAO.guardar(usuario);
                esNuevo = true;
            }
            if (esNuevo) {
                vista.mostrarUsuarioRegistrado(usuario.getNombre(), usuario.getDni());
            } else {
                vista.mostrarInicioSesion(usuario.getNombre());
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error accediendo a la base de datos: " + e.getMessage());
            return;
        }
        if (esNuevo) {
            vista.mostrarMensaje("Ahora vamos a registrar tus ingresos.");
            registrarIngresos();
            vista.mostrarMensaje("¡Genial! Ahora continuamos registrando tus gastos.");
            // Mostrar guía para evitar que se confundan deseos con gastos
            vista.mostrarGuiaGastos();
            registrarGastos();
        }
        ejecutarMenu();
    }

    private void registrarIngresos() {
        boolean continuar;
        do {
            VistaUsuario.IngresoInput datos = vista.solicitarDatosIngreso();
            Ingreso ingreso = new Ingreso(
                    datos.getNombre(),
                    datos.getMonto(),
                    datos.getFecha(),
                    datos.esFuturo()
            );
            usuario.agregarIngreso(ingreso);
            try {
                ingresoDAO.guardar(usuario.getDni(), ingreso);
            } catch (SQLException e) {
                vista.mostrarMensaje("No se pudo guardar el ingreso en la base: " + e.getMessage());
            }
            vista.mostrarMensaje("Ingreso registrado correctamente:\n" + ingreso);
            // Mostrar resumen rápido después de cada registro
            mostrarResumen();
            continuar = vista.preguntarContinuacion("¿Querés registrar otro ingreso? (sí/no): ");
        } while (continuar);
    }

    private void registrarGastos() {
        boolean continuar;
        do {
            VistaUsuario.GastoInput datos = vista.solicitarDatosGasto();
            Gasto gasto = new Gasto(
                    datos.getMonto(),
                    datos.getNombre(),
                    datos.esFijo()
            );
            usuario.agregarGasto(gasto);
            try {
                gastoDAO.guardar(usuario.getDni(), gasto);
            } catch (SQLException e) {
                vista.mostrarMensaje("No se pudo guardar el gasto en la base: " + e.getMessage());
            }
            vista.mostrarMensaje("Gasto registrado correctamente: " + gasto);
            // Mostrar resumen rápido después de cada registro
            mostrarResumen();
            continuar = vista.preguntarContinuacion("¿Querés registrar otro gasto? (sí/no): ");
        } while (continuar);
    }

    private void mostrarResumen() {

        double totalIngresos = 0.0;
        double totalGastos = 0.0;
        for (Ingreso i : usuario.getIngresos()) {
            if (i.esDisponible()) totalIngresos += i.getMonto();
        }
        for (Gasto g : usuario.getGastos()) {
            totalGastos += g.getMonto();
        }
        double cajaLibre = totalIngresos - totalGastos;

        java.text.NumberFormat nf = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("es-AR"));
        String resumen = "Ingresos totales: " + nf.format(totalIngresos) +
            " | Gastos totales: " + nf.format(totalGastos);
        vista.mostrarMensaje(resumen);

        // Aviso suave si aún no hay gastos cargados
        if (usuario.getGastos().isEmpty()) {
            vista.mostrarMensaje("Aún no registraste gastos. Cargalos para un resumen más realista.");
            // Mostrar sólo el saldo actual, sin sugerencia de uso cuando no hay gastos
            String saldoCorto = "Tu saldo disponible actual es de " + nf.format(cajaLibre) + ".";
            vista.mostrarMensaje(saldoCorto);
            return; // no mostramos el texto "Podés destinar..." si no hay gastos
        }

        // Buscar próximo ingreso futuro (si existe) para enriquecer el mensaje
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate proximaFecha = null;
        for (Ingreso i : usuario.getIngresos()) {
            // Consideramos "futuro" a todo ingreso con fecha hoy o posterior que aún no está disponible
            if (!i.esDisponible() && (i.getFecha().isAfter(hoy) || i.getFecha().isEqual(hoy))) {
                if (proximaFecha == null || i.getFecha().isBefore(proximaFecha)) {
                    proximaFecha = i.getFecha();
                }
            }
        }
        StringBuilder detalle = new StringBuilder();
        if (cajaLibre <= 0) {
            String faltante = nf.format(Math.abs(cajaLibre));
            detalle.append("Tenés un faltante de ").append(faltante)
                    .append("; evitá gastos discrecionales");
            if (proximaFecha != null) {
                String fechaStr = proximaFecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                detalle.append(" hasta tu próximo ingreso del ").append(fechaStr).append(".");
            } else {
                detalle.append(" hasta que recibas nuevos ingresos.");
            }
        } else {
            String montoFormateado = nf.format(cajaLibre);
            detalle.append("Tu saldo disponible actual es de ").append(montoFormateado)
                    .append(". Podés destinar este monto a deseos o ahorro");
            if (proximaFecha != null) {
                String fechaStr = proximaFecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                detalle.append(" hasta tu próximo ingreso del ").append(fechaStr).append(".");
            } else {
                detalle.append(" hasta que recibas nuevos ingresos.");
            }
        }

        vista.mostrarMensaje(detalle.toString());
    }

    private void ejecutarMenu() {
        boolean seguir = true;
        while (seguir) {
            int opcion = vista.mostrarMenuYObtenerOpcion();
            switch (opcion) {
                case 1:
                    mostrarResumen();
                    break;
                case 2:
                    {
                        // Detectar próximo ingreso; si no hay, solicitar fecha para calcular con precisión
                        java.time.LocalDate hoy = java.time.LocalDate.now();
                        java.time.LocalDate proximaFecha = null;
                        for (Ingreso i : usuario.getIngresos()) {
                            if (!i.esDisponible() && (i.getFecha().isAfter(hoy) || i.getFecha().isEqual(hoy))) {
                                if (proximaFecha == null || i.getFecha().isBefore(proximaFecha)) {
                                    proximaFecha = i.getFecha();
                                }
                            }
                        }

                        if (proximaFecha == null) {
                            java.time.LocalDate fecha = vista.solicitarFechaProximoIngreso();
                            vista.mostrarMensaje(usuario.obtenerMensajePresupuesto(fecha));
                        } else {
                            vista.mostrarMensaje(usuario.obtenerMensajePresupuesto());
                        }
                    }
                    break;
                case 3:
                    vista.mostrarMensaje(usuario.obtenerMensajePerfilFinanciero());
                    break;
                case 4:
                    submenuIngresos();
                    break;
                case 5:
                    submenuGastos();
                    break;
                case 6:
                    vista.mostrarDespedida(usuario.getNombre());
                    seguir = false;
                    break;
                default:
                    vista.mostrarMensaje("Opción inválida.");
            }
        }
    }

    private void submenuIngresos() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenuIngresos();
            int op = vista.leerOpcionMenuIngresos();
            switch (op) {
                case 1: { // Ver listado y seleccionar
                    java.util.List<Ingreso> ingresos;
                    try {
                        ingresos = ingresoDAO.listarPorDni(usuario.getDni());
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al listar ingresos: " + e.getMessage());
                        break;
                    }
                    if (ingresos.isEmpty()) {
                        vista.mostrarListadoIngresos(ingresos);
                        break; // nada para seleccionar
                    }
                    vista.mostrarListadoIngresos(ingresos);
                    String sel = vista.leerOpcionListadoIngresos(ingresos.size());
                    if (sel.equals("a")) break; // volver al menú de ingresos
                    int idx = sel.charAt(0) - 'b';
                    if (idx < 0 || idx >= ingresos.size()) {
                        vista.mostrarMensaje("Selección inválida.");
                        break;
                    }
                    Ingreso seleccionado = ingresos.get(idx);
                    vista.mostrarIngresoSeleccionado(seleccionado);
                    String act = vista.leerOpcionAccionIngreso();
                    if (act.equals("a")) {
                        VistaUsuario.EditIngresoInput edit = vista.solicitarDatosEdicionIngreso(seleccionado);
                        Ingreso actualizado = new Ingreso(
                                seleccionado.getId(),
                                edit.getNombre(),
                                edit.getMonto(),
                                edit.getFecha(),
                                edit.esFuturo()
                        );
                        try {
                            ingresoDAO.actualizar(actualizado);
                            recargarUsuarioDesdeBD();
                            vista.mostrarMensaje("Ingreso actualizado.");
                        } catch (SQLException e) {
                            vista.mostrarMensaje("No se pudo actualizar: " + e.getMessage());
                        }
                    } else if (act.equals("b")) {
                        boolean ok = vista.confirmarEliminacionIngreso(seleccionado.getNombre());
                        if (ok) {
                            try {
                                ingresoDAO.eliminar(seleccionado.getId());
                                recargarUsuarioDesdeBD();
                                vista.mostrarMensaje("Ingreso eliminado.");
                            } catch (SQLException e) {
                                vista.mostrarMensaje("No se pudo eliminar: " + e.getMessage());
                            }
                        }
                    } // 'c' vuelve
                    break;
                }
                case 2: { // Nuevo ingreso
                    VistaUsuario.IngresoInput datos = vista.solicitarDatosIngreso();
                    Ingreso nuevo = new Ingreso(datos.getNombre(), datos.getMonto(), datos.getFecha(), datos.esFuturo());
                    try {
                        ingresoDAO.guardar(usuario.getDni(), nuevo);
                        recargarUsuarioDesdeBD();
                        vista.mostrarMensaje("Ingreso agregado.");
                    } catch (SQLException e) {
                        vista.mostrarMensaje("No se pudo guardar el ingreso: " + e.getMessage());
                    }
                    break;
                }
                case 3:
                    salir = true; // volver al menú principal
                    break;
                default:
                    // no debería ocurrir
                    break;
            }
        }
    }

    private void recargarUsuarioDesdeBD() {
        try {
            java.util.List<Ingreso> ingresos = ingresoDAO.listarPorDni(usuario.getDni());
            java.util.List<Gasto> gastos = gastoDAO.listarPorDni(usuario.getDni());
            Usuario nuevo = new Usuario(usuario.getNombre(), usuario.getDni());
            for (Ingreso i : ingresos) nuevo.agregarIngreso(i);
            for (Gasto g : gastos) nuevo.agregarGasto(g);
            this.usuario = nuevo;
        } catch (SQLException e) {
            // Si falla la recarga, dejamos el usuario como estaba y mostramos mensaje
            vista.mostrarMensaje("No se pudo recargar el usuario: " + e.getMessage());
        }
    }

    private void submenuGastos() {
        boolean salir = false;
        while (!salir) {
            vista.mostrarMenuGastos();
            int op = vista.leerOpcionMenuGastos();
            switch (op) {
                case 0: {
                    vista.mostrarGuiaGastos();
                    break;
                }
                case 1: { // Ver listado y seleccionar
                    java.util.List<Gasto> gastos;
                    try {
                        gastos = gastoDAO.listarPorDni(usuario.getDni());
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al listar gastos: " + e.getMessage());
                        break;
                    }
                    if (gastos.isEmpty()) {
                        vista.mostrarListadoGastos(gastos);
                        break; // nada para seleccionar
                    }
                    vista.mostrarListadoGastos(gastos);
                    String sel = vista.leerOpcionListadoGastos(gastos.size());
                    if (sel.equals("a")) break; // volver al menú de gastos
                    int idx = sel.charAt(0) - 'b';
                    if (idx < 0 || idx >= gastos.size()) {
                        vista.mostrarMensaje("Selección inválida.");
                        break;
                    }
                    Gasto seleccionado = gastos.get(idx);
                    vista.mostrarGastoSeleccionado(seleccionado);
                    String act = vista.leerOpcionAccionGasto();
                    if (act.equals("a")) {
                        VistaUsuario.EditGastoInput edit = vista.solicitarDatosEdicionGasto(seleccionado);
                        // Creamos un nuevo objeto y mantenemos el id
                        Gasto actualizado = new Gasto(edit.getMonto(), edit.getNombre(), edit.esFijo());
                        actualizado.setId(seleccionado.getId());
                        try {
                            gastoDAO.actualizar(actualizado);
                            recargarUsuarioDesdeBD();
                            vista.mostrarMensaje("Gasto actualizado.");
                        } catch (SQLException e) {
                            vista.mostrarMensaje("No se pudo actualizar: " + e.getMessage());
                        }
                    } else if (act.equals("b")) {
                        boolean ok = vista.confirmarEliminacionGasto(seleccionado.getNombre());
                        if (ok) {
                            try {
                                gastoDAO.eliminar(seleccionado.getId());
                                recargarUsuarioDesdeBD();
                                vista.mostrarMensaje("Gasto eliminado.");
                            } catch (SQLException e) {
                                vista.mostrarMensaje("No se pudo eliminar: " + e.getMessage());
                            }
                        }
                    }
                    break;
                }
                case 2: { // Nuevo gasto
                    VistaUsuario.GastoInput datos = vista.solicitarDatosGasto();
                    Gasto nuevo = new Gasto(datos.getMonto(), datos.getNombre(), datos.esFijo());
                    try {
                        gastoDAO.guardar(usuario.getDni(), nuevo);
                        recargarUsuarioDesdeBD();
                        vista.mostrarMensaje("Gasto agregado.");
                    } catch (SQLException e) {
                        vista.mostrarMensaje("No se pudo guardar el gasto: " + e.getMessage());
                    }
                    break;
                }
                case 3:
                    salir = true;
                    break;
                default:
                    break;
            }
        }
    }
}
